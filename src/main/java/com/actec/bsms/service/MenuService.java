package com.actec.bsms.service;

import com.actec.bsms.entity.Menu;
import com.actec.bsms.repository.dao.MenuDao;
import com.actec.bsms.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 目录 service
 * @author zhangst
 * @create 2017-11-14 4:47 PM
 */
@Service
public class MenuService {

    @Autowired
    MenuDao menuDao;

    public Menu get(int id){
        return menuDao.get(id);
    }

    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
    public void save(Menu menu, int inspectDeviceTypeId) {
        if (null!=menu) {
            if (menu.getId()==0) {
                menu.setCreateDate(DateUtils.getNowDate());
                menu.setUpdateDate(DateUtils.getNowDate());
                menuDao.insert(menu);
                insertInspectDeviceMenu(findLast().getId(), inspectDeviceTypeId);
            } else {
                menu.setUpdateDate(DateUtils.getNowDate());
                menuDao.update(menu);
            }
        }
    }

    public void setMenu(int menuId, String name, String description, String inputType, int inspectDeviceTypeId){
        Menu menu;
        if (menuId!=0) {
            menu = get(menuId);
        } else {
            menu = new Menu();
        }
        menu.setName(name);
        if (null!=description) {
            menu.setDescription(description);
        }
        if (null!=inputType) {
            menu.setInputType(inputType);
        }
        save(menu, inspectDeviceTypeId);
    }

    public void insertInspectDeviceMenu(int menuId, int inspectDeviceTypeId) {
        menuDao.insertInspectDeviceMenu(menuId, inspectDeviceTypeId);
    }

    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
    public void delete(int menuId, int inspectDeviceTypeId) {
        menuDao.delete(get(menuId));
        menuDao.deleteInspectDeviceMenu(menuId, inspectDeviceTypeId);
    }

    public List<Menu> findAll() {
        return menuDao.findAllList(new Menu());
    }

    public Menu findLast() { return  menuDao.findLast(); }

}
