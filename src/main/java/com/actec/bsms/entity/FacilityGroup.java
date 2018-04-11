package com.actec.bsms.entity;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 基站组实体类
 *
 * @author zhangst
 * @create 2017-11-10 10:32 AM
 */

public class FacilityGroup extends DataEntity<FacilityGroup> {

    public static final int ALL_FACILITY = 1;

    private String name;	//名称

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private List<Facility> facilityList = Lists.newArrayList(); // 拥有设备列表

    public List<Facility> getFacilityList() {
        return facilityList;
    }

    public void setFacilityList(List<Facility> facilityList) {
        this.facilityList = facilityList;
    }
}
