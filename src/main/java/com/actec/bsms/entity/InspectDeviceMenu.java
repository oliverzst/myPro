package com.actec.bsms.entity;

/**
 * 设备类型与目录关联表
 *
 * @author zhangst
 * @create 2017-11-28 3:59 PM
 */

public class InspectDeviceMenu {

    private int inspectDeviceTypeId;	//巡检设备类型

    private int menuId; //目录ID

    public int getInspectDeviceTypeId() {
        return inspectDeviceTypeId;
    }

    public void setInspectDeviceTypeId(int inspectDeviceTypeId) {
        this.inspectDeviceTypeId = inspectDeviceTypeId;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }
}
