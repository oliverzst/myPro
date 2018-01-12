package com.actec.bsms.entity.vo;

/**
 * @author zhangst
 * @create 2017-11-29 10:57 AM
 */

public class FinishTaskRequset {

    private int userId;	//用户ID

    private String facilityDomain; //设备域名

    private String records; //完成任务记录

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFacilityDomain() {
        return facilityDomain;
    }

    public void setFacilityDomain(String facilityDomain) {
        this.facilityDomain = facilityDomain;
    }

    public String getRecords() {
        return records;
    }

    public void setRecords(String records) {
        this.records = records;
    }
}
