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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * webscoket测试类
 *
 * @author zhangst
 * @create 2017-11-07 5:13 PM
 */
@ServerEndpoint("/websocket/TaskHandler")
@Component
public class TaskWebsocket extends BaseWebsocket {
    private static Logger logger = LoggerFactory.getLogger(TaskWebsocket.class);
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<TaskWebsocket> webSocketSet = new CopyOnWriteArraySet<TaskWebsocket>();

    private UserService userService = ApplicationContextHelper.getBean(UserService.class);

    private FacilityGroupService facilityGroupService = ApplicationContextHelper.getBean(FacilityGroupService.class);

    private TaskService taskService = ApplicationContextHelper.getBean(TaskService.class);

    @Override
    protected String getMessage(String message, User user) {
        try {
            Map<String, String> map = JSON.parseObject(message, new TypeReference<Map<String,String>>(){});
            int userId = Integer.parseInt(map.get("userId"));
            User user1 = userService.get(userId, false);
            int facilityGroupId = user1.getFacilityGroupId();
            int roleId = user1.getRoleId();
            List<Facility> facilityList = facilityGroupService.get(facilityGroupId, false).getFacilityList();
            List<Task> taskList = Lists.newArrayList();
            if (roleId<=2) {
                for (int i=0;i<facilityList.size();i++) {
                    List<Task> inspectTaskList = taskService.findInspectTaskByFacilityDomainAndUserId(userId, facilityList.get(i).getDomain(), false);
                    inspectTaskList.addAll(taskService.findRepairTaskByFacilityDomainAndUserId(userId, facilityList.get(i).getDomain(), false));
                    inspectTaskList.addAll(taskService.findDutyTaskByFacilityDomainAndUserId(userId, facilityList.get(i).getDomain(), false));
                    for (int j=0;j<inspectTaskList.size();j++) {
                        Task task = inspectTaskList.get(j);
                        task.setFacilityName(facilityList.get(i).getName());
                        taskList.add(task);
                    }
                }
            } else {
                String inspectDeviceTypes = user1.getInspectDeviceType();
                String[] inspectDeviceTypeString = inspectDeviceTypes.split(",");
                for (int i=0;i<facilityList.size();i++) {
                    List<Task> inspectTaskList = taskService.findInspectTaskByFacilityDomainAndUserId(userId, facilityList.get(i).getDomain(), false);
                    inspectTaskList.addAll(taskService.findRepairTaskByFacilityDomainAndUserId(userId, facilityList.get(i).getDomain(), false));
                    inspectTaskList.addAll(taskService.findDutyTaskByFacilityDomainAndUserId(userId, facilityList.get(i).getDomain(), false));
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
            taskList.addAll(taskService.findDutyTaskByFacilityDomainAndUserId(userId, "", false));
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
