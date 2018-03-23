package com.actec.bsms.service;

import com.actec.bsms.entity.Facility;
import com.actec.bsms.entity.Inspect;
import com.actec.bsms.entity.User;
import com.actec.bsms.entity.Watch;
import com.actec.bsms.vo.statistics.CountByUser;
import com.actec.bsms.vo.statistics.CountType;
import com.actec.bsms.vo.statistics.EveryDayOrMonth;
import com.actec.bsms.repository.dao.FacilityDao;
import com.actec.bsms.repository.dao.FacilityGroupDao;
import com.actec.bsms.repository.dao.UserDao;
import com.actec.bsms.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 统计相关
 *
 * @author zhangst
 * @create 2017-12-21 10:11 AM
 */
@Service
public class StatisticsService {

    @Autowired
    FacilityGroupDao facilityGroupDao;
    @Autowired
    FacilityDao facilityDao;
    @Autowired
    UserDao userDao;

    public String statisticsByUser(List<Inspect> inspectList, List<Watch> watchList, String yearString, String monthString, int userId) throws ParseException {
        Calendar now = Calendar.getInstance();
        if (StringUtils.isEmpty(yearString)) {
            yearString = String.valueOf(now.get(Calendar.YEAR));
        }
        if (StringUtils.isEmpty(monthString)) {
            monthString = "12,11,10,9,8,7,6,5,4,3,2,1";
        }
        //是否指定用户，若是，则对该用户进行统计
        if (userId!=0) {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String[] years = yearString.split(",");
            String[] monthy = monthString.split(";");
            CountByUser countByUser = new CountByUser();
            int totalCountSelf =0;
            int totalCountTask =0;
            int totalCountDuty =0;
            int totalCountRepair =0;
            long totalTimeSelf=0;
            long totalTimeTask=0;
            long totalTimeDuty=0;
            long totalTimeRepair=0;
            List<EveryDayOrMonth> everyDayList = Lists.newArrayList();
            for (int y=0;y<years.length;y++) {
                String year = years[y];
                String[] months = monthy[y].split(",");
                //只查询一个月时，需要按天统计
                if (months.length==1 && years.length==1) {
                    String month = months[0];
                    Calendar cl=Calendar.getInstance();//实例化一个日历对象
                    cl.set(Calendar.YEAR, Integer.parseInt(year));//年设置
                    cl.set(Calendar.MONTH, Integer.parseInt(month)-1);//月设置
                    int day = cl.getActualMaximum(Calendar.DAY_OF_MONTH);
                    for (int i=1;i<=day;i++) {
                        String startTime = year+"-"+month+"-"+i+" 00:00:00";
                        String endTime = year+"-"+month+"-"+i+" 23:59:59";
                        Date startDate = sdf.parse(startTime);
                        Date endDate = sdf.parse(endTime);
                        int countSelf=0;
                        int countTask=0;
                        int countDuty=0;
                        int countRepair=0;
                        long timeSelf=0;
                        long timeTask=0;
                        long timeDuty=0;
                        long timeRepair=0;
                        for (int j=0;j<inspectList.size();j++) {
                            Inspect inspect = inspectList.get(j);
                            if (inspect.getEndTime().before(endDate) && inspect.getEndTime().after(startDate)) {
                                if (inspect.getInspectType()==Inspect.SELF) {
                                    countSelf++;
                                    timeSelf+=(int) ((inspect.getEndTime().getTime()-inspect.getInspectTime().getTime())/1000);
                                } else if (inspect.getInspectType()==Inspect.TASK) {
                                    countTask++;
                                    timeTask+=(int) ((inspect.getEndTime().getTime()-inspect.getInspectTime().getTime())/1000);
                                } else if (inspect.getInspectType()==Inspect.REPAIR) {
                                    countRepair++;
                                    timeRepair+=(int) ((inspect.getEndTime().getTime()-inspect.getInspectTime().getTime())/1000);
                                }
                            }
                        }
                        for (int w=0;w<watchList.size();w++) {
                            Watch watch = watchList.get(w);
                            if (watch.getEndTime().before(endDate) && watch.getEndTime().after(startDate)) {
                                countDuty++;
                                timeDuty+=(int) ((watch.getEndTime().getTime()-watch.getWatchTime().getTime())/1000);
                            }
                        }
                        EveryDayOrMonth everyDay = new EveryDayOrMonth();
                        everyDay.setDateKey(year+"-"+month+"-"+i);
                        List<CountType> countTypeList = Lists.newArrayList();
                        if (countSelf!=0) {
                            countTypeList.add(new CountType("自主巡检", countSelf, timeSelf/countSelf));
                        }
                        if (countTask!=0) {
                            countTypeList.add(new CountType("巡检任务", countTask, timeTask/countTask));
                        }
                        if (countRepair!=0) {
                            countTypeList.add(new CountType("抢修任务", countRepair, timeRepair/countRepair));
                        }
                        if (countDuty!=0) {
                            countTypeList.add(new CountType("值守任务", countDuty, timeDuty/countDuty));
                        }
                        everyDay.setCountList(countTypeList);
                        everyDayList.add(everyDay);
                        countByUser.setEvery(everyDayList);
                        totalCountSelf+=countSelf;
                        totalCountTask+=countTask;
                        totalCountRepair+=countRepair;
                        totalCountDuty+=countDuty;
                        totalTimeSelf+=timeSelf;
                        totalTimeTask+=timeTask;
                        totalTimeRepair+=timeRepair;
                        totalTimeDuty+=timeDuty;
                    }
                    //查询多个月时，按月统计
                } else {
                    for (int i=0;i<months.length;i++) {
                        int countSelf=0;
                        int countTask=0;
                        int countDuty=0;
                        int countRepair=0;
                        long timeSelf=0;
                        long timeTask=0;
                        long timeDuty=0;
                        long timeRepair=0;
                        Calendar cl=Calendar.getInstance();//实例化一个日历对象
                        cl.set(Calendar.YEAR, Integer.parseInt(year));//年设置
                        cl.set(Calendar.MONTH, Integer.parseInt(months[i])-1);//月设置
                        int day = cl.getActualMaximum(Calendar.DAY_OF_MONTH);
                        String startTime = year+"-"+months[i]+"-"+1+" 00:00:00";
                        String endTime = year+"-"+months[i]+"-"+day+" 23:59:59";
                        Date startDate = sdf.parse(startTime);
                        Date endDate = sdf.parse(endTime);
                        for (int j=0;j<inspectList.size();j++) {
                            Inspect inspect = inspectList.get(j);
                            if (inspect.getEndTime().before(endDate) && inspect.getEndTime().after(startDate)) {
                                if (inspect.getInspectType()==Inspect.SELF) {
                                    countSelf++;
                                    timeSelf+=(int) ((inspect.getEndTime().getTime()-inspect.getInspectTime().getTime())/1000);
                                } else if (inspect.getInspectType()==Inspect.TASK) {
                                    countTask++;
                                    timeTask+=(int) ((inspect.getEndTime().getTime()-inspect.getInspectTime().getTime())/1000);
                                } else if (inspect.getInspectType()==Inspect.REPAIR) {
                                    countRepair++;
                                    timeRepair+=(int) ((inspect.getEndTime().getTime()-inspect.getInspectTime().getTime())/1000);
                                }
                            }
                        }
                        for (int w=0;w<watchList.size();w++) {
                            Watch watch = watchList.get(w);
                            if (watch.getEndTime().before(endDate) && watch.getEndTime().after(startDate)) {
                                countDuty++;
                                timeDuty+=(int) ((watch.getEndTime().getTime()-watch.getWatchTime().getTime())/1000);
                            }
                        }
                        EveryDayOrMonth everyDay = new EveryDayOrMonth();
                        everyDay.setDateKey(year+"-"+months[i]);
                        List<CountType> countTypeList = Lists.newArrayList();
                        if (countSelf!=0) {
                            countTypeList.add(new CountType("自主巡检", countSelf, timeSelf/countSelf));
                        }
                        if (countTask!=0) {
                            countTypeList.add(new CountType("巡检任务", countTask, timeTask/countTask));
                        }
                        if (countRepair!=0) {
                            countTypeList.add(new CountType("抢修任务", countRepair, timeRepair/countRepair));
                        }
                        if (countDuty!=0) {
                            countTypeList.add(new CountType("值守任务", countDuty, timeDuty/countDuty));
                        }
                        everyDay.setCountList(countTypeList);
                        everyDayList.add(everyDay);
                        countByUser.setEvery(everyDayList);
                        totalCountSelf+=countSelf;
                        totalCountTask+=countTask;
                        totalCountRepair+=countRepair;
                        totalCountDuty+=countDuty;
                        totalTimeSelf+=timeSelf;
                        totalTimeTask+=timeTask;
                        totalTimeRepair+=timeRepair;
                        totalTimeDuty+=timeDuty;
                    }
                }
            }
            //按任务类型统计
            List<CountType> countTypeList = Lists.newArrayList();
            if (totalCountSelf!=0) {
                countTypeList.add(new CountType("自主巡检", totalCountSelf, totalTimeSelf/totalCountSelf));
            }
            if (totalCountTask!=0) {
                countTypeList.add(new CountType("巡检任务", totalCountTask, totalTimeTask/totalCountTask));
            }
            if (totalCountRepair!=0) {
                countTypeList.add(new CountType("抢修任务", totalCountRepair, totalTimeRepair/totalCountRepair));
            }
            if (totalCountDuty!=0) {
                countTypeList.add(new CountType("值守任务", totalCountDuty, totalTimeDuty/totalCountDuty));
            }
            countByUser.setTotalType(countTypeList);
            //按设备统计
            List<CountType> countDeviceList = Lists.newArrayList();
            Map<String, String> facilityDomainMap = getHistoryFacilityDomain(inspectList);
            Set<String> keys=facilityDomainMap.keySet();
            Iterator<String> iterator=keys.iterator();
            while (iterator.hasNext()){
                String domain = iterator.next();
                String name = facilityDomainMap.get(domain);
                int count =0;
                int time =0;
                for (int i=0;i<inspectList.size();i++) {
                    Inspect inspect = inspectList.get(i);
                    if (inspect.getFacilityDomain().equals(domain)) {
                        count++;
                        time+=(int) ((inspect.getEndTime().getTime()-inspect.getInspectTime().getTime())/1000);
                    }
                }
                if (count!=0) {
                    countDeviceList.add(new CountType(name, count, time/count));
                }
            }
            countByUser.setTotalDevice(countDeviceList);
            return JSON.toJSONString(countByUser);
        } else { //未指定用户，则统计所有用户
            List<User> userList = userDao.findAllList(new User());
            List<CountType> countUserList = Lists.newArrayList();
            for (int i=0;i<userList.size();i++) {
                User user = userList.get(i);
                CountType countType = countByUser(user, inspectList, watchList);
                if (null!=countType) {
                    countUserList.add(countType);
                }
            }
            return JSON.toJSONString(countUserList);
        }
    }

