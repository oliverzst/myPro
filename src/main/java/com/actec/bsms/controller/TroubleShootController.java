package com.actec.bsms.controller;

import com.actec.bsms.entity.TroubleShoot;
import com.actec.bsms.service.TroubleShootService;
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
 * 故障查询统计接口类
 *
 * @author zhangst
 * @create 2017-12-19 11:55 AM
 */
@Path("/trouble")
@Scope("singleton")
@Produces(MediaType.APPLICATION_JSON)
public class TroubleShootController extends BaseController{

    @Autowired
    TroubleShootService troubleShootService;

    @GET
    @Path("/getHistory")
    public String getHistory(@QueryParam("userId")int userId, @QueryParam("moduleId")int moduleId, @QueryParam("inspectDeviceType")int inspectDeviceType,
                             @QueryParam("facilityDomain")String facilityDomain, @QueryParam("year")String year,
                             @QueryParam("month")String month) {
        try {
            List<TroubleShoot> historyTroubleShootList = troubleShootService.findTroubleShootList(userId, moduleId, inspectDeviceType, facilityDomain, year, month);
            return JSON.toJSONString(historyTroubleShootList);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return JSON.toJSONString(failResult);
    }

    //未完待续

}
