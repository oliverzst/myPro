package com.actec.bsms.service.cache;

import com.actec.bsms.entity.Facility;
import com.actec.bsms.entity.Task;
import com.actec.bsms.repository.dao.TaskDao;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 测试缓存
 *
 * @author zhangst
 * @create 2018-01-11 3:38 PM
 */

@Service
public class TaskCache extends IRedisService<String> {

    private static final String REDIS_KEY = "TASK_REDIS_KEY";

    private static final String ALL_TASKS = "ALL_TASKS";

    @Autowired
    TaskDao taskDao;

    @Override
    protected String getRedisKey() {
        return this.REDIS_KEY;
    }

    public void put(String key, Task task) {
        super.put(key, JSON.toJSONString(task), -1);
        updateAllTasks();
    }

    public void remove(String key) {
        super.remove(key);
        updateAllTasks();
    }

    //更新ALL_TASKS字段
    private void updateAllTasks() {
        List<Task> allTaskList = Lists.newArrayList();
        super.remove(ALL_TASKS);
        List<String> allTasks = super.getAll();
        for (int i=0;i<allTasks.size();i++) {
            allTaskList.add(JSON.parseObject(allTasks.get(i), Task.class));
        }
        super.put(ALL_TASKS, JSON.toJSONString(allTaskList), -1);
    }

    public List<Task> findByFacilityDomainAndUserId(int userId, Facility facility) {
        List<Task> taskList = JSON.parseObject(get(ALL_TASKS), new TypeReference<List<Task>>() {});
        List<Task> taskListReturn = Lists.newArrayList();
        Task task;
        for (int t = 0, len = taskList.size(); t<len; t++) {
            task = taskList.get(t);
            if (task.getFacilityDomain().equals(facility==null?"":facility.getDomain()) && (task.getInspectBy()==0 ||
                    task.getInspectBy()==userId || task.getReleaseBy()==userId)) {
                task.setFacilityName(facility.getName());
                taskListReturn.add(task);
            }
        }
        return taskListReturn;
    }

    public List<Task> findByFacilityDomainAndUserIdAndType(int userId, Facility facility, int type) {
        List<Task> taskList = JSON.parseObject(get(ALL_TASKS), new TypeReference<List<Task>>() {});
        List<Task> taskListReturn = Lists.newArrayList();
        Task task;
        for (int t = 0, len = taskList.size(); t<len; t++) {
            task = taskList.get(t);
            if (task.getFacilityDomain().equals(facility==null?"":facility.getDomain()) && task.getType()==type && (task.getInspectBy()==0 ||
                    task.getInspectBy()==userId || task.getReleaseBy()==userId)) {
                task.setFacilityName(facility.getName());
                taskListReturn.add(task);
            }
        }
        return taskListReturn;
    }

    public void putAllTasks(List<Task> taskList) {
        if (!CollectionUtils.isEmpty(taskList)) {
            Map<String, String> map = Maps.newHashMap();
            for (Task task: taskList) {
                map.put(String.valueOf(task.getId()), JSON.toJSONString(task));
            }
            //多保存一个名为ALL_TASKS的字段，其内容是所有数据的序列化
            map.put(ALL_TASKS, JSON.toJSONString(taskList));
            this.putAll(map, -1);
        }
    }

    public void init() {
        //初始化，将所有未完成的Task存入缓存中
        this.empty();
        List<Task> taskList = taskDao.findNoFinishList();
        putAllTasks(taskList);
    }

}
