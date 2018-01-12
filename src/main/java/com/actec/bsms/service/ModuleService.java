package com.actec.bsms.service;

import com.actec.bsms.entity.Module;
import com.actec.bsms.repository.dao.ModuleDao;
import com.actec.bsms.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void save(Module module) {
        if (null!=module) {
            if (module.getId()==0) {
                module.setCreateDate(DateUtils.getNowDate());
                module.setUpdateDate(DateUtils.getNowDate());
                moduleDao.insert(module);
            } else {
                module.setUpdateDate(DateUtils.getNowDate());
                moduleDao.update(module);
            }
        }
    }

    public void insertInspectDeviceModule(int moduleId, int inspectDeviceTypeId) {
        moduleDao.insertInspectDeviceModule(moduleId, inspectDeviceTypeId);
    }

    public void delete(int menuId, int inspectDeviceTypeId) {
        moduleDao.delete(get(menuId));
        moduleDao.deleteInspectDeviceModule(menuId, inspectDeviceTypeId);
    }

    public List<Module> findAll() {
        return moduleDao.findAllList(new Module());
    }

    public Module findLast() { return  moduleDao.findLast(); }

}
