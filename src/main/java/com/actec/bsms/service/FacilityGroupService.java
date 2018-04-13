package com.actec.bsms.service;

import com.actec.bsms.entity.Facility;
import com.actec.bsms.entity.FacilityGroup;
import com.actec.bsms.repository.dao.FacilityDao;
import com.actec.bsms.repository.dao.FacilityGroupDao;
import com.actec.bsms.service.cache.FacilityGroupCache;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
    @Autowired
    FacilityDao facilityDao;

    public FacilityGroup get(int id){
        FacilityGroup facilityGroup = facilityGroupDao.get(id);
        return facilityGroup;
    }

    public FacilityGroup get(int id, boolean isFromSql){
        FacilityGroup facilityGroup = facilityGroupCache.get(""+id);
        if (isFromSql && null==facilityGroup) {
            facilityGroup = facilityGroupDao.get(id);
        }
        return facilityGroup;
    }

    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
    public void save(FacilityGroup facilityGroup, List<Facility> facilityList) {
        if (null!=facilityGroup) {
            if (facilityGroup.getId()==0) {
                facilityGroup.setCreateDate(new Date());
                facilityGroup.setUpdateDate(new Date());
                facilityGroupDao.insert(facilityGroup);
            } else {
                facilityGroup.setUpdateDate(new Date());
                facilityGroupDao.update(facilityGroup);
            }
            facilityGroup = findByName(facilityGroup.getName());
            //比较设备列表是否改变，若改变则更新设备-设备组关联表数据
            if (null==facilityGroup.getFacilityList() || !JSON.toJSONString(facilityList).equals(JSON.toJSONString(facilityGroup.getFacilityList()))) {
                facilityGroup.setFacilityList(facilityList);
                updateFacilityGroup(facilityGroup);
            }
            //更新缓存
            facilityGroupCache.put(""+facilityGroup.getId(), facilityGroup, -1);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
    public void setFacilityGroup(int facilityGroupId, String name, String facilityDomains) {
        FacilityGroup facilityGroup = get(facilityGroupId, true);
        //根据设备组名称去重
        if (null==facilityGroup) {
            facilityGroup = findByName(name);
            if (null==facilityGroup) {
                facilityGroup = new FacilityGroup();
            }
        }
        facilityGroup.setName(name);
        //获取设备组关联设备列表
        String[] facilityS = facilityDomains.split(",");
        List<Facility> facilityList = Lists.newArrayList();
        for (int i=0;i<facilityS.length;i++) {
            facilityList.add(facilityDao.findByDomain(facilityS[i]));
        }
        //保存
        save(facilityGroup, facilityList);
    }

    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
    public void updateFacilityGroup(FacilityGroup facilityGroup) {
        facilityGroupDao.deleteFacilityGroup(facilityGroup);
        if (facilityGroup.getFacilityList() != null && facilityGroup.getFacilityList().size() > 0) {
            facilityGroupDao.insertFacilityGroup(facilityGroup);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
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

    public Map<String, String> getFacDomains(int id, boolean isFromSql) {
        Map<String, String> facDomainMap = Maps.newHashMap();
        List<Facility> facilityList = Lists.newArrayList();
        if (null!=get(id, isFromSql)) {
            facilityList = get(id, isFromSql).getFacilityList();
        }
        for (int i=0;i<facilityList.size();i++) {
            facDomainMap.put(facilityList.get(i).getDomain(), facilityList.get(i).getName());
        }
        return facDomainMap;
    }

}
