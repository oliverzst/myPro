package com.actec.bsms.controller;

import com.actec.bsms.entity.Inspect;
import com.actec.bsms.entity.Watch;
import com.actec.bsms.service.InspectService;
import com.actec.bsms.service.StatisticsService;
import com.actec.bsms.service.WatchService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

/**
 * 统计相关
 *
 * @author zhangst
 * @create 2017-12-21 10:01 AM
 */
@Path("/statistics")
@Scope("singleton")
@Produces(MediaType.APPLICATION_JSON)
public class StatisticsController extends BaseController{

    @Autowired
    InspectService inspectService;
    @Autowired
    WatchService watchService;
    @Autowired
    StatisticsService statisticsService;

    @GET
    @Path("/statisticsByUser")
    public String statisticsByUser(@QueryParam("userId")int userId, @QueryParam("year")String year, @QueryParam("month")String month) {
        try {
            List<Inspect> inspectList = inspectService.getInspectHistoryByUser(year, month, userId);
            List<Watch> watchList = watchService.findWatchList(year, month, userId);
            String statistics = statisticsService.statisticsByUser(inspectList, watchList, year, month, userId);
            return statistics;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return JSON.toJSONString(failResult);
    }

    @GET
    @Path("/statisticsByUserAndFacility")
    public String statisticsByUser(@QueryParam("userId")int userId, @QueryParam("facilityDomain")String facilityDomain, @QueryParam("year")String year, @QueryParam("month")String month) {
        try {
            List<Inspect> inspectList = inspectService.getInspectHistoryByUser(year, month, userId);
            String statistics = statisticsService.statisticsByDevice(inspectList, year, month, facilityDomain);
            return statistics;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return JSON.toJSONString(failResult);
    }

    @GET
    @Path("/getFacilityMap")
    public String getFacilityMap() {
        try {
            List<Inspect> inspectList = inspectService.findAllHistory();
            Map<String, String> facilityMap = statisticsService.getHistoryFacilityDomain(inspectList);
            return JSON.toJSONString(facilityMap);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return JSON.toJSONString(failResult);
    }

    @GET
    @Path("/getStartDate")
    public String getStartDate() {
        try {
            return JSON.toJSONString(inspectService.getStartDate());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return JSON.toJSONString(failResult);
    }

    @GET
    @Path("/statisticsByDevice")
    public String statisticsByDevice(@QueryParam("facilityDomain")String facilityDomain, @QueryParam("year")String year, @QueryParam("month")String month) {
        try {
            List<Inspect> inspectList = inspectService.getInspectHistoryByFacility(year, month, facilityDomain);
            String statistics = statisticsService.statisticsByDevice(inspectList, year, month, facilityDomain);
            return statistics;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return JSON.toJSONString(failResult);
    }

}
