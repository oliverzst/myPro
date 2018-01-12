package com.actec.bsms.entity.vo;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * 告警
 *
 * @author zhangst
 * @create 2017-11-16 3:49 PM
 */

public class Alarm {

    private String alarmId;

    private String domainName;

    private String fullDomainName;

    private String realName;

    private String fullRealName;

    private int code;

    private String codeName;

    private int level;

    private String levelName;

    private String description;

    private Date alarmTimestamp;

    private String proposal;

    public String getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    public Date getAlarmTimestamp() {
        return alarmTimestamp;
    }

    public void setAlarmTimestamp(Date alarmTimestamp) {
        this.alarmTimestamp = alarmTimestamp;
    }

    public String getProposal() {
        return proposal;
    }

    public void setProposal(String proposal) {
        this.proposal = proposal;
    }

    public String getFullDomainName() {
        return fullDomainName;
    }

    public void setFullDomainName(String fullDomainName) {
        this.fullDomainName = fullDomainName;
    }

    public String getFullRealName() {
        return fullRealName;
    }

    public void setFullRealName(String fullRealName) {
        this.fullRealName = fullRealName;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }
}
