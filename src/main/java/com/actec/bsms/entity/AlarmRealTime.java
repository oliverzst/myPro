package com.actec.bsms.entity;


import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;


public class AlarmRealTime implements Serializable {
    private static Logger logger = LoggerFactory.getLogger(AlarmRealTime.class);

    private String alarmResultId;
    private String domainName;
    private String fullDomainName;
    private String realName;
    private String fullRealName;
    private int code;
    private String codeName;
    private int level;
    private String levelName;
    private String description;
    private int alarmTimestamp;

    public String getAlarmResultId() {
        return alarmResultId;
    }

    public void setAlarmResultId(String alarmResultId) {
        this.alarmResultId = alarmResultId;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getFullDomainName() {
        return fullDomainName;
    }

    public void setFullDomainName(String fullDomainName) {
        this.fullDomainName = fullDomainName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getFullRealName() {
        return fullRealName;
    }

    public void setFullRealName(String fullRealName) {
        this.fullRealName = fullRealName;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAlarmTimestamp() {
        return alarmTimestamp;
    }

    public void setAlarmTimestamp(int alarmTimestamp) {
        this.alarmTimestamp = alarmTimestamp;
    }

    @Override
    public String toString() {
        return "AlarmRealTime{" +
                "alarmResultId='" + alarmResultId + '\'' +
                ", domainName='" + domainName + '\'' +
                ", fullDomainName='" + fullDomainName + '\'' +
                ", realName='" + realName + '\'' +
                ", fullRealName='" + fullRealName + '\'' +
                ", code=" + code +
                ", codeName='" + codeName + '\'' +
                ", level=" + level +
                ", levelName='" + levelName + '\'' +
                ", description='" + description + '\'' +
                ", alarmTimestamp=" + alarmTimestamp +
                '}';
    }

    public static String listToString(List<AlarmRealTime> alarmRealTimeList) {
        if(CollectionUtils.isEmpty(alarmRealTimeList))
            return "";
        StringBuffer sb = new StringBuffer();
        for (AlarmRealTime alarmRealTime : alarmRealTimeList) {
            sb.append(alarmRealTime.toString()).append('\\');
        }
        return sb.toString();
    }
}
