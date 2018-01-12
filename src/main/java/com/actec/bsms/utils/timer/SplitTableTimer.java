package com.actec.bsms.utils.timer;

import com.actec.bsms.repository.dao.InspectDao;
import com.actec.bsms.repository.dao.TaskDao;
import com.actec.bsms.repository.dao.TroubleShootDao;
import com.actec.bsms.repository.dao.WatchDao;
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

    private static TaskDao taskDao = ApplicationContextHelper.getBean(TaskDao.class);
    private static InspectDao inspectDao = ApplicationContextHelper.getBean(InspectDao.class);
    private static WatchDao watchDao = ApplicationContextHelper.getBean(WatchDao.class);
    private static TroubleShootDao troubleShootDao = ApplicationContextHelper.getBean(TroubleShootDao.class);

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
                    //新建月表
                    taskDao.createMonthTable(year, month);
                    //新建表
                    taskDao.createTable();
                    //将未完成任务迁移到task表
                    taskDao.updateTaskTable(year, month);
                    taskDao.deleteMonthTable(year, month);
                }

                tableName = "inspect_"+String.valueOf(year)+"_"+String.valueOf(month);
                if (!TableCache.tableExisted(tableName)) {
                    //新建月表
                    inspectDao.createMonthTable(year, month);
                    //新建表
                    inspectDao.createTable();
                    //将未完成任务迁移到task表
                    inspectDao.updateInspectTable(year, month);
                    inspectDao.deleteMonthTable(year, month);
                }

                tableName = "trouble_shoot_"+String.valueOf(year)+"_"+String.valueOf(month);
                if (!TableCache.tableExisted(tableName)) {
                    //新建月表
                    troubleShootDao.createMonthTable(year, month);
                    //新建表
                    troubleShootDao.createTable();
                }

                tableName = "watch_"+String.valueOf(year)+"_"+String.valueOf(month);
                if (!TableCache.tableExisted(tableName)) {
                    //新建月表
                    watchDao.createMonthTable(year, month);
                    //新建表
                    watchDao.createTable();
                    //将未完成值守任务迁移到新watch表
                    watchDao.updateInspectTable(year, month);
                    watchDao.deleteMonthTable(year, month);
                }

            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }
}
