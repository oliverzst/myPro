package com.actec.bsms.service;

import com.actec.bsms.entity.Facility;
import com.actec.bsms.entity.Inspect;
import com.actec.bsms.entity.Task;
import com.actec.bsms.entity.Watch;
import com.actec.bsms.repository.dao.InspectDao;
import com.actec.bsms.repository.dao.TableDao;
import com.actec.bsms.repository.dao.TaskDao;
import com.actec.bsms.utils.DateUtils;
import com.actec.bsms.utils.StringUtils;
import com.actec.bsms.utils.TableCache;
import com.actec.bsms.common.excel.ExportExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * 巡检接口类
 *
 * @author zhangst
 * @create 2017-12-8 11:08 PM
 */
@Service
public class InspectService {

    @Autowired
    InspectDao inspectDao;
    @Autowired
    TaskDao taskDao;
    @Autowired
    TableDao tableDao;

    private static String tableName = "inspect";

    public Inspect get(int id){
        return inspectDao.get(id);
    }

    public void save(Inspect inspect) {
        if (null!=inspect) {
            if (inspect.getId()==0) {
                inspectDao.insert(inspect);
            } else {
                inspectDao.update(inspect);
            }
        }
    }

    public void delete(Inspect task) {
        if (null!=task) {
            inspectDao.delete(task);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
    public void createMonthTable(int year, int month) {
        //将当前task表另存为上月task月表
        tableDao.createMonthTable(tableName, year, month);
        //新建一张task表
        inspectDao.createTable();
        //将未完成任务迁移到新task表
        tableDao.updateMonthTable(tableName, year, month);
        tableDao.deleteMonthTable(tableName, year, month);
    }

    public List<Inspect> findAllHistory() {
        List<Inspect> inspectList = Lists.newArrayList();
        List<String> tableNames = findTables();
        for (String tableName: tableNames) {
            inspectList.addAll(inspectDao.findHistoryByTableName(tableName));
        }
        return inspectList;
    }

    public Inspect findById(int id) {
        return inspectDao.findById(id);
    }

    public Inspect findByIdByTableName(int id) {
        List<String> tableNames = findTables();
        for (int i=tableNames.size();i>0;i--) {
            Inspect inspect = inspectDao.findByIdByTableName(id, tableNames.get(i-1));
            if (null!=inspect) {
                return inspect;
            }
        }
        return null;
    }

    public List<Inspect> findHistoryInspectByFacilityDomain(String facilityDomain) {
        List<Inspect> inspectList = inspectDao.findHistoryInspectByFacilityDomain(facilityDomain);
        return addTaskList(inspectList);
    }

    public List<Inspect> findHistoryInspectByFacilityDomainByMonth(int year, int month, String facilityDomain) {
        List<Inspect> inspectList = inspectDao.findHistoryInspectByFacilityDomainByMonth(year, month, facilityDomain);
        return addTaskList(inspectList);
    }

    public List<Inspect> findHistoryInspectByUserId(int userId) {
        List<Inspect> inspectList = inspectDao.findByInspectByEnd(userId);
        return addTaskList(inspectList);
    }

    public List<Inspect> findHistoryInspectByUserIdByMonth(int year, int month, int userId) {
        List<Inspect> inspectList = inspectDao.findByInspectByMonth(year, month, userId);
        return addTaskList(inspectList);
    }

    public List<Inspect> findByFacilityAndUser(String facilityDomain, int userId) {
        if (0!=inspectDao.findByFacilityAndUser(facilityDomain, userId, Inspect.TASK).size()) {
            return inspectDao.findByFacilityAndUser(facilityDomain, userId, Inspect.TASK);
        } else if ((0!=inspectDao.findByFacilityAndUser(facilityDomain, userId, Inspect.TASK).size())){
            return inspectDao.findByFacilityAndUser(facilityDomain, userId, Inspect.SELF);
        } else {
            return inspectDao.findByFacilityAndUser(facilityDomain, userId, Inspect.REPAIR);
        }
    }

    public Inspect findByFacilityAndUserAndInspectDeviceType(String facilityDomain, int userId, int inspectDeviceType) {
        if (null!=inspectDao.findByFacilityAndUserAndInspectDeviceType(facilityDomain, userId, inspectDeviceType,Inspect.TASK)) {
            return inspectDao.findByFacilityAndUserAndInspectDeviceType(facilityDomain, userId, inspectDeviceType,Inspect.TASK);
        } else if (null!=inspectDao.findByFacilityAndUserAndInspectDeviceType(facilityDomain, userId, inspectDeviceType,Inspect.SELF)){
            return inspectDao.findByFacilityAndUserAndInspectDeviceType(facilityDomain, userId, inspectDeviceType,Inspect.SELF);
        } else {
            return inspectDao.findByFacilityAndUserAndInspectDeviceType(facilityDomain, userId, inspectDeviceType,Inspect.REPAIR);
        }
    }

    public void excuteInspect(Inspect inspect, String facilityDomain, int userId, int inspectType, int inspectDeviceType) {
        inspect.setFacilityDomain(facilityDomain);
        inspect.setInspectTime(DateUtils.getNowDate());
        inspect.setInspectBy(userId);
        inspect.setInspectDeviceTypeId(inspectDeviceType);
        inspect.setInspectType(inspectType);
        inspect.setState(Inspect.EXECUTE);
        save(inspect);
    }

    public void finishInspect(Inspect inspect, String records, String troubleShootInfo) {
        inspect.setInspectRecords(records);
        if (!StringUtils.isEmpty(troubleShootInfo)) {
            inspect.setTroubleShootInfo(troubleShootInfo);
        }
        inspect.setState(Inspect.FINISH);
        inspect.setEndTime(DateUtils.getNowDate());
        save(inspect);
    }

    public List<Inspect> getInspectHistoryByFacility(String yearString, String monthString, String facilityDomain) {
        List<Inspect> historyInspectList = Lists.newArrayList();
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
                    historyInspectList.addAll(findHistoryInspectByFacilityDomain(facilityDomain));
                } else {
                    String tableName = "inspect_"+years[i]+"_"+months[j];
                    if (TableCache.tableExisted(tableName)) {
                        historyInspectList.addAll(findHistoryInspectByFacilityDomainByMonth(Integer.parseInt(years[i]),Integer.parseInt(months[j]),facilityDomain));
                    }
                }
            }
        }
        return historyInspectList;
    }

    public List<Inspect> getInspectHistoryByUser(String yearString, String monthString, int userId) {
        List<Inspect> historyInspectList = Lists.newArrayList();
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
                    historyInspectList.addAll(findHistoryInspectByUserId(userId));
                } else {
                    String tableName = "inspect_"+years[i]+"_"+months[j];
                    if (TableCache.tableExisted(tableName)) {
                        historyInspectList.addAll(findHistoryInspectByUserIdByMonth(Integer.parseInt(years[i]),Integer.parseInt(months[j]),userId));
                    }
                }
            }
        }
        return historyInspectList;
    }

    private List<Inspect> addTaskList(List<Inspect> inspectList) {
        List<Inspect> inspectListReturn = Lists.newArrayList();
        for (int i=0;i<inspectList.size();i++) {
            Inspect inspect = inspectList.get(i);
            if (inspect.getFacility()==null) {
                if (inspect.getFacilityDomain()!=null) {
                    String name = inspect.getFacilityDomain().split("\\.|@")[0] + "基站";
                    inspect.setFacility(new Facility(name));
                } else {
                    inspect.setFacility(new Facility());
                }
            }
            String taskIds = inspect.getTaskIds();
            if (!StringUtils.isEmpty(taskIds)) {
                String[] taskIdList = taskIds.split(",");
                List<Task> taskList = Lists.newArrayList();
                for (int j=1;j<taskIdList.length;j++) {
                    taskList.add(taskDao.get(Integer.parseInt(taskIdList[j])));
                }
                inspect.setTaskList(taskList);
            }
            inspectListReturn.add(inspect);
        }
        return inspectListReturn;
    }

    private static String[] excelHeaders = {"用户名", "任务类型", "基站名", "开始时间","常规记录","故障排查记录","完成时间"};

    public void exportInspectExcel(List<Inspect> inspectList, List<Watch> watchList, String fileName, HttpServletResponse response) throws Exception {

        List<String> headersSel = new ArrayList<>();
        for (int i=0;i<excelHeaders.length;i++) {
            headersSel.add(excelHeaders[i]);
        }
        ExportExcel excel = new ExportExcel(null, headersSel);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (inspectList!=null) { //巡检
            for (int i=0;i<inspectList.size();i++) {
                Inspect inspect = inspectDao.get(inspectList.get(i).getId());
                Row row = excel.addRow();
                int k = 0;
                for (int j=0;j<excelHeaders.length;j++) {
                    String value = null;
                    switch(j) {
                        case 0: value = inspect.getInspectUser().getName();
                            break;
                        case 1: value = inspect.getInspectType()==Inspect.SELF?"自主巡检":inspect.getInspectType()==Inspect.TASK?"巡检任务":"抢修任务";
                            break;
                        case 2: value = inspect.getFacility()!=null?inspect.getFacility().getName():(inspect.getFacilityDomain().split("\\.|@")[0] + "基站");
                            break;
                        case 3: value = sdf.format(inspect.getInspectTime());
                            break;
                        case 4: value = inspect.getInspectRecords()!=null?getInspectRecords(inspect.getInspectRecords()):"";
                            break;
                        case 5: value = inspect.getTroubleShootInfo()!=null?getTroubleShootInfo(inspect.getTroubleShootInfo()):"";
                            break;
                        case 6: value = sdf.format(inspect.getEndTime());
                            break;
                    }
                    excel.addCell(row, k, value);
                    k++;
                }
            }
        }
        if (watchList!=null) {//值守
            for (int i=0;i<watchList.size();i++) {
                Row row = excel.addRow();
                int k = 0;
                for (int j=0;j<excelHeaders.length;j++) {
                    String value = null;
                    switch(j) {
                        case 0: value = watchList.get(i).getWatchUser().getName();
                            break;
                        case 1: value = "值守";
                            break;
                        case 2: value = watchList.get(i).getFacility()!=null?watchList.get(i).getFacility().getName():"";
                            break;
                        case 3: value = sdf.format(watchList.get(i).getWatchTime());
                            break;
                        case 4: value = getWatchRecords(watchList.get(i).getWatchRecords());
                            break;
                        case 5: value = "";
                            break;
                        case 6: value = sdf.format(watchList.get(i).getEndTime());
                            break;
                    }
                    excel.addCell(row, k, value);
                    k++;
                }
            }
        }
        excel.write(response, fileName).dispose();
    }

    private static String getInspectRecords(String inspectRecords) throws Exception {
        String inspectRecord = "---常规巡检---\n";
        List<Map<String, String>> inspectRecordsMap = JSON.parseObject(inspectRecords, new TypeReference<List<Map<String,String>>>(){});
        String remarks;
        String result;
        for (int i=0;i<inspectRecordsMap.size();i++) {
            String name = inspectRecordsMap.get(i).get("name");
            if (inspectRecordsMap.get(i).get("inspectResult")!=null) {
                Map<String, String> inspectResultMap = JSON.parseObject(inspectRecordsMap.get(i).get("inspectResult"), new TypeReference<Map<String,String>>(){});
                if (inspectResultMap.get("result").equals("false")) {
                    result = "故障";
                } else {
                    result = "正常";
                }
                inspectRecord += name+"："+result;
                if (inspectResultMap.get("remarks")!=null) {
                    remarks = inspectResultMap.get("remarks");
                    inspectRecord += "，"+remarks;
                }
                inspectRecord += "\n";
            }  else if (inspectRecordsMap.get(i).get("comment")!=null) {
                remarks = inspectRecordsMap.get(i).get("comment");
                inspectRecord += name+"："+remarks+"\n";
            }
        }
        return inspectRecord;
    }

    private static String getTroubleShootInfo(String troubleShootInfo) throws Exception {
        String troubleShootResult = "";
        List<Map<String, String>> troubleShootMap = JSON.parseObject(troubleShootInfo, new TypeReference<List<Map<String,String>>>(){});
        for (int i=0;i<troubleShootMap.size();i++) {
            String isResolve = "未解决";
            if (troubleShootMap.get(i).get("moduleName").equals("1")) {
                isResolve = "已解决";
            }
            troubleShootResult +="\n"+"---故障"+(i+1)+"处理---"+"\n";
            troubleShootResult +="模块："+troubleShootMap.get(i).get("moduleName")+"\n";
            troubleShootResult +="序号："+troubleShootMap.get(i).get("number")+"\n";
            troubleShootResult +="故障现象："+troubleShootMap.get(i).get("description")+"\n";
            troubleShootResult +="处理过程："+troubleShootMap.get(i).get("process")+"\n";
            troubleShootResult +="解决情况："+isResolve+"\n";
        }
        return troubleShootResult;
    }

    private static String getWatchRecords(String watchRecords) throws Exception {
        String watchRecordResult = "";
        List<Map<String, String>> watchRecordMap = JSON.parseObject(watchRecords, new TypeReference<List<Map<String,String>>>(){});
        for (int i=0;i<watchRecordMap.size();i++) {
            watchRecordResult = "---值守记录---\n";
            watchRecordResult += "内容："+watchRecordMap.get(i).get("content")+"\n";
            watchRecordResult += "地点："+watchRecordMap.get(i).get("place")+"\n";
            if (watchRecordMap.get(i).get("records")!=null) {
                List<Map<String, String>> recordsMap = JSON.parseObject(watchRecordMap.get(i).get("records"), new TypeReference<List<Map<String,String>>>(){});
                for (int j=0;j<recordsMap.size();j++) {
                    watchRecordResult += "记录"+(j+1)+"："+recordsMap.get(i).get("content")+"\n";
                }
            }
        }
        return watchRecordResult;
    }

    public String getStartDate() {
        List<String> tableList = findTables();
        Calendar now = Calendar.getInstance();
        if (tableList.size()==1) {
            return now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1);
        } else {
            return tableList.get(0).substring(8).replaceAll("_","-");
        }
    }

    private List<String> findTables() {
        List<String> tables = new ArrayList<>();
        String tablePrefix = "inspect";
        int year = 2017;
        int month = 12;
        Calendar now = Calendar.getInstance();
        for (;year<=now.get(Calendar.YEAR);year++) {
            if (year!=now.get(Calendar.YEAR)) {
                for (;month<=12;month++) {
                    String tableName = tablePrefix + "_" + year + "_" + month;
                    if (TableCache.tableExisted(tableName)) {
                        tables.add(tableName);
                    }
                }
            } else {
                for (;month<=now.get(Calendar.MONTH);month++) {
                    String tableName = tablePrefix + "_" + year + "_" + month;
                    if (TableCache.tableExisted(tableName)) {
                        tables.add(tableName);
                    }
                }
            }
            if (month==13) {
                month = 1;
            }
        }
        tables.add(tablePrefix);
        return tables;
    }

}
