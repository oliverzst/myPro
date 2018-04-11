package com.actec.bsms.webscoket;

import com.actec.bsms.repository.socket.RealtimeAlarmSocket;
import com.actec.bsms.service.FacilityGroupService;
import com.actec.bsms.service.UserService;
import com.actec.bsms.utils.AlarmUtils;
import com.actec.bsms.utils.ApplicationContextHelper;
import com.actec.bsms.vo.Alarm;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * webscoket测试类
 *
 * @author zhangst
 * @create 2017-11-07 5:13 PM
 */
@ServerEndpoint("/websocket/AlarmHandler")
@Component
public class AlarmWebsocket extends BaseWebsocket {

    private static UserService userService = ApplicationContextHelper.getBean(UserService.class);
    private static FacilityGroupService facilityGroupService = ApplicationContextHelper.getBean(FacilityGroupService.class);

    private static final String failResult = "failure";

    @Override
    protected String getMessage(String message, Session session) {
        try {
            Map<String, String> map = JSON.parseObject(message, new TypeReference<Map<String,String>>(){});
            int userId = Integer.parseInt(map.get("userId"));
            int facilityGroupId = userService.get(userId, false).getFacilityGroupId();
            Map<String, String> facDomainMap = facilityGroupService.getFacDomains(facilityGroupId, false);
            List<Alarm> alarmList = AlarmUtils.alarmRealTimesToAlarms(RealtimeAlarmSocket.REALTIME_ALARM_LIST);
            List<Alarm> alarmReportList = Lists.newArrayList();
            Alarm alarm5 = new Alarm();
            alarm5.setAlarmId("1");
            alarm5.setRealName("基站69");
            alarm5.setDomainName("r69.pdt.cn");
            alarm5.setCodeName("断电");
            String time = "2017-11-27 10:10:10";
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            alarm5.setAlarmTimestamp(sdf.parse(time));
            alarm5.setLevelName("严重");
            alarm5.setDescription("告警描述");
            alarm5.setProposal("处理建议");
            alarmList.add(alarm5);
            Alarm alarm1 = new Alarm();
            alarm1.setAlarmId("2");
            alarm1.setRealName("基站16");
            alarm1.setDomainName("r16.pdt.cn");
            alarm1.setCodeName("故障");
            time = "2017-11-29 10:10:11";
            sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            alarm1.setAlarmTimestamp(sdf.parse(time));
            alarm1.setLevelName("一般");
            alarm1.setDescription("告警描述");
            alarm1.setProposal("处理建议");
            alarmList.add(alarm1);
            Alarm alarm2 = new Alarm();
            alarm2.setAlarmId("3");
            alarm2.setRealName("基站1013");
            alarm2.setDomainName("r1013.pdt.cn");
            alarm2.setCodeName("故障");
            time = "2017-11-28 10:10:12";
            sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            alarm2.setAlarmTimestamp(sdf.parse(time));
            alarm2.setLevelName("轻微");
            alarm2.setDescription("告警描述");
            alarm2.setProposal("处理建议");
            alarmList.add(alarm2);
            Alarm alarm3 = new Alarm();
            alarm3.setAlarmId("4");
            alarm3.setRealName("有线固定台");
            alarm3.setDomainName("srs22@pdt.cn");
            alarm3.setCodeName("故障1");
            time = "2017-11-29 10:10:12";
            sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            alarm3.setAlarmTimestamp(sdf.parse(time));
            alarm3.setLevelName("严重");
            alarm3.setDescription("告警描述");
            alarm3.setProposal("处理建议");
            alarmList.add(alarm3);
            Alarm alarm4 = new Alarm();
            alarm4.setAlarmId("5");
            alarm4.setRealName("基站170");
            alarm4.setDomainName("r170.pdt.cn");
            alarm4.setCodeName("故障");
            time = "2017-12-01 10:10:12";
            sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            alarm4.setAlarmTimestamp(sdf.parse(time));
            alarm4.setLevelName("严重");
            alarm4.setDescription("告警描述");
            alarm4.setProposal("处理建议");
            alarmList.add(alarm4);
            for (int i=0,len=alarmList.size();i<len;i++) {
                //根据设备域名检查告警数据,若满足,则为其添加实名;若不满足，则删除，不上报
                Alarm alarm = alarmList.get(i);
                if (facDomainMap.containsKey(alarm.getDomainName())) {
                    alarm.setRealName(facDomainMap.get(alarm.getDomainName()));
                    alarmReportList.add(alarm);
                }
            }
            return JSON.toJSONString(alarmReportList);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JSON.toJSONString(failResult);
        }
    }
    @Override
    @OnOpen
    public void start(Session session) {
        super.start(session);
    }

    @Override
    @OnClose
    public void close(Session session) {
        super.close(session);
    }

    @Override
    @OnMessage
    public void incoming(Session session, String request) {
        super.incoming(session, request);
    }

    @Override
    @OnError
    public void onError(Session session, Throwable t) throws Throwable {
        super.onError(session,t);
    }
}
