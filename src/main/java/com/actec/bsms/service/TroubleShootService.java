package com.actec.bsms.service;

import com.actec.bsms.entity.TroubleShoot;
import com.actec.bsms.repository.dao.TableDao;
import com.actec.bsms.repository.dao.TroubleShootDao;
import com.actec.bsms.utils.DateUtils;
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
 * 故障处理接口类
 *
 * @author zhangst
 * @create 2017-12-14 3:08 PM
 */
@Service
public class TroubleShootService {

    @Autowired
    TroubleShootDao troubleShootDao;
    @Autowired
    TableDao tableDao;

    private static String tableName = "trouble_shoot";

    public TroubleShoot get(int id){
        return troubleShootDao.get(id);
    }

    public void save(TroubleShoot troubleShoot) {
        if (null!=troubleShoot) {
            if (troubleShoot.getId()==0) {
                troubleShootDao.insert(troubleShoot);
            } else {
                troubleShootDao.update(troubleShoot);
            }
        }
    }

    public void delete(TroubleShoot troubleShoot) {
        if (null!=troubleShoot) {
            troubleShootDao.delete(troubleShoot);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
    public void createMonthTable(int year, int month) {
        //将当前task表另存为上月task月表
        tableDao.createMonthTable(tableName, year, month);
        //新建一张task表
        troubleShootDao.createTable();
    }

    public List<TroubleShoot> findTroubleShootList(int userId, int moduleId, int inspectDeviceType, String facilityDomain,
                                                   String year, String month) {
        List<TroubleShoot> historyTroubleShootList = Lists.newArrayList();
        Calendar now = Calendar.getInstance();
        if (year==null) {
            year = String.valueOf(now.get(Calendar.YEAR));
        }
        if (month==null) {
            month = "12,11,10,9,8,7,6,5,4,3,2,1";
        }
        String[] years = year.split(",");
        String[] months = month.split(",");
        for (int i=0;i<years.length;i++) {
            for (int j=0;j<months.length;j++) {
                if (Integer.parseInt(months[j])==now.get(Calendar.MONTH)+1) {
                    historyTroubleShootList.addAll(troubleShootDao.findHistory(userId, moduleId, inspectDeviceType, facilityDomain));
                } else {
                    String tableName = "trouble_shoot_"+years[i]+"_"+months[j];
                    if (TableCache.tableExisted(tableName)) {
                        historyTroubleShootList.addAll(troubleShootDao.findHistoryByMonth(userId, moduleId, inspectDeviceType, facilityDomain, Integer.parseInt(years[i]),Integer.parseInt(months[j])));
                    }
                }
            }
        }
        return historyTroubleShootList;
    }

    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
    public void saveTroubleShoots(List<TroubleShoot> troubleShootList, String facilityDomain, int userId, int inspectDeviceType, int inspectId) {
        deleteByInspect(inspectId);
        for (int k=0;k<troubleShootList.size();k++) {
            TroubleShoot troubleShoot = troubleShootList.get(k);
            troubleShoot.setFacilityDomain(facilityDomain);
            troubleShoot.setUserId(userId);
            troubleShoot.setInspectDeviceTypeId(inspectDeviceType);
            troubleShoot.setEndTime(DateUtils.getNowDate());
            troubleShoot.setInspectId(inspectId);
            save(troubleShoot);
        }
    }

    private void deleteByInspect(int inspectId) {
        troubleShootDao.deleteByInspect(inspectId);
    }

}
