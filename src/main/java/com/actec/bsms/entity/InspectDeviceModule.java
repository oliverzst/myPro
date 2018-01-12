package com.actec.bsms.entity;

/**
 * 设备类型与目录关联表
 *
 * @author zhangst
 * @create 2017-11-28 3:59 PM
 */

public class InspectDeviceModule {

    private int inspectDeviceTypeId;	//巡检设备类型

    private int moduleId; //目录ID

    public int getInspectDeviceTypeId() {
        return inspectDeviceTypeId;
    }

    public void setInspectDeviceTypeId(int inspectDeviceTypeId) {
        this.inspectDeviceTypeId = inspectDeviceTypeId;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }
}
