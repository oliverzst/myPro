package com.actec.bsms.service;

import com.actec.bsms.entity.Menu;
import com.actec.bsms.repository.dao.MenuDao;
import com.actec.bsms.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void save(Menu menu) {
        if (null!=menu) {
            if (menu.getId()==0) {
                menu.setCreateDate(DateUtils.getNowDate());
                menu.setUpdateDate(DateUtils.getNowDate());
                menuDao.insert(menu);
            } else {
                menu.setUpdateDate(DateUtils.getNowDate());
                menuDao.update(menu);
            }
        }
    }

    public void insertInspectDeviceMenu(int menuId, int inspectDeviceTypeId) {
        menuDao.insertInspectDeviceMenu(menuId, inspectDeviceTypeId);
    }

    public void delete(int menuId, int inspectDeviceTypeId) {
        menuDao.delete(get(menuId));
        menuDao.deleteInspectDeviceMenu(menuId, inspectDeviceTypeId);
    }

    public List<Menu> findAll() {
        return menuDao.findAllList(new Menu());
    }

    public Menu findLast() { return  menuDao.findLast(); }

}
