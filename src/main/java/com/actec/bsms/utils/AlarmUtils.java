package com.actec.bsms.utils;

import com.actec.bsms.entity.AlarmRealTime;
import com.actec.bsms.entity.Facility;
import com.actec.bsms.vo.Alarm;
import com.actec.bsms.service.FacilityService;
import com.google.common.collect.Lists;

import java.util.Date;
import java.util.List;

/**
 * 告警工具类
 *
 * @author zhangst
 * @create 2017-11-16 3:55 PM
 */

public class AlarmUtils {

    private static FacilityService facilityService = ApplicationContextHelper.getBean(FacilityService.class);

    public static Alarm alarmRealTimeToAlarm(AlarmRealTime alarmRealTime) {
        Alarm alarm = new Alarm();
        alarm.setAlarmId(alarmRealTime.getAlarmResultId()!=null?alarmRealTime.getAlarmResultId():"");
        alarm.setDomainName(alarmRealTime.getDomainName()!=null?alarmRealTime.getDomainName():"");
        alarm.setFullDomainName(alarmRealTime.getFullDomainName()!=null?alarmRealTime.getFullDomainName():"");
        Facility facility = facilityService.findByDomain(alarm.getDomainName());
        alarm.setRealName(facility!=null?facility.getName():"");
        alarm.setFullRealName(alarmRealTime.getFullRealName()!=null?alarmRealTime.getFullRealName():"");
        alarm.setCode(alarmRealTime.getCode());
        alarm.setCodeName(alarmRealTime.getCodeName()!=null?alarmRealTime.getCodeName():"");
        alarm.setLevel(alarmRealTime.getLevel());
        alarm.setLevelName(alarmRealTime.getLevelName()!=null?alarmRealTime.getLevelName():"");
        alarm.setDescription(alarmRealTime.getDescription()!=null?alarmRealTime.getDescription():"");
        alarm.setAlarmTimestamp(new Date((long)alarmRealTime.getAlarmTimestamp()*1000));
        //从知识库中获取告警解决建议 alarm.setProposal();
        return alarm;
    }

    public static List<Alarm> alarmRealTimesToAlarms(List<AlarmRealTime> alarmRealTimeList) {
        List<Alarm> alarmList = Lists.newArrayList();
        for (int i=0;i<alarmRealTimeList.size();i++) {
            alarmList.add(alarmRealTimeToAlarm(alarmRealTimeList.get(i)));
        }
        return alarmList;
    }

}
