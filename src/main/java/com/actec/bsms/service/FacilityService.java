package com.actec.bsms.service;

import com.actec.bsms.entity.Facility;
import com.actec.bsms.repository.dao.FacilityDao;
import com.actec.bsms.repository.dao.InspectDao;
import com.actec.bsms.repository.dao.UserDao;
import com.actec.bsms.service.cache.FacilityGroupCache;
import com.actec.bsms.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 设备接口
 *
 * @author zhangst
 * @create 2017-11-14 4:43 PM
 */
@Service
public class FacilityService {

    @Autowired
    FacilityDao facilityDao;
    @Autowired
    FacilityGroupCache facilityGroupCache;
    @Autowired
    InspectDao inspectDao;

    @Autowired
    UserDao userDao;

    public Facility get(int id){
        return facilityDao.get(id);
    }

    public void save(Facility facility) {
        if (null!=facility) {
            if (facility.getId()==0) {
                facilityDao.insert(facility);
            } else {
                facilityDao.update(facility);
                //更新缓存数据
                facilityGroupCache.updateFacility(facility);
            }
        }
    }

    public List<Facility> findAll() { return facilityDao.findAll(); }

    public void delete(int id){
        facilityDao.delete(id);
        facilityDao.deleteFacilityGroup(get(id).getDomain());
        facilityGroupCache.deleteFacility(id);
    }

    public Facility findByDomain(String domain) {
        return facilityDao.findByDomain(domain);
    }

    public void startInspect(String facilityDomain, int userId) {
        Facility facility = findByDomain(facilityDomain);
        if (StringUtils.isEmpty(facility.getStatus())) {
            facility.setStatus("当前巡检人："+userDao.get(userId).getName());
        } else {
            String[] status = facility.getStatus().split("，|：");
            String name = userDao.get(userId).getName();
            boolean isExit = false;
            for (int s=0;s<status.length;s++) {
                if (status[s].equals(name)) {
                    isExit = true;
                }
            }
            if (!isExit) {
                facility.setStatus(facility.getStatus()+"，"+ name);
            }
        }
        save(facility);
    }

    public void finishInspect(String facilityDomain, int userId, int lastInspectId) {
        Facility facility = findByDomain(facilityDomain);
        String[] s = facility.getStatus().split("，|：");
        if (s.length<=2) {
            facility.setStatus("");
        } else {
            String userName = userDao.get(userId).getName();
            for (int j=1;j<s.length;j++) {
                if (s[j].equals(userName)) {
                    if (j==1) {
                        facility.setStatus(facility.getStatus().replaceAll(userName+"，", ""));
                    } else {
                        facility.setStatus(facility.getStatus().replaceAll("，"+userName, ""));
                    }
                }
            }
        }
        facility.setLastTaskId(lastInspectId);
        facility.setLastInspect(inspectDao.findById(lastInspectId));
        save(facility);
    }

}
