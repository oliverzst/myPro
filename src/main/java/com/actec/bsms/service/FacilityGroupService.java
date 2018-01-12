package com.actec.bsms.service;

import com.actec.bsms.entity.Facility;
import com.actec.bsms.entity.FacilityGroup;
import com.actec.bsms.repository.dao.FacilityGroupDao;
import com.actec.bsms.service.cache.FacilityGroupCache;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 设备组service
 * @author zhangst
 * @create 2017-11-14 4:47 PM
 */
@Service
public class FacilityGroupService {

    @Autowired
    FacilityGroupDao facilityGroupDao;
    @Autowired
    FacilityGroupCache facilityGroupCache;

    public FacilityGroup get(int id, boolean isFromSql){
        FacilityGroup facilityGroup = facilityGroupCache.get(""+id);
        if (isFromSql && null==facilityGroup) {
            facilityGroup = facilityGroupDao.get(id);
        }
        return facilityGroup;
    }

    public void save(FacilityGroup facilityGroup) {
        if (null!=facilityGroup) {
            if (facilityGroup.getId()==0) {
                facilityGroup.setCreateDate(new Date());
                facilityGroup.setUpdateDate(new Date());
                facilityGroupDao.insert(facilityGroup);
                FacilityGroup newFacilityGroup = facilityGroupDao.findNewFacilityGroup();
                facilityGroupCache.put(""+newFacilityGroup.getId(), newFacilityGroup, -1);
            } else {
                facilityGroup.setUpdateDate(new Date());
                facilityGroupDao.update(facilityGroup);
                facilityGroupCache.put(""+facilityGroup.getId(), facilityGroup, -1);
            }
        }
    }

    public void updateFacilityGroup(FacilityGroup facilityGroup) {
        facilityGroupDao.deleteFacilityGroup(facilityGroup);
        if (facilityGroup.getFacilityList() != null && facilityGroup.getFacilityList().size() > 0) {
            facilityGroupDao.insertFacilityGroup(facilityGroup);
        }
        facilityGroupCache.put(""+facilityGroup.getId(), facilityGroup, -1);
    }

    public void delete(int facilityGroupId) {
        FacilityGroup facilityGroup = facilityGroupDao.get(facilityGroupId);
        if (null!=facilityGroup) {
            facilityGroupDao.delete(facilityGroup);
            facilityGroupDao.deleteFacilityGroup(facilityGroup);
            facilityGroupCache.remove(""+facilityGroup.getId());
        }
    }

    public FacilityGroup findByName(String name) {
        return facilityGroupDao.findByName(name);
    }

    public List<FacilityGroup> findAll() {
        return facilityGroupDao.findAll();
    }

    public List<String> getFacDomains(int id, boolean isFromSql) {
        List<String> facDomains = Lists.newArrayList();
        List<Facility> facilityList = Lists.newArrayList();
        if (null!=get(id, isFromSql)) {
            facilityList = get(id, isFromSql).getFacilityList();
        }
        for (int i=0;i<facilityList.size();i++) {
            facDomains.add(facilityList.get(i).getDomain());
        }
        return facDomains;
    }

}
