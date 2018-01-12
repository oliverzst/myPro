package com.actec.bsms.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 基站实体类
 *
 * @author zhangst
 * @create 2017-11-13 4:32 PM
 */

public class Facility implements Serializable {

    private static final long serialVersionUID = 1L;

    /**设备类型**/
    public enum facilityType {
        /**基站          其他**/
        BASE_STATION(1), OTHERS(2);
        private int typeValue;
        facilityType(int rv){
            this.typeValue=rv;
        }
        public int getValue(){
            return typeValue;
        }
    }

    public Facility() {
    }

    public Facility(String name) {
        this.name = name;
    }

    private int id;

    private String ip;

    private String name;

    private String domain;

    private int sysNumber;

    private BigDecimal longitude;

    private BigDecimal latitude;

    private String manufacturer;

    private String address;

    private int lastTaskId;

    private String status;

    private Inspect lastInspect;

    private int type;

    //动态属性
    //控制信道频点号
    private String cchFreq;
    //逻辑信道个数
    private int tchCount;
    //连接状态
    private int rcuStatus;

    private List<Menu> menuList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public int getSysNumber() {
        return sysNumber;
    }

    public void setSysNumber(int sysNumber) {
        this.sysNumber = sysNumber;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getLastTaskId() {
        return lastTaskId;
    }

    public void setLastTaskId(int lastTaskId) {
        this.lastTaskId = lastTaskId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Inspect getLastInspect() {
        return lastInspect;
    }

    public void setLastInspect(Inspect lastInspect) {
        this.lastInspect = lastInspect;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
    }

    public String getCchFreq() {
        return cchFreq;
    }

    public void setCchFreq(String cchFreq) {
        this.cchFreq = cchFreq;
    }

    public int getTchCount() {
        return tchCount;
    }

    public void setTchCount(int tchCount) {
        this.tchCount = tchCount;
    }

    public int getRcuStatus() {
        return rcuStatus;
    }

    public void setRcuStatus(int rcuStatus) {
        this.rcuStatus = rcuStatus;
    }
}
