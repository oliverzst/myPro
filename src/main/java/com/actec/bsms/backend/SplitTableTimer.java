package com.actec.bsms.backend;

import com.actec.bsms.service.InspectService;
import com.actec.bsms.service.TaskService;
import com.actec.bsms.service.TroubleShootService;
import com.actec.bsms.service.WatchService;
import com.actec.bsms.utils.ApplicationContextHelper;
import com.actec.bsms.utils.TableCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.TimerTask;

/**
 * 任务定时器
 *
 * @author zhangst
 * @create 2017-11-30 3:43 PM
 */

public class SplitTableTimer extends TimerTask {

    private static Logger logger = LoggerFactory.getLogger(SplitTableTimer.class);

    private static TaskService taskService = ApplicationContextHelper.getBean(TaskService.class);
    private static InspectService inspectService = ApplicationContextHelper.getBean(InspectService.class);
    private static WatchService watchService = ApplicationContextHelper.getBean(WatchService.class);
    private static TroubleShootService troubleShootService = ApplicationContextHelper.getBean(TroubleShootService.class);

    private SplitTableTimer() {}

    private static SplitTableTimer instance=null;
    public static synchronized SplitTableTimer getInstance() {
        if(instance==null){
            instance=new SplitTableTimer();
        }
        return instance;
    }

    /**
     * The action to be performed by this timer task.
     */
    @Override
    public void run() {
        Calendar now = Calendar.getInstance();
        logger.info(now.getTime().toString());
        /**判断每个月1号，生成上个月的月表，并新建task表*/
        if (now.get(Calendar.DAY_OF_MONTH)==1) {
            try {
                int year = now.get(Calendar.YEAR);
                int month = now.get(Calendar.MONTH);
                if (month==0) { //上一个月应该是前一年的12月
                    year = year - 1;
                    month = 12;
                }
                String tableName = "task_"+String.valueOf(year)+"_"+String.valueOf(month);
                if (!TableCache.tableExisted(tableName)) {
                    taskService.createMonthTable(year, month);
                }

                tableName = "inspect_"+String.valueOf(year)+"_"+String.valueOf(month);
                if (!TableCache.tableExisted(tableName)) {
                    inspectService.createMonthTable(year, month);
                }

                tableName = "trouble_shoot_"+String.valueOf(year)+"_"+String.valueOf(month);
                if (!TableCache.tableExisted(tableName)) {
                    troubleShootService.createMonthTable(year, month);
                }

                tableName = "watch_"+String.valueOf(year)+"_"+String.valueOf(month);
                if (!TableCache.tableExisted(tableName)) {
                    watchService.createMonthTable(year, month);
                }

            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }
}
