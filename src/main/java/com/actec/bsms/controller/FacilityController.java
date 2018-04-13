package com.actec.bsms.controller;

import com.actec.bsms.entity.Facility;
import com.actec.bsms.entity.FacilityGroup;
import com.actec.bsms.service.FacilityGroupService;
import com.actec.bsms.service.FacilityService;
import com.actec.bsms.service.UserService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * 设备及设备组操作接口
 *
 * @author zhangst
 * @create 2017-11-14 3:25 PM
 */
@Path("/facilityGroup")
@Scope("singleton")
@Produces(MediaType.APPLICATION_JSON)
public class FacilityController extends BaseController {

    @Autowired
    FacilityGroupService facilityGroupService;
    @Autowired
    UserService userService;
    @Autowired
    FacilityService facilityService;

    /**
     * 根据用户Id获取用户归属设备组
     */
    @GET
    @Path("/get")
    public String get(@QueryParam("userId")int userId) {
        try {
            int facilityGroupId = userService.get(userId, true).getFacilityGroupId();
            FacilityGroup facilityGroup = facilityGroupService.get(facilityGroupId, true);
            return JSON.toJSONString(facilityGroup);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JSON.toJSONString(failResult);
        }
    }

    /**
     * 根据设备Id获取设备信息
     */
    @GET
    @Path("/getFacility")
    public String getFacility(@QueryParam("facilityId")int facilityId) {
        try {
            Facility facility = facilityService.get(facilityId);
            return JSON.toJSONString(facility);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JSON.toJSONString(failResult);
        }
    }

    /**
     * 获取所有设备组
     */
    @GET
    @Path("/getFacilityGroup")
    public String getFacilityGroup() {
        try {
            return JSON.toJSONString(facilityGroupService.findAll());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JSON.toJSONString(failResult);
        }
    }

    /**
     * 根据用户Id获取用户所管理的所有设备信息
     */
    @GET
    @Path("/getAllFacility")
    public String getAllFacility(@QueryParam("userId")int userId) {
        try {
            //获取用户归属设备组ID
            int facilityGroupId = userService.get(userId, true).getFacilityGroupId();
            //获取用户归属设备组
            FacilityGroup facilityGroup = facilityGroupService.get(facilityGroupId, true);
            if (null!=facilityGroup) {
                return JSON.toJSONString(facilityGroup.getFacilityList());
            }
            return "[]";
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JSON.toJSONString(failResult);
        }
    }

    /**
     * 添加、修改设备组
     */
    @GET
    @Path("/setFacilityGroup")
    public String setFacilityGroup(@QueryParam("id")int facilityGroupId, @QueryParam("name")String name, @QueryParam("facilityDomains")String facilityDomains) {
        try {
            facilityGroupService.setFacilityGroup(facilityGroupId, name, facilityDomains);
            return JSON.toJSONString(successResult);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return JSON.toJSONString(failResult);
    }

    /**
     * 删除设备组
     */
    @GET
    @Path("/delFacilityGroup")
    public String delFacilityGroup(@QueryParam("facilityGroupIds")String facilityGroupIds) {
        try {
            String[] facilityGroupId = facilityGroupIds.split(",");
            for (int i=0;i<facilityGroupId.length;i++) {
                facilityGroupService.delete(Integer.parseInt(facilityGroupId[i]));
            }
            return JSON.toJSONString(successResult);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JSON.toJSONString(failResult);
        }
    }

}
