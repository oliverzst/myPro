package com.actec.bsms.service.cache;

import com.actec.bsms.entity.Facility;
import com.actec.bsms.entity.Task;
import com.actec.bsms.repository.dao.TaskDao;
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
public class TaskCache extends IRedisService<Task> {
    private static final String REDIS_KEY = "TASK_REDIS_KEY";

    @Autowired
    TaskDao taskDao;

    @Override
    protected String getRedisKey() {
        return this.REDIS_KEY;
    }

    public List<Task> findByFacilityDomainAndUserId(int userId, Facility facility, int type) {
        List<Task> taskList = getAll();
        List<Task> taskListReturn = Lists.newArrayList();
        Task task;
        for (int t=0,len=taskList.size();t<len;t++) {
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
            Map<String, Task> map = Maps.newHashMap();
            for (Task task: taskList) {
                map.put(String.valueOf(task.getId()), task);
            }
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
