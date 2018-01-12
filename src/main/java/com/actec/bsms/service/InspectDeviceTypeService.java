package com.actec.bsms.service;

import com.actec.bsms.entity.InspectDeviceType;
import com.actec.bsms.repository.dao.InspectDeviceTypeDao;
import com.actec.bsms.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 巡检设备类型 service
 * @author zhangst
 * @create 2017-12-14 10:47 AM
 */
@Service
public class InspectDeviceTypeService {

    @Autowired
    InspectDeviceTypeDao inspectDeviceTypeDao;

    public InspectDeviceType get(int id){
        return inspectDeviceTypeDao.get(id);
    }

    public void save(InspectDeviceType inspectDeviceType) {
        if (null!=inspectDeviceType) {
            if (inspectDeviceType.getId()==0) {
                inspectDeviceType.setCreateDate(DateUtils.getNowDate());
                inspectDeviceType.setUpdateDate(DateUtils.getNowDate());
                inspectDeviceTypeDao.insert(inspectDeviceType);
            } else {
                inspectDeviceType.setUpdateDate(DateUtils.getNowDate());
                inspectDeviceTypeDao.update(inspectDeviceType);
            }
        }
    }

    public void updateInspectDeviceMenu(InspectDeviceType inspectDeviceType) {
        inspectDeviceTypeDao.deleteInspectDeviceMenu(inspectDeviceType);
        if (inspectDeviceType.getMenuList() != null && inspectDeviceType.getMenuList().size() > 0) {
            inspectDeviceTypeDao.insertInspectDeviceMenu(inspectDeviceType);
        }
    }

    public void updateInspectDeviceModule(InspectDeviceType inspectDeviceType) {
        inspectDeviceTypeDao.deleteInspectDeviceModule(inspectDeviceType);
        if (inspectDeviceType.getModuleList() != null && inspectDeviceType.getModuleList().size() > 0) {
            inspectDeviceTypeDao.insertInspectDeviceModule(inspectDeviceType);
        }
    }

    public void delete(InspectDeviceType inspectDeviceType) {
        if (null!=inspectDeviceType) {
            inspectDeviceTypeDao.delete(inspectDeviceType);
            inspectDeviceTypeDao.deleteInspectDeviceMenu(inspectDeviceType);
            inspectDeviceTypeDao.deleteInspectDeviceModule(inspectDeviceType);
        }
    }

    public List<InspectDeviceType> findAll() {
        return inspectDeviceTypeDao.findAllList(new InspectDeviceType());
    }

    public InspectDeviceType findById(int id) {
        return inspectDeviceTypeDao.findById(id);
    }

}
