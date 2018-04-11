package com.actec.bsms.webscoket;

import com.actec.bsms.entity.Facility;
import com.actec.bsms.entity.Task;
import com.actec.bsms.entity.User;
import com.actec.bsms.service.FacilityGroupService;
import com.actec.bsms.service.TaskService;
import com.actec.bsms.service.UserService;
import com.actec.bsms.utils.ApplicationContextHelper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.List;
import java.util.Map;

/**
 * webscoket任务类
 *
 * @author zhangst
 * @create 2017-11-07 5:13 PM
 */
@ServerEndpoint("/websocket/TaskHandler")
@Component
public class TaskWebsocket extends BaseWebsocket {

    private static UserService userService = ApplicationContextHelper.getBean(UserService.class);
    private static FacilityGroupService facilityGroupService = ApplicationContextHelper.getBean(FacilityGroupService.class);
    private static TaskService taskService = ApplicationContextHelper.getBean(TaskService.class);

    @Override
    protected String getMessage(String message, Session session) {
        try {
            Map<String, String> map = JSON.parseObject(message, new TypeReference<Map<String,String>>(){});
            final int userId = Integer.parseInt(map.get("userId"));
            User user = userService.get(userId, false);
            final int facilityGroupId = user.getFacilityGroupId();
            final int roleId = user.getRoleId();
            List<Facility> facilityList = facilityGroupService.get(facilityGroupId, false).getFacilityList();
            List<Task> taskList = Lists.newArrayList();
            //根据用户权限级别推送不同的数据
            if (roleId<=User.MANAGER) {
                for (int i=0,len=facilityList.size();i<len;i++) {
                    taskList = taskService.findAllTaskByFacilityDomainAndUserId(taskList, userId, facilityList.get(i), false);
                }
            } else {
                //根据用户的巡检设备类型 筛选数据
                String inspectDeviceTypes = user.getInspectDeviceType();
                String[] inspectDeviceTypeString = inspectDeviceTypes.split(",");
                List<Task> inspectTaskList = Lists.newArrayList();
                for (int i=0;i<facilityList.size();i++) {
                    inspectTaskList = taskService.findAllTaskByFacilityDomainAndUserId(inspectTaskList, userId, facilityList.get(i), false);
                    for (int j=0;j<inspectTaskList.size();j++) {
                        Task task = inspectTaskList.get(j);
                        if (task.getType()!=Task.DUTY_TASK) {
                            String inspectDeviceType = String.valueOf(task.getInspectDeviceTypeId());
                            for (int k=0;k<inspectDeviceTypeString.length;k++) {
                                if (inspectDeviceType.equals(inspectDeviceTypeString[k])) {
                                    task.setFacilityName(facilityList.get(i).getName());
                                    taskList.add(task);
                                }
                            }
                        } else {
                            taskList.add(task);
                        }
                    }
                }
            }
            //查询设备为null的值守任务
            taskList.addAll(taskService.findDutyTaskByFacilityDomainAndUserId(userId, null, false));
            return JSON.toJSONString(taskList);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return "";
    }
    @Override
    @OnOpen
    public void start(Session session) {
        super.start(session);
    }

    @Override
    @OnClose
    public void close(Session session) {
        super.close(session);
    }

    @Override
    @OnMessage
    public void incoming(Session session, String request) {
        super.incoming(session, request);
    }

    @Override
    @OnError
    public void onError(Session session, Throwable t) throws Throwable {
        super.onError(session,t);
    }
}
