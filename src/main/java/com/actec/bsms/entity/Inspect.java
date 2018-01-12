package com.actec.bsms.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 巡检实体类
 *
 * @author zhangst
 * @create 2017-11-13 5:04 PM
 */

public class Inspect implements Serializable {

    /**巡检类型**/
    /**自主**/
    public static final int SELF = 0;
    /**巡检**/
    public static final int TASK = 1;
    /**抢修**/
    public static final int REPAIR = 2;

    /**巡检状态**/
    /**正在进行**/
    public static final int EXECUTE = 0;
    /**结束**/
    public static final int FINISH = 1;

    private static final long serialVersionUID = 1L;

    public Inspect() {

    }

    public Inspect(String facilityDomain, Date inspectTime, int inspectBy, int type) {
        this.facilityDomain = facilityDomain;
        this.inspectTime = inspectTime;
        this.inspectType = type;
        this.inspectBy = inspectBy;
    }

    private int id;

    private String facilityDomain;

    private Facility facility;

    private Date inspectTime;

    private int inspectBy;

    private int inspectType;

    private int state;

    private String inspectRecords;

    private Date endTime;

    private User inspectUser;

    private String taskIds;

    private List<Task> taskList;

    private int inspectDeviceTypeId;

    private InspectDeviceType inspectDeviceType;

    private String troubleShootInfo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFacilityDomain() {
        return facilityDomain;
    }

    public void setFacilityDomain(String facilityDomain) {
        this.facilityDomain = facilityDomain;
    }

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    public Date getInspectTime() {
        return inspectTime;
    }

    public void setInspectTime(Date inspectTime) {
        this.inspectTime = inspectTime;
    }

    public int getInspectBy() {
        return inspectBy;
    }

    public void setInspectBy(int inspectBy) {
        this.inspectBy = inspectBy;
    }

    public User getInspectUser() {
        return inspectUser;
    }

    public void setInspectUser(User inspectUser) {
        this.inspectUser = inspectUser;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getInspectRecords() {
        return inspectRecords;
    }

    public void setInspectRecords(String inspectRecords) {
        this.inspectRecords = inspectRecords;
    }

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getInspectType() {
        return inspectType;
    }

    public void setInspectType(int inspectType) {
        this.inspectType = inspectType;
    }

    @JSONField(serialize = false)
    public String getTaskIds() {
        return taskIds;
    }

    public void setTaskIds(String taskIds) {
        this.taskIds = taskIds;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
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

    public String getTroubleShootInfo() {
        return troubleShootInfo;
    }

    public void setTroubleShootInfo(String troubleShootInfo) {
        this.troubleShootInfo = troubleShootInfo;
    }
}
