package com.actec.bsms.entity;

import java.util.List;

/**
 * 目录类
 *
 * @author zhangst
 * @create 2017-12-13 4:29 PM
 */

public class InspectDeviceType extends DataEntity<InspectDeviceType> {

    private String name;	// 名称

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    List<Menu> menuList;

    List<Module> moduleList;

    public List<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
    }

    public List<Module> getModuleList() {
        return moduleList;
    }

    public void setModuleList(List<Module> moduleList) {
        this.moduleList = moduleList;
    }
}
