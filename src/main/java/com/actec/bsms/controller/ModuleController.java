package com.actec.bsms.controller;

import com.actec.bsms.entity.Module;
import com.actec.bsms.service.InspectDeviceTypeService;
import com.actec.bsms.service.ModuleService;
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
 * 模块接口
 *
 * @author zhangst
 * @create 2017-11-28 5:34 PM
 */
@Path("/module")
@Scope("singleton")
@Produces(MediaType.APPLICATION_JSON)
public class ModuleController extends BaseController {

    @Autowired
    ModuleService moduleService;

    @Autowired
    InspectDeviceTypeService inspectDeviceTypeService;

    @GET
    @Path("/get")
    public String get(@QueryParam("moduleId")int moduleId) {
        try {
            Module module = moduleService.get(moduleId);
            return JSON.toJSONString(module);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JSON.toJSONString(failResult);
        }
    }

    @GET
    @Path("/getModuleList")
    public String getModuleList(@QueryParam("inspectDeviceTypeId")int inspectDeviceTypeId) {
        try {
            List<Module> moduleList = inspectDeviceTypeService.findById(inspectDeviceTypeId).getModuleList();
            return JSON.toJSONString(moduleList);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JSON.toJSONString(failResult);
        }
    }

    @GET
    @Path("/setModule")
    public String setModule(@QueryParam("moduleId")int moduleId, @QueryParam("name")String name, @QueryParam("description")String description,
                            @QueryParam("inputType")String inputType, @QueryParam("inspectDeviceTypeId")int inspectDeviceTypeId) {
        try {
            Module module = new Module();
            if (moduleId!=0) {
                module = moduleService.get(moduleId);
            }
            module.setName(name);
            if (null!=description) {
                module.setDescription(description);
            }
            if (null!=inputType) {
                module.setInputType(inputType);
            }
            moduleService.save(module, inspectDeviceTypeId);
            return JSON.toJSONString(successResult);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JSON.toJSONString(failResult);
        }
    }

    @GET
    @Path("/deleteModule")
    public String deleteModule(@QueryParam("moduleId")int moduleId,  @QueryParam("inspectDeviceTypeId")int inspectDeviceTypeId) {
        try {
            moduleService.delete(moduleId, inspectDeviceTypeId);
            return JSON.toJSONString(successResult);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JSON.toJSONString(failResult);
        }
    }

}