    public Map<String, String> getHistoryFacilityDomain(List<Inspect> inspectList) {
        Map<String, String> facilityDomainMap = Maps.newHashMap();
        for (int i=0;i<inspectList.size();i++) {
            String domain = inspectList.get(i).getFacilityDomain();
            if (!facilityDomainMap.containsKey(domain)) {
                Facility facility = facilityDao.findByDomain(domain);
                String name = domain.split("\\.|@")[0] + "基站";
                if (null!=facility) {
                    name = facility.getName();
                }
                facilityDomainMap.put(domain, name);
            }
        }
        return facilityDomainMap;
    }

    public String statisticsByDevice(List<Inspect> inspectList, String yearString, String monthString,
                                     String facilityDomain) throws ParseException {
        Calendar now = Calendar.getInstance();
        if (StringUtils.isEmpty(yearString)) {
            yearString = String.valueOf(now.get(Calendar.YEAR));
        }
        if (StringUtils.isEmpty(monthString)) {
            monthString = "12,11,10,9,8,7,6,5,4,3,2,1";
        }
        if (!StringUtils.isEmpty(facilityDomain)) { //指定基站
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String[] years = yearString.split(",");
            String[] monthy = monthString.split(";");
            CountByUser countByUser = new CountByUser();
            int totalCountSelf =0;
            int totalCountTask =0;
            int totalCountRepair =0;
            long totalTimeSelf=0;
            long totalTimeTask=0;
            long totalTimeRepair=0;
            List<EveryDayOrMonth> everyDayList = Lists.newArrayList();
            for (int y=0;y<years.length;y++) {
                String year = years[y];
                String[] months = monthy[y].split(",");
                if (months.length==1 && years.length==1) { //只查询一个月时，需要按天统计
                    String month = months[0];
                    Calendar cl=Calendar.getInstance();//实例化一个日历对象
                    cl.set(Calendar.YEAR, Integer.parseInt(year));//年设置
                    cl.set(Calendar.MONTH, Integer.parseInt(month)-1);//月设置
                    int day = cl.getActualMaximum(Calendar.DAY_OF_MONTH);
                    for (int i=1;i<=day;i++) {
                        String startTime = year+"-"+month+"-"+i+" 00:00:00";
                        String endTime = year+"-"+month+"-"+i+" 23:59:59";
                        Date startDate = sdf.parse(startTime);
                        Date endDate = sdf.parse(endTime);
                        EveryDayOrMonth everyDay = new EveryDayOrMonth();
                        everyDay.setDateKey(year+"-"+month+"-"+i);
                        List<User> userList = userDao.findAllList(new User());
                        List<CountType> countTypeList = Lists.newArrayList();
                        for (int u=0;u<userList.size();u++) {
                            int count=0;
                            long time=0;
                            User user = userList.get(u);
                            for (int j=0;j<inspectList.size();j++) {
                                Inspect inspect = inspectList.get(j);
                                if (inspect.getEndTime().before(endDate) && inspect.getEndTime().after(startDate) && inspect.getInspectBy()==user.getId()) {
                                    count++;
                                    time+=(int) ((inspect.getEndTime().getTime()-inspect.getInspectTime().getTime())/1000);
                                }
                            }
                            if (count!=0) {
                                countTypeList.add(new CountType(user.getName(), count, time/count));
                            }
                        }
                        everyDay.setCountList(countTypeList);
                        everyDayList.add(everyDay);
                        countByUser.setEvery(everyDayList);
                    }
                } else { //查询多个月时，按月统计
                    for (int i=0;i<months.length;i++) {
                        Calendar cl=Calendar.getInstance();//实例化一个日历对象
                        cl.set(Calendar.YEAR, Integer.parseInt(year));//年设置
                        cl.set(Calendar.MONTH, Integer.parseInt(months[i])-1);//月设置
                        int day = cl.getActualMaximum(Calendar.DAY_OF_MONTH);
                        String startTime = year+"-"+months[i]+"-"+1+" 00:00:00";
                        String endTime = year+"-"+months[i]+"-"+day+" 23:59:59";
                        Date startDate = sdf.parse(startTime);
                        Date endDate = sdf.parse(endTime);
                        EveryDayOrMonth everyDay = new EveryDayOrMonth();
                        everyDay.setDateKey(year+"-"+months[i]);
                        List<CountType> countTypeList = Lists.newArrayList();
                        List<User> userList = userDao.findAllList(new User());
                        for (int u=0;u<userList.size();u++) {
                            int count=0;
                            long time=0;
                            User user = userList.get(u);
                            for (int j=0;j<inspectList.size();j++) {
                                Inspect inspect = inspectList.get(j);
                                if (inspect.getEndTime().before(endDate) && inspect.getEndTime().after(startDate) && inspect.getInspectBy()==user.getId()) {
                                    count++;
                                    time+=(int) ((inspect.getEndTime().getTime()-inspect.getInspectTime().getTime())/1000);
                                }
                            }
                            if (count!=0) {
                                countTypeList.add(new CountType(user.getName(), count, time/count));
                            }
                        }
                        everyDay.setCountList(countTypeList);
                        everyDayList.add(everyDay);
                        countByUser.setEvery(everyDayList);
                    }
                }
            }
            for (int j=0;j<inspectList.size();j++) {
                Inspect inspect = inspectList.get(j);
                if (inspect.getInspectType()==Inspect.SELF) {
                    totalCountSelf++;
                    totalTimeSelf+=(int) ((inspect.getEndTime().getTime()-inspect.getInspectTime().getTime())/1000);
                } else if (inspect.getInspectType()==Inspect.TASK) {
                    totalCountTask++;
                    totalTimeTask+=(int) ((inspect.getEndTime().getTime()-inspect.getInspectTime().getTime())/1000);
                } else if (inspect.getInspectType()==Inspect.REPAIR) {
                    totalCountRepair++;
                    totalTimeRepair+=(int) ((inspect.getEndTime().getTime()-inspect.getInspectTime().getTime())/1000);
                }
            }
            //按任务类型统计
            List<CountType> countTypeList = Lists.newArrayList();
            if (totalCountSelf!=0) {
                countTypeList.add(new CountType("自主巡检", totalCountSelf, totalTimeSelf/totalCountSelf));
            }
            if (totalCountTask!=0) {
                countTypeList.add(new CountType("巡检任务", totalCountTask, totalTimeTask/totalCountTask));
            }
            if (totalCountRepair!=0) {
                countTypeList.add(new CountType("抢修任务", totalCountRepair, totalTimeRepair/totalCountRepair));
            }
            countByUser.setTotalType(countTypeList);

            //按用户统计
            List<CountType> countUserList = Lists.newArrayList();
            List<User> userList = userDao.findAllList(new User());
            for (int u=0;u<userList.size();u++) {
                User user = userList.get(u);
                CountType countType = countByUser(user, inspectList, null);
                if (null!=countType) {
                    countUserList.add(countType);
                }
            }
            countByUser.setTotalDevice(countUserList);
            return JSON.toJSONString(countByUser);
        } else { //未指定设备，统计所有设备
            List<CountType> countUserList = Lists.newArrayList();
            Map<String, String> facilityDomainMap = getHistoryFacilityDomain(inspectList);
            Set<String> keys=facilityDomainMap.keySet();
            Iterator<String> iterator=keys.iterator();
            while (iterator.hasNext()){
                String domain = iterator.next();
                String name = facilityDomainMap.get(domain);
                int count =0;
                int time =0;
                for (int j=0;j<inspectList.size();j++) {
                    Inspect inspect = inspectList.get(j);
                    if (inspect.getFacilityDomain().equals(domain)) {
                        count++;
                        time+=(int) ((inspect.getEndTime().getTime()-inspect.getInspectTime().getTime())/1000);
                    }
                }
                if (count!=0) {
                    countUserList.add(new CountType(name, count, time/count));
                }
            }
            return JSON.toJSONString(countUserList);
        }

    }

    private CountType countByUser(User user, List<Inspect> inspectList, List<Watch> watchList) {
        int count =0;
        int time =0;
        if (null!=inspectList) {
            for (int j=0;j<inspectList.size();j++) {
                Inspect inspect = inspectList.get(j);
                if (inspect.getInspectBy()==user.getId()) {
                    count++;
                    time+=(int) ((inspect.getEndTime().getTime()-inspect.getInspectTime().getTime())/1000);
                }
            }
        }
        if (null!=watchList) {
            for (int j=0;j<watchList.size();j++) {
                Watch watch = watchList.get(j);
                if (watch.getWatchBy()==user.getId()) {
                    count++;
                    time+=(int) ((watch.getEndTime().getTime()-watch.getWatchTime().getTime())/1000);
                }
            }
        }
        if (count==0) {
            return null;
        }
        return new CountType(user.getName(), count, time/count);
    }

}
