package com.actec.bsms.entity;

import java.util.Date;

/**
 * 故障处理记录类
 *
 * @author zhangst
 * @create 2017-12-14 3:04 PM
 */

public class TroubleShoot {

    /**故障是否解决**/
    /**未解决**/
    public static final int UNRESOLVED = 0;
    /**已解决**/
    public static final int RESOLVED = 1;

    private static final long serialVersionUID = 1L;

    public TroubleShoot(){}

    private int id;

    private int inspectId;

    private String facilityDomain;

    private Facility facility;

    private int userId; //故障处理人ID

    private User user; //故障处理人

    private int moduleId; //故障模块ID

    private Module module; //故障模块

    private int number; //序号

    private String description; //故障现象描述

    private String process;  //故障处理过程描述

    private int isResolve; //故障是否解决

    private Date endTime; //处理时间

    private int inspectDeviceTypeId;

    private InspectDeviceType inspectDeviceType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInspectId() {
        return inspectId;
    }

    public void setInspectId(int inspectId) {
        this.inspectId = inspectId;
    }

    public String getFacilityDomain() {
        return facilityDomain;
    }

    public void setFacilityDomain(String facilityDomain) {
        this.facilityDomain = facilityDomain;
    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public int getIsResolve() {
        return isResolve;
    }

    public void setIsResolve(int isResolve) {
        this.isResolve = isResolve;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getInspectDeviceTypeId() {
        return inspectDeviceTypeId;
    }

    public void setInspectDeviceTypeId(int inspectDeviceTypeId) {
        this.inspectDeviceTypeId = inspectDeviceTypeId;
    }

    public InspectDeviceType getInspectDeviceType() {
        return inspectDeviceType;
    }

    public void setInspectDeviceType(InspectDeviceType inspectDeviceType) {
        this.inspectDeviceType = inspectDeviceType;
    }
}
