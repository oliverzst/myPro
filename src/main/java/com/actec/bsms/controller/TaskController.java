package com.actec.bsms.controller;

import com.actec.bsms.entity.*;
import com.actec.bsms.service.*;
import com.actec.bsms.service.cache.TaskCache;
import com.actec.bsms.utils.DateUtils;
import com.actec.bsms.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * 任务操作接口
 *
 * @author zhangst
 * @create 2017-11-14 3:25 PM
 */
@Path("/task")
@Scope("singleton")
@Produces(MediaType.APPLICATION_JSON)
public class TaskController extends BaseController{

    @Autowired
    TaskService taskService;
    @Autowired
    InspectService inspectService;
    @Autowired
    WatchService watchService;
    @Autowired
    TroubleShootService troubleShootService;
    @Autowired
    FacilityGroupService facilityGroupService;
    @Autowired
    UserService userService;
    @Autowired
    FacilityService facilityService;
    @Autowired
    TaskCache taskCache;

    @GET
    @Path("/getTask")
    public String getTask(@QueryParam("taskId")int id) {
        try {
            Inspect inspect = inspectService.get(id);
            String result = JSON.toJSONString(inspect);
            return result;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return JSON.toJSONString(failResult);
    }

    @GET
    @Path("/setTasks")
    public String setTasks(@QueryParam("taskId")int id) {
        try {
            Task task = taskService.get(id);
            taskCache.put(String.valueOf(id), task, -1);
            return JSON.toJSONString(successResult);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return JSON.toJSONString(failResult);
    }

    @GET
    @Path("/getMyTask")
    public String getMyTask(@QueryParam("userId")int userId) {
        try {
            List<Task> taskList = taskService.findInspectTaskByUserId(userId);
            taskList.addAll(taskService.findRepairTaskByUserId(userId));
            return JSON.toJSONString(taskList, SerializerFeature.WriteMapNullValue);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return JSON.toJSONString(failResult);
    }

    @GET
    @Path("/getAllTask")
    public String getAllTask(@QueryParam("userId")int userId) {
        try {
            int facilityGroupId = userService.get(userId, true).getFacilityGroupId();
            List<Facility> facilityList = facilityGroupService.get(facilityGroupId, true).getFacilityList();
            List<Task> taskList = Lists.newArrayList();
            for (int i=0;i<facilityList.size();i++) {
                taskList.addAll(taskService.findInspectTaskByFacilityDomain(facilityList.get(i).getDomain()));
            }
            return JSON.toJSONString(taskList, SerializerFeature.WriteMapNullValue);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return JSON.toJSONString(failResult);
    }

    @GET
    @Path("/getMyDutyTask")
    public String getMyAlmTask(@QueryParam("userId")int userId) {
        try {
            List<Task> taskList = taskService.findDutyTaskByUserId(userId);
            return JSON.toJSONString(taskList, SerializerFeature.WriteMapNullValue);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return JSON.toJSONString(failResult);
    }

    @GET
    @Path("/addTasks")
    public String addTasks(@QueryParam("taskInfo")String taskInfo) {
        try {
            String result = taskService.addTasks(taskInfo);
            return JSON.toJSONString(result);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return JSON.toJSONString(failResult);
    }

    @GET
    @Path("/receiveTask")
    public String receiveTask(@QueryParam("userId")int userId, @QueryParam("taskId")int taskId) {
        try {
            taskService.receiveTask(userId, taskId);
            return JSON.toJSONString(successResult);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return JSON.toJSONString(failResult);
    }

    @GET
    @Path("/signWatchTask")
    public String signWatchTask(@QueryParam("taskId")int taskId) {
        try {
            //更新任务
            Task task = taskService.get(taskId);
            taskService.excuteTask(task);

            //开始值守
            watchService.startWatch(taskId, task.getInspectBy());
            return JSON.toJSONString(successResult);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return JSON.toJSONString(failResult);
    }

    @GET
    @Path("/signBaseStation")
    //基站签到
    public String signBaseStation(@QueryParam("facilityDomain")String facilityDomain, @QueryParam("longitude")String longitude,
                                  @QueryParam("latitude")String latitude) {
        //验证用户当前经纬度是否满足签到条件
        return JSON.toJSONString(successResult);
    }

    @GET
    @Path("/signNoTask")
    public String signNoTask(@QueryParam("userId")int userId, @QueryParam("inspectDeviceType")int inspectDeviceType, @QueryParam("facilityDomain")String facilityDomain) {
        //工程人员自行巡检情况，没有接任务，此时需新建任务
        try {
            Inspect inspect = inspectService.findByFacilityAndUserAndInspectDeviceType(facilityDomain, userId, inspectDeviceType);
            if (null==inspect) {
                inspect = new Inspect();
            }
            if (inspectService.findByFacilityAndUser(facilityDomain, userId).size()==0) {
                //更新设备状态
                facilityService.startInspect(facilityDomain, userId);
            }
            List<Task> taskList = taskService.findByFacilityAndUserAndInspectDeviceType(facilityDomain, userId, inspectDeviceType);
            if (taskList.size()==0) { //自主巡检
                inspectService.excuteInspect(inspect, facilityDomain, userId, Inspect.SELF, inspectDeviceType);
            } else {  //任务巡检
                int type = Inspect.TASK;
                for (int i=0;i<taskList.size();i++) {
                    Task task = taskList.get(i);
                    if (task.getType()==Task.REPAIR_TASK) {
                        type = Inspect.REPAIR;
                    }
                    taskService.excuteTask(task);
                }
                inspectService.excuteInspect(inspect, facilityDomain, userId, type, inspectDeviceType);
            }
            int inspectId = inspect.getId();
            if (inspectId==0) {
                inspectId = inspectService.findByFacilityAndUserAndInspectDeviceType(facilityDomain, userId, inspectDeviceType).getId();
            }
            return JSON.toJSONString(inspectId);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return JSON.toJSONString(failResult);
    }

    @POST
    @Path("/submitWatch")
    public String submitWatch(@FormParam("taskId")int taskId, @FormParam("records")String records){
        try {

            //提交并完成值守
            watchService.finishWatch(taskId, records);

            //更新任务信息
            Task task = taskService.get(taskId);
            taskService.submitTask(task, records);

            return JSON.toJSONString(successResult);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JSON.toJSONString(failResult);
        }
    }

    @POST
    @Path("/submitInspect")
    public String submitInspect(@FormParam("inspectId")int inspectId, @FormParam("userId")int userId, @FormParam("inspectDeviceType")int inspectDeviceType,
                                @FormParam("facilityDomain")String facilityDomain, @FormParam("taskIds")String taskIds,
                                @FormParam("records")String records, @FormParam("troubleShootInfo")String troubleShootInfo) {
        try {
            Inspect inspect = inspectService.findById(inspectId);

            //更新任务信息
            if (!StringUtils.isEmpty(taskIds)) {
                String[] taskIdString = taskIds.split(",");
                for (int i=0;i<taskIdString.length;i++) {
                    Task task = taskService.get(Integer.parseInt(taskIdString[i]));
                    taskService.submitTask(task, records);
                }
                inspect.setTaskIds(taskIds);
            }

            //结束当前巡检
            inspectService.finishInspect(inspect, records, troubleShootInfo);

            //保存故障处理记录
            List<TroubleShoot> troubleShootList = JSON.parseArray(troubleShootInfo, TroubleShoot.class);
            troubleShootService.saveTroubleShoots(troubleShootList, facilityDomain, userId, inspectDeviceType, inspectId);

            //若该用户在该基站上没有未完成巡检 则更新设备状态
            if (inspectService.findByFacilityAndUser(facilityDomain, userId).size()==0) {
                facilityService.finishInspect(facilityDomain, userId, inspect.getId());
            }

            return JSON.toJSONString(successResult);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return JSON.toJSONString(failResult);
    }

    @GET
    @Path("/getSubmitTasks")
    public String getSubmitTasks(@QueryParam("facilityDomain")String facilityDomain, @QueryParam("userId")int userId,
                                 @QueryParam("inspectDeviceTypeId")int inspectDeviceTypeId) {
        try {
            List<Task> taskList = taskService.findByFacilityAndUserAndInspectDeviceType(facilityDomain, userId, inspectDeviceTypeId);
            String result = JSON.toJSONString(taskList, SerializerFeature.WriteMapNullValue);
            return result;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JSON.toJSONString(failResult);
        }
    }

    @GET
    @Path("/finishTask")
    public String finishTask(@QueryParam("taskIds")String taskIds) {
        try {
            String[] taskIdsSting = taskIds.split(",");
            for (int i=0;i<taskIdsSting.length;i++) {
                Task task = taskService.get(Integer.parseInt(taskIdsSting[i]));
                task.setState(Task.FINISH);
                task.setEndTime(DateUtils.getNowDate());
                taskService.save(task);
            }
            return JSON.toJSONString(successResult);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return JSON.toJSONString(failResult);
    }

    @GET
    @Path("/deleteTask")
    public String deleteTask(@QueryParam("taskIds")String taskIds) {
        try {
            String[] taskIdsSting = taskIds.split(",");
            for (int i=0;i<taskIdsSting.length;i++) {
                taskService.deleteTask(Integer.parseInt(taskIdsSting[i]));
            }
            return JSON.toJSONString(successResult);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return JSON.toJSONString(failResult);
    }

    @GET
    @Path("/getFacilityTaskHistory")
    public String getFacilityTaskHistory(@QueryParam("facilityDomain")String facilityDomain, @QueryParam("year")String year,
                                         @QueryParam("month")String month) {
        try {
            List<Inspect> historyInspectList = inspectService.getInspectHistoryByFacility(year, month, facilityDomain);
            String result = JSON.toJSONString(historyInspectList, SerializerFeature.WriteMapNullValue);
            return result;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return JSON.toJSONString(failResult);
    }

    @GET
    @Path("/getUserTaskHistory")
    public String getUserTaskHistory(@QueryParam("userId")int userId, @QueryParam("year")String year, @QueryParam("month")String month) {
        try {
            List<Inspect> historyInspectList = inspectService.getInspectHistoryByUser(year, month, userId);
            String result = JSON.toJSONString(historyInspectList, SerializerFeature.WriteMapNullValue);
            return result;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return JSON.toJSONString(failResult);
    }

    @GET
    @Path("/getUserWatchHistory")
    public String getUserDutyHistory(@QueryParam("userId")int userId, @QueryParam("year")String year, @QueryParam("month")String month) {
        try {
            List<Watch> historyWatchList = watchService.findWatchList(year, month, userId);
            String result = JSON.toJSONString(historyWatchList, SerializerFeature.WriteMapNullValue);
            return result;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return JSON.toJSONString(failResult);
    }

    @GET
    @Path("/exportInspectExcelByUser")
    public String exportInspectExcelByUser(@QueryParam("userId")int userId, @QueryParam("year")String year, @QueryParam("month")String month) {
        try {
            User user = userService.get(userId, true);
            String fileName = "巡检记录"+"_"+user.getName()+".xls";
            if (StringUtils.isEmpty(year)) {
                year = "";
            }
            if (StringUtils.isEmpty(month)) {
                month = "";
            }
            List<Inspect> inspectList = inspectService.getInspectHistoryByUser(year, month, userId);
            List<Watch> watchList = watchService.findWatchList(year, month, userId);
            inspectService.exportInspectExcel(inspectList, watchList, fileName, response);
            return JSON.toJSONString(successResult);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return JSON.toJSONString(failResult);
    }

    @GET
    @Path("/exportInspectExcelByFacility")
    public String exportInspectExcelByFacility(@QueryParam("facilityDomain")String facilityDomain, @QueryParam("year")String year, @QueryParam("month")String month) {
        try {
            Facility facility = facilityService.findByDomain(facilityDomain);
            String fileName = "巡检记录"+"_"+facility.getName()+".xls";
            if (StringUtils.isEmpty(year)) {
                year = "";
            }
            if (StringUtils.isEmpty(month)) {
                month = "";
            }
            List<Inspect> inspectList = inspectService.getInspectHistoryByFacility(year, month, facilityDomain);
            inspectService.exportInspectExcel(inspectList, null, fileName, response);
            return JSON.toJSONString(successResult);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return JSON.toJSONString(failResult);
    }

}
