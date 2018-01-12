package com.actec.bsms.controller;

import com.actec.bsms.entity.InspectDeviceType;
import com.actec.bsms.service.InspectDeviceTypeService;
import com.actec.bsms.service.MenuService;
import com.actec.bsms.service.ModuleService;
import com.actec.bsms.service.UserService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * 目录操作接口
 *
 * @author zhangst
 * @create 2017-11-28 5:34 PM
 */
@Path("/inspectDeviceType")
@Scope("singleton")
@Produces(MediaType.APPLICATION_JSON)
public class InspectDeviceTypeController extends BaseController {

    @Autowired
    InspectDeviceTypeService inspectDeviceTypeService;
    @Autowired
    MenuService menuService;
    @Autowired
    ModuleService moduleService;
    @Autowired
    UserService userService;

    @GET
    @Path("/get")
    public String get(@QueryParam("inspectDeviceTypeId")int inspectDeviceTypeId) {
        try {
            InspectDeviceType inspectDeviceType = inspectDeviceTypeService.get(inspectDeviceTypeId);
            return JSON.toJSONString(inspectDeviceType);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JSON.toJSONString(failResult);
        }
    }

    @GET
    @Path("/getInspectDeviceTypeList")
    public String getInspectDeviceTypeList() {
        try {
            List<InspectDeviceType> inspectDeviceTypeList = inspectDeviceTypeService.findAll();
            return JSON.toJSONString(inspectDeviceTypeList);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JSON.toJSONString(failResult);
        }
    }

    @GET
    @Path("/setInspectDeviceType")
    public String setInspectDeviceType(@QueryParam("id")int id, @QueryParam("name")String name, @QueryParam("menuIds")String menuIds, @QueryParam("moduleIds")String moduleIds) {
        try {
            InspectDeviceType inspectDeviceType = new InspectDeviceType();
            if (id!=0) {
                inspectDeviceType = inspectDeviceTypeService.get(id);
            }
            if (name!=null) {
                inspectDeviceType.setName(name);
            }
            inspectDeviceTypeService.save(inspectDeviceType);
            return JSON.toJSONString(successResult);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JSON.toJSONString(failResult);
        }
    }

    @GET
    @Path("/deleteInspectDeviceType")
    public String deleteInspectDeviceType(@QueryParam("id")int id) {
        try {
            InspectDeviceType inspectDeviceType = inspectDeviceTypeService.get(id);
            if (null!=inspectDeviceType) {
                inspectDeviceTypeService.delete(inspectDeviceType);
            }
            //删除用户绑定的该巡检设备类型
            userService.deleteInspectDeviceType(id);
            return JSON.toJSONString(successResult);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JSON.toJSONString(failResult);
        }
    }

    @GET
    @Path("/getInspectDeviceType")
    public String getInspectDeviceType(@QueryParam("id")int id) {
        try {
            InspectDeviceType inspectDeviceType = inspectDeviceTypeService.findById(id);
            if (null!=inspectDeviceType) {
                return JSON.toJSONString(inspectDeviceType);
            }
            return JSON.toJSONString(failResult);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JSON.toJSONString(failResult);
        }
    }

}
