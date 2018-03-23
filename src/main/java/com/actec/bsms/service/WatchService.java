package com.actec.bsms.service;

import com.actec.bsms.entity.Facility;
import com.actec.bsms.entity.Watch;
import com.actec.bsms.repository.dao.TableDao;
import com.actec.bsms.repository.dao.WatchDao;
import com.actec.bsms.utils.DateUtils;
import com.actec.bsms.utils.StringUtils;
import com.actec.bsms.utils.TableCache;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;

/**
 * 值守任务接口类
 *
 * @author zhangst
 * @create 2017-12-8 11:08 PM
 */
@Service
public class WatchService {

    @Autowired
    WatchDao watchDao;
    @Autowired
    TableDao tableDao;

    private static String tableName = "watch";

    public Watch get(int id){
        return watchDao.get(id);
    }

    public void save(Watch watch) {
        if (null!=watch) {
            if (watch.getId()==0) {
                watchDao.insert(watch);
            } else {
                watchDao.update(watch);
            }
        }
    }

    public void delete(Watch watch) {
        if (null!=watch) {
            watchDao.delete(watch);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
    public void createMonthTable(int year, int month) {
        //将当前task表另存为上月task月表
        tableDao.createMonthTable(tableName, year, month);
        //新建一张task表
        watchDao.createTable();
        //将未完成任务迁移到新task表
        tableDao.updateMonthTable(tableName, year, month);
        tableDao.deleteMonthTable(tableName, year, month);
    }


    public Watch findByTaskId(int taskId) {
        return watchDao.findByTaskId(taskId);
    }

    public List<Watch> findHistoryByUserId(int userId) {
        return watchDao.findHistoryByUserId(userId);
    }

    public List<Watch> findHistoryByUserIdAndMonth(int year, int month, int userId) {
        return watchDao.findHistoryByUserIdAndMonth(year, month, userId);
    }

    public List<Watch> findWatchList(String yearString, String monthString, int userId) {
        List<Watch> historyWatchList = Lists.newArrayList();
        Calendar now = Calendar.getInstance();
        if (StringUtils.isEmpty(yearString)) {
            yearString = String.valueOf(now.get(Calendar.YEAR));
        }
        if (StringUtils.isEmpty(monthString)) {
            monthString = "12,11,10,9,8,7,6,5,4,3,2,1";
        }
        String[] years = yearString.split(",");
        String[] monthy = monthString.split(";");
        for (int i=0;i<years.length;i++) {
            String[] months = monthy[i].split(",");
            for (int j=0;j<months.length;j++) {
                if ((Integer.parseInt(months[j])==now.get(Calendar.MONTH)+1) && (Integer.parseInt(years[i])==now.get(Calendar.YEAR))) {
                    historyWatchList.addAll(watchDao.findHistoryByUserId(userId));
                } else {
                    String tableName = "inspect_"+years[i]+"_"+months[j];
                    if (TableCache.tableExisted(tableName)) {
                        historyWatchList.addAll(watchDao.findHistoryByUserIdAndMonth(Integer.parseInt(years[i]),Integer.parseInt(months[j]),userId));
                    }
                }
            }
        }
        return doWatchs(historyWatchList);
    }

    public void finishWatch(int taskId, String records) {
        Watch watch = findByTaskId(taskId);
        watch.setWatchRecords(records);
        watch.setEndTime(DateUtils.getNowDate());
        watch.setState(Watch.FINISH);
        save(watch);
    }

    public void startWatch(int taskId, int userId) {
        Watch watch = findByTaskId(taskId);
        if (null==watch) {
            watch = new Watch();
        }
        watch.setState(Watch.EXECUTE);
        watch.setTaskId(taskId);
        watch.setWatchBy(userId);
        watch.setWatchTime(DateUtils.getNowDate());
        save(watch);
    }

    private List<Watch> doWatchs(List<Watch> watchList) {
        List<Watch> watchListReturn = Lists.newArrayList();
        for (int i=0;i<watchList.size();i++) {
            Watch watch = watchList.get(i);
            if (watch.getFacility()==null) {
                watch.setFacility(new Facility());
            }
            watchListReturn.add(watch);
        }
        return watchListReturn;
    }

}
