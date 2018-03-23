package com.actec.bsms.service;

import com.actec.bsms.entity.Module;
import com.actec.bsms.repository.dao.ModuleDao;
import com.actec.bsms.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 模块 service
 * @author zhangst
 * @create 2017-11-14 4:47 PM
 */
@Service
public class ModuleService {

    @Autowired
    ModuleDao moduleDao;

    public Module get(int id){
        return moduleDao.get(id);
    }

    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
    public void save(Module module, int inspectDeviceTypeId) {
        if (null!=module) {
            if (module.getId()==0) {
                module.setCreateDate(DateUtils.getNowDate());
                module.setUpdateDate(DateUtils.getNowDate());
                moduleDao.insert(module);
                insertInspectDeviceModule(findLast().getId(), inspectDeviceTypeId);
            } else {
                module.setUpdateDate(DateUtils.getNowDate());
                moduleDao.update(module);
            }
        }
    }

    public void insertInspectDeviceModule(int moduleId, int inspectDeviceTypeId) {
        moduleDao.insertInspectDeviceModule(moduleId, inspectDeviceTypeId);
    }

    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
    public void delete(int menuId, int inspectDeviceTypeId) {
        moduleDao.delete(get(menuId));
        moduleDao.deleteInspectDeviceModule(menuId, inspectDeviceTypeId);
    }

    public List<Module> findAll() {
        return moduleDao.findAllList(new Module());
    }

    public Module findLast() { return  moduleDao.findLast(); }

}
