package com.actec.bsms.controller;

import com.actec.bsms.entity.Menu;
import com.actec.bsms.service.InspectDeviceTypeService;
import com.actec.bsms.service.MenuService;
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
 * 用户巡检目录接口
 *
 * @author zhangst
 * @create 2017-11-28 5:34 PM
 */
@Path("/menu")
@Scope("singleton")
@Produces(MediaType.APPLICATION_JSON)
public class MenuController extends BaseController {

    @Autowired
    MenuService menuService;

    @Autowired
    InspectDeviceTypeService inspectDeviceTypeService;

    @GET
    @Path("/get")
    public String get(@QueryParam("menuId")int menuId) {
        try {
            Menu menu = menuService.get(menuId);
            return JSON.toJSONString(menu);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JSON.toJSONString(failResult);
        }
    }

    @GET
    @Path("/getMenuList")
    public String getMenuList(@QueryParam("inspectDeviceTypeId")int inspectDeviceTypeId) {
        try {
            List<Menu> menuList = inspectDeviceTypeService.findById(inspectDeviceTypeId).getMenuList();
            return JSON.toJSONString(menuList);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JSON.toJSONString(failResult);
        }
    }

    @GET
    @Path("/setMenu")
    public String setMenu(@QueryParam("menuId")int menuId, @QueryParam("name")String name, @QueryParam("description")String description,
                          @QueryParam("inputType")String inputType, @QueryParam("inspectDeviceTypeId")int inspectDeviceTypeId) {
        try {
            menuService.setMenu(menuId, name, description, inputType, inspectDeviceTypeId);
            return JSON.toJSONString(successResult);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JSON.toJSONString(failResult);
        }
    }

    @GET
    @Path("/deleteMenu")
    public String deleteMenu(@QueryParam("menuId")int menuId, @QueryParam("inspectDeviceTypeId")int inspectDeviceTypeId) {
        try {
            menuService.delete(menuId, inspectDeviceTypeId);
            return JSON.toJSONString(successResult);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JSON.toJSONString(failResult);
        }
    }

}
