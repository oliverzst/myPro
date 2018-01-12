package com.actec.bsms.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Date;

/**
 * 任务实体类
 *
 * @author zhangst
 * @create 2017-11-13 5:04 PM
 */

public class Task implements Serializable{

    /**任务类型**/
    /**巡检任务**/
    public static final int INSPECT_TASK = 1;
    /**值守任务**/
    public static final int DUTY_TASK = 2;
    /**抢修任务**/
    public static final int REPAIR_TASK = 3;


    /**任务状态**/
    /**新建**/
    public static final int NEW = 0;
    /**已接**/
    public static final int RECEIVE = 1;
    /**正在进行**/
    public static final int EXECUTE = 2;
    /**结束**/
    public static final int FINISH = 3;

    private static final long serialVersionUID = 1L;

    public Task() {

    }

    public Task(String taskName, String description, String facilityDomain, Date releaseTime, int releaseBy, Date inspectTime, int inspectBy, int type) {
        this.taskName = taskName;
        this.description = description;
        this.facilityDomain = facilityDomain;
        this.releaseBy = releaseBy;
        this.inspectTime = inspectTime;
        this.releaseTime = releaseTime;
        this.type = type;
        this.inspectBy = inspectBy;
    }

    private int id;

    private String taskName;

    private String description;

    private String facilityDomain;

    private Date releaseTime;

    private int releaseBy;

    private Date inspectTime;

    private int inspectBy;

    private int type; //type=1表示巡检任务，type=2表示告警任务

    private int state;

    private Date signInTime;

    private String inspectRecords;

    private Date endTime;

    private int applyBy;

    private Date submitTime;

    private String facilityName;

    private User releaseUser;

    private User inspectUser;

    private int inspectDeviceTypeId;

    private InspectDeviceType inspectDeviceType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFacilityDomain() {
        return facilityDomain;
    }

    public void setFacilityDomain(String facilityDomain) {
        this.facilityDomain = facilityDomain;
    }

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    public int getReleaseBy() {
        return releaseBy;
    }

    public void setReleaseBy(int releaseBy) {
        this.releaseBy = releaseBy;
    }

    public User getReleaseUser() {
        return releaseUser;
    }

    public void setReleaseUser(User releaseUser) {
        this.releaseUser = releaseUser;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    public Date getSignInTime() {
        return signInTime;
    }

    public void setSignInTime(Date signInTime) {
        this.signInTime = signInTime;
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

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public int getApplyBy() {
        return applyBy;
    }

    public void setApplyBy(int applyBy) {
        this.applyBy = applyBy;
    }

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
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
