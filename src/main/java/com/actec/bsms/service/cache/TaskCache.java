package com.actec.bsms.service.cache;

import com.actec.bsms.entity.Task;
import com.actec.bsms.repository.dao.TaskDao;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<Task> findByFacilityDomainAndUserId(int userId, String facilityDomain, int type) {
        List<Task> taskList = this.getAll();
        List<Task> taskListReturn = Lists.newArrayList();
        for (int t=0;t<taskList.size();t++) {
            Task task = taskList.get(t);
            if (task.getFacilityDomain().equals(facilityDomain) && task.getType()==type && (task.getInspectBy()==0 ||
                    task.getInspectBy()==userId || task.getReleaseBy()==userId)) {
                taskListReturn.add(task);
            }
        }
        return taskListReturn;
    }

    public void init() {
        //初始化，将所有未完成的Task存入缓存中
        this.empty();
        List<Task> taskList = taskDao.findNoFinishList();
        if (!CollectionUtils.isEmpty(taskList)) {
            for (Task task: taskList) {
                this.put(String.valueOf(task.getId()), task, -1);
            }
        }
    }

}
