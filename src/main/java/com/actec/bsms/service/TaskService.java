package com.actec.bsms.service;

import com.actec.bsms.entity.Facility;
import com.actec.bsms.entity.Task;
import com.actec.bsms.repository.dao.TableDao;
import com.actec.bsms.repository.dao.TaskDao;
import com.actec.bsms.service.cache.TaskCache;
import com.actec.bsms.utils.DateUtils;
import com.actec.bsms.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 任务接口类
 *
 * @author zhangst
 * @create 2017-11-14 3:08 PM
 */
@Service
public class TaskService {

    @Autowired
    TaskDao taskDao;

    @Autowired
    TableDao tableDao;

    @Autowired
    TaskCache taskCache;

    public Task get(int id){
        return taskDao.get(id);
    }

    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
    public void save(Task task) {
        if (null!=task) {
            if (task.getId()==0) {
                taskDao.insert(task);
                Task lastInsertTask = taskDao.findLastInsertTask();
                taskCache.put(Integer.toString(lastInsertTask.getId()), lastInsertTask);
            } else {
                taskDao.update(task);
                if (task.getState()==Task.FINISH) {
                    //已完成任务，从缓存中移除
                    taskCache.remove(Integer.toString(task.getId()));
                } else {
                    taskCache.put(Integer.toString(task.getId()), get(task.getId()));
                }
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
    public void delete(Task task) {
        if (null!=task) {
            taskDao.delete(task);
            taskCache.remove(Integer.toString(task.getId()));
        }
    }

    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
    public void createMonthTable(int year, int month) {
        final String tableName = "task";
        //将当前task表另存为上月task月表
        tableDao.createMonthTable(tableName, year, month);
        //新建一张task表
        taskDao.createTable();
        //将未完成任务迁移到新task表
        tableDao.updateMonthTable(tableName, year, month);
        tableDao.deleteMonthTable(tableName, year, month);
    }

    public List<Task> findInspectTaskByFacilityDomain(String facilityDomain) {
        return taskDao.findByFacilityDomain(facilityDomain, Task.INSPECT_TASK);
    }

    public List<Task> findAllTaskByFacilityDomainAndUserId(List<Task> taskList, int userId, Facility facility, boolean isFromSql) {
        if (!isFromSql || findByFacilityDomainAndUserIdFromRedis(userId, facility).size()!=0) {
            taskList.addAll(findByFacilityDomainAndUserIdFromRedis(userId, facility));
            return taskList;
        }
        taskList.addAll(findInspectTaskByFacilityDomainAndUserId(userId, facility));
        taskList.addAll(findRepairTaskByFacilityDomainAndUserId(userId, facility));
        taskList.addAll(findDutyTaskByFacilityDomainAndUserId(userId, facility, true));
        return taskList;
    }

    private List<Task> findByFacilityDomainAndUserIdFromRedis(int userId, Facility facility) {
        return taskCache.findByFacilityDomainAndUserId(userId, facility);
    }

    public List<Task> findInspectTaskByFacilityDomainAndUserId(int userId, Facility facility) {
        return taskDao.findByFacilityDomainAndUserId(userId, facility.getDomain(), Task.INSPECT_TASK);
    }

    public List<Task> findRepairTaskByFacilityDomainAndUserId(int userId, Facility facility) {
        return taskDao.findByFacilityDomainAndUserId(userId, facility.getDomain(), Task.REPAIR_TASK);
    }

    public List<Task> findDutyTaskByFacilityDomainAndUserId(int userId, Facility facility, boolean isFromSql) {
        if (!isFromSql || taskCache.findByFacilityDomainAndUserIdAndType(userId, facility, Task.DUTY_TASK).size()!=0) {
            return taskCache.findByFacilityDomainAndUserIdAndType(userId, facility, Task.DUTY_TASK);
        }
        return taskDao.findByFacilityDomainAndUserId(userId, facility==null?"":facility.getDomain(), Task.DUTY_TASK);
    }

    public List<Task> findByFacilityAndUserAndInspectDeviceType(String facilityDomain, int userId, int inspectDeviceType) {
        List<Task> taskList = taskDao.findByFacilityAndUserAndInspectDeviceType(facilityDomain, userId, inspectDeviceType,Task.INSPECT_TASK);
        taskList.addAll(taskDao.findByFacilityAndUserAndInspectDeviceType(facilityDomain, userId, inspectDeviceType,Task.REPAIR_TASK));
        return taskList;
    }

    public Task findByFacAndReleaseByAndInspectBy(String facilityDomain, int releaseBy,int inspectBy) {
        return taskDao.findByFacAndReleaseByAndInspectBy(facilityDomain, releaseBy, inspectBy, 1);
    }

    public List<Task> findInspectTaskByUserId(int userId) {
        return taskDao.findByInspectBy(userId, Task.INSPECT_TASK);
    }

    public List<Task> findRepairTaskByUserId(int userId) {
        return taskDao.findByInspectBy(userId, Task.REPAIR_TASK);
    }

    public List<Task> findDutyTaskByUserId(int userId) {
        return taskDao.findByInspectBy(userId, Task.DUTY_TASK);
    }

    public int checkTaskExist(String taskName, String description, String facilityDomain, int applyBy) {
        return taskDao.checkTaskExist(taskName, description, facilityDomain, applyBy);
    }

    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
    public void deleteTask(int taskId) {
        taskDao.deleteTask(taskId);
        taskCache.remove(""+taskId);
    }

    public String addTasks(String taskInfo) throws Exception {
        String result = "exist";
        Map<String, String> taskInfoMap = JSON.parseObject(taskInfo, new TypeReference<Map<String,String>>(){});
//        JSONObject object = JSONObject.fromObject(taskInfo);
        String taskName = taskInfoMap.get("taskName");
        String description = taskInfoMap.get("description");
        int userId = Integer.parseInt(taskInfoMap.get("userId"));
        int taskType = Task.INSPECT_TASK;
        if (taskInfoMap.containsKey("type")) {
            int type = Integer.parseInt(taskInfoMap.get("type"));
            if (type==Task.DUTY_TASK) {
                taskType = Task.DUTY_TASK;
            } else if (type==Task.REPAIR_TASK) {
                taskType = Task.REPAIR_TASK;
            }
        }
        String[] inspectBys = null;
        if (taskInfoMap.containsKey("inspectBy")) {
            if (!StringUtils.isEmpty(taskInfoMap.get("inspectBy"))) {
                String inspectBy = taskInfoMap.get("inspectBy");
                inspectBys = inspectBy.split(",");
            }
        }
        String facilityDomains = "";
        if (taskInfoMap.containsKey("facilityDomains")) {
            facilityDomains = taskInfoMap.get("facilityDomains");
        }
        if (taskType==Task.INSPECT_TASK || taskType==Task.REPAIR_TASK) {//发布巡检任务
            int inspectDeviceTypeId = 0;
            if (taskInfoMap.containsKey("inspectDeviceTypeId")) {
                inspectDeviceTypeId = Integer.parseInt(taskInfoMap.get("inspectDeviceTypeId"));
            }
            String[] facilityDomain = facilityDomains.split(",");
            for (int i=0; i<facilityDomain.length; i++) {
                if (null!=inspectBys) {
                    for (int j=0; j<inspectBys.length; j++) {
                        int isTaskExit = taskDao.checkTaskExist(taskName, description, facilityDomain[i], Integer.parseInt(inspectBys[j]));
                        if (isTaskExit==0) {
                            Task newTask = new Task(taskName,description,facilityDomain[i], DateUtils.getNowDate(),userId,DateUtils.getNowDate(),Integer.parseInt(inspectBys[j]), taskType);
                            newTask.setInspectDeviceTypeId(inspectDeviceTypeId);
//                            newTask.setState(Task.RECEIVE);
                            newTask.setApplyBy(Integer.parseInt(inspectBys[j]));
                            save(newTask);
                            result = "success";
                        }
                    }
                } else {
                    int isTaskExit = taskDao.checkTaskExist(taskName, description, facilityDomain[i], 0);
                    if (isTaskExit==0) {
                        Task newTask = new Task(taskName,description,facilityDomain[i],DateUtils.getNowDate(),userId,DateUtils.getNowDate(),0, taskType);
                        newTask.setInspectDeviceTypeId(inspectDeviceTypeId);
                        save(newTask);
                        result = "success";
                    }
                }
            }
        } else if (taskType==Task.DUTY_TASK){ //发布值守任务
            Date inspectTime;
            if (taskInfoMap.containsKey("inspectTime")) {
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//小写的mm表示的是分钟
                String time=taskInfoMap.get("inspectTime");
                inspectTime=sdf.parse(time);
                if (null!=facilityDomains) {
                    String[] facilityDomain = facilityDomains.split(",");
                    for (int i=0; i<facilityDomain.length; i++) {
                        if (null!=inspectBys) {
                            for (int j=0; j<inspectBys.length; j++) {
                                int isTaskExit = taskDao.checkTaskExist(taskName, description, facilityDomain[i], Integer.parseInt(inspectBys[j]));
                                if (isTaskExit==0) {
                                    Task newTask = new Task(taskName,description,facilityDomain[i],DateUtils.getNowDate(),userId,inspectTime,Integer.parseInt(inspectBys[j]), taskType);
//                                    newTask.setState(Task.RECEIVE);
                                    newTask.setApplyBy(Integer.parseInt(inspectBys[j]));
                                    save(newTask);
                                    result = "success";
                                }
                            }
                        } else {
                            int isTaskExit = taskDao.checkTaskExist(taskName, description, facilityDomain[i], 0);
                            if (isTaskExit==0) {
                                Task newTask = new Task(taskName,description,facilityDomain[i],DateUtils.getNowDate(),userId,inspectTime,0, taskType);
                                save(newTask);
                                result = "success";
                            }
                        }
                    }
                } else {
                    if (null!=inspectBys) {
                        for (int j=0; j<inspectBys.length; j++) {
                            int isTaskExit = taskDao.checkTaskExist(taskName, description, "", Integer.parseInt(inspectBys[j]));
                            if (isTaskExit==0) {
                                Task newTask = new Task(taskName,description,"",DateUtils.getNowDate(),userId,inspectTime,Integer.parseInt(inspectBys[j]), taskType);
//                                newTask.setState(Task.RECEIVE);
                                newTask.setApplyBy(Integer.parseInt(inspectBys[j]));
                                save(newTask);
                                result = "success";
                            }
                        }
                    } else {
                        int isTaskExit = taskDao.checkTaskExist(taskName, description, "", 0);
                        if (isTaskExit==0) {
                            Task newTask = new Task(taskName,description,"",DateUtils.getNowDate(),userId,inspectTime,0, taskType);
                            save(newTask);
                            result = "success";
                        }
                    }
                }
            }
        }
        return result;
    }

    public void receiveTask(int userId, int taskId) {
        Task task = taskDao.get(taskId);
        int isTaskExit = taskDao.checkReceivedTaskExist(task.getTaskName(), task.getDescription(), task.getFacilityDomain(), userId);
        //若任务重复，则合并任务
        if (isTaskExit==0) {
            if (null==task.getInspectTime()) {
                task.setInspectTime(DateUtils.getNowDate());
            }
            task.setInspectBy(userId);
            task.setState(Task.RECEIVE);
            save(task);
        } else {
            delete(task);
        }
    }

    public void excuteTask(Task task) {
        task.setSignInTime(DateUtils.getNowDate());
        task.setState(Task.EXECUTE);
        save(task);
    }

    public void submitTask(Task task, String records) {
        if (null!=task) {
            task.setInspectRecords(records);
            task.setSubmitTime(DateUtils.getNowDate());
            save(task);
        }
    }

}
