package com.actec.bsms.controller;

import com.actec.bsms.entity.Facility;
import com.actec.bsms.entity.FacilityGroup;
import com.actec.bsms.service.FacilityGroupService;
import com.actec.bsms.service.FacilityService;
import com.actec.bsms.service.UserService;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * 任务操作接口
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

    @GET
    @Path("/getAllFacility")
    public String getAllFacility(@QueryParam("userId")int userId) {
        try {
            int facilityGroupId = userService.get(userId, true).getFacilityGroupId();
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

    @GET
    @Path("/setFacilityGroup")
    public String setFacilityGroup(@QueryParam("id")int facilityGroupId, @QueryParam("name")String name, @QueryParam("facilityDomains")String facilityDomains) {
        try {
            FacilityGroup facilityGroup = facilityGroupService.get(facilityGroupId, true);
            if (null==facilityGroup) {
                facilityGroup = facilityGroupService.findByName(name);
                if (null==facilityGroup) {
                    facilityGroup = new FacilityGroup();
                }
            }
            facilityGroup.setName(name);
            String[] facilitys = facilityDomains.split(",");
            List<Facility> facilityList = Lists.newArrayList();
            for (int i=0;i<facilitys.length;i++) {
                facilityList.add(facilityService.findByDomain(facilitys[i]));
            }
            facilityGroupService.save(facilityGroup);
            facilityGroup = facilityGroupService.findByName(name);
            facilityGroup.setFacilityList(facilityList);
            facilityGroupService.updateFacilityGroup(facilityGroup);
            return JSON.toJSONString(successResult);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return JSON.toJSONString(failResult);
    }

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
