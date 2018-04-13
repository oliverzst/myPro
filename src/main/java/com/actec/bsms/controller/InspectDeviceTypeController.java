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
 * 用户巡检设备类型操作接口
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

    /**
     * 根据Id获取设备巡检类型
     */
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

    /**
     * 获取所有设备巡检类型
     */
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

    /**
     * 添加、修改设备巡检类型
     */
    @GET
    @Path("/setInspectDeviceType")
    public String setInspectDeviceType(@QueryParam("id")int id, @QueryParam("name")String name) {
        try {
            inspectDeviceTypeService.setInspectDeviceType(id, name);
            return JSON.toJSONString(successResult);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JSON.toJSONString(failResult);
        }
    }

    /**
     * 删除设备巡检类型
     */
    @GET
    @Path("/deleteInspectDeviceType")
    public String deleteInspectDeviceType(@QueryParam("id")int id) {
        try {
            inspectDeviceTypeService.delete(id);
            //同时删除用户绑定的该巡检设备类型
            userService.deleteInspectDeviceType(id);
            return JSON.toJSONString(successResult);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JSON.toJSONString(failResult);
        }
    }

}
