package com.actec.bsms.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * 值守实体类
 *
 * @author zhangst
 * @create 2017-12-14 6:04 PM
 */

public class Watch {

    /**值守状态**/
    /**正在进行**/
    public static final int EXECUTE = 0;
    /**结束**/
    public static final int FINISH = 1;

    private static final long serialVersionUID = 1L;

    public Watch() {

    }

    private int id;

    private Date watchTime;

    private int watchBy;

    private int state;

    private String watchRecords; //值守记录

    private Date endTime;

    private User watchUser;

    private int taskId;

    private Task task;

    private Facility facility;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    public Date getWatchTime() {
        return watchTime;
    }

    public void setWatchTime(Date watchTime) {
        this.watchTime = watchTime;
    }

    public int getWatchBy() {
        return watchBy;
    }

    public void setWatchBy(int watchBy) {
        this.watchBy = watchBy;
    }

    public String getWatchRecords() {
        return watchRecords;
    }

    public void setWatchRecords(String watchRecords) {
        this.watchRecords = watchRecords;
    }

    public User getWatchUser() {
        return watchUser;
    }

    public void setWatchUser(User watchUser) {
        this.watchUser = watchUser;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        if (facility!=null) {
            this.facility = facility;
        }
        this.facility = new Facility();
    }
}
