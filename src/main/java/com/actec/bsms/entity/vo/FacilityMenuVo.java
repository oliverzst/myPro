package com.actec.bsms.entity.vo;

import com.actec.bsms.entity.Menu;

import java.util.List;

/**
 * 设备类型与目录关联表
 *
 * @author zhangst
 * @create 2017-11-28 3:59 PM
 */

public class FacilityMenuVo {

    private int facilityType;	//设备类型

    private List<Menu> menuList;  //目录

    public int getFacilityType() {
        return facilityType;
    }

    public void setFacilityType(int facilityType) {
        this.facilityType = facilityType;
    }

    public List<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
    }
}
