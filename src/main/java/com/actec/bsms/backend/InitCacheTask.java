package com.actec.bsms.backend;

import com.actec.bsms.service.cache.FacilityGroupCache;
import com.actec.bsms.service.cache.TaskCache;
import com.actec.bsms.service.cache.UserCache;
import com.actec.bsms.utils.ApplicationContextHelper;

/**
 * 缓存初始化线程
 *
 * @author zhangst
 * @create 2017-11-27 3:54 PM
 */

public class InitCacheTask implements Runnable{

    private static TaskCache taskCache = ApplicationContextHelper.getBean(TaskCache.class);
    private static FacilityGroupCache facilityGroupCache = ApplicationContextHelper.getBean(FacilityGroupCache.class);
    private static UserCache userCache = ApplicationContextHelper.getBean(UserCache.class);

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        try {
            //清空redis
            facilityGroupCache.empty();
            userCache.empty();
            taskCache.empty();
            while (true) {
                if (facilityGroupCache.count()==0 && userCache.count()==0 && taskCache.count()==0) {
                    taskCache.init();
                    facilityGroupCache.init();
                    userCache.init();
                }
                Thread.sleep(600000L);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
