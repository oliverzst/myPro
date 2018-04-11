package com.actec.bsms.service.cache;

import com.actec.bsms.entity.Facility;
import com.actec.bsms.entity.FacilityGroup;
import com.actec.bsms.repository.dao.FacilityGroupDao;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 测试缓存
 *
 * @author zhangst
 * @create 2018-01-11 3:38 PM
 */

@Service
public class FacilityGroupCache extends IRedisService<FacilityGroup> {
    private static final String REDIS_KEY = "FACILITY_REDIS_KEY";

    @Autowired
    FacilityGroupDao facilityGroupDao;

    @Override
    protected String getRedisKey() {
        return this.REDIS_KEY;
    }

    public void putAllFacilityGroups(List<FacilityGroup> facilityGroupList) {
        if (!CollectionUtils.isEmpty(facilityGroupList)) {
            Map<String, FacilityGroup> map = Maps.newHashMap();
            for (FacilityGroup facilityGroup: facilityGroupList) {
                map.put(String.valueOf(facilityGroup.getId()), facilityGroup);
            }
            this.putAll(map, -1);
        }
    }

    public void init() {
        //初始化，将所有未完成的Task存入缓存中
        this.empty();
        List<FacilityGroup> facilityGroupList = facilityGroupDao.findAll();
        putAllFacilityGroups(facilityGroupList);
    }

    public void deleteFacility(int facilityId) {
        List<FacilityGroup> facilityGroupList = this.getAll();
        for (int i=0;i<facilityGroupList.size();i++) {
            FacilityGroup facilityGroup = facilityGroupList.get(i);
            List<Facility> facilityList = facilityGroup.getFacilityList();
            for (int j=0;j<facilityList.size();j++) {
                Facility facility = facilityList.get(j);
                if (facility.getId() == facilityId) {
                    facilityList.remove(j);
                    facilityGroup.setFacilityList(facilityList);
                    this.put(String.valueOf(facilityGroup.getId()), facilityGroup, -1);
                    break;
                }
            }
        }
    }

    public void updateFacility(Facility facility) {
        List<FacilityGroup> facilityGroupList = this.getAll();
        for (int i=0;i<facilityGroupList.size();i++) {
            FacilityGroup facilityGroup = facilityGroupList.get(i);
            List<Facility> facilityList = facilityGroup.getFacilityList();
            for (int j=0;j<facilityList.size();j++) {
                Facility oldFacility = facilityList.get(j);
                if (facility.getDomain().equals(oldFacility.getDomain())) {
                    facilityList.remove(j);
                    facilityList.add(j, facility);
                    facilityGroup.setFacilityList(facilityList);
                    this.put(String.valueOf(facilityGroup.getId()), facilityGroup, -1);
                    break;
                }
            }
        }
    }

    public String findRealNameByDomain(String domain) {
        List<FacilityGroup> facilityGroupList = this.getAll();
        for (int i=0;i<facilityGroupList.size();i++) {
            FacilityGroup facilityGroup = facilityGroupList.get(i);
            List<Facility> facilityList = facilityGroup.getFacilityList();
            for (int j=0;j<facilityList.size();j++) {
                Facility facility = facilityList.get(j);
                if (facility.getDomain().equals(domain)) {
                    return facility.getName();
                }
            }
        }
        return "";
    }

}
