package com.actec.bsms.webscoket;

import com.actec.bsms.entity.User;
import com.actec.bsms.entity.vo.Alarm;
import com.actec.bsms.repository.socket.RealtimeAlarmSocket;
import com.actec.bsms.service.FacilityGroupService;
import com.actec.bsms.service.UserService;
import com.actec.bsms.utils.ApplicationContextHelper;
import com.actec.bsms.utils.alarm.AlarmUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
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

    private static Logger logger = LoggerFactory.getLogger(BaseWebsocket.class);

    private UserService userService = ApplicationContextHelper.getBean(UserService.class);

    private FacilityGroupService facilityGroupService = ApplicationContextHelper.getBean(FacilityGroupService.class);

    private static String failResult = "failure";

    @Override
    protected String getMessage(String message, User user) {
        try {
            Map<String, String> map = JSON.parseObject(message, new TypeReference<Map<String,String>>(){});
            int userId = Integer.parseInt(map.get("userId"));
            int facilityGroupId = userService.get(userId, false).getFacilityGroupId();
            List<String> facDomainList = facilityGroupService.getFacDomains(facilityGroupId, false);
            List<Alarm> alarmList = AlarmUtils.alarmRealTimesToAlarms(RealtimeAlarmSocket.REALTIME_ALARM_LIST);
            List<Alarm> alarmReportList = Lists.newArrayList();
//            Alarm alarm = new Alarm();
//            alarm.setAlarmId("1");
//            alarm.setRealName("d129基站");
//            alarm.setDomainName("r129@pdt.cn");
//            alarm.setCodeName("断电");
//            String time = "2017-11-27 10:10:10";
//            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            alarm.setAlarmTimestamp(sdf.parse(time));
//            alarm.setLevelName("严重");
//            alarm.setDescription("告警描述");
//            alarm.setProposal("处理建议");
//            alarmList.add(alarm);
//            Alarm alarm1 = new Alarm();
//            alarm1.setAlarmId("2");
//            alarm1.setRealName("基站213");
//            alarm1.setDomainName("r105@pdt.cn");
//            alarm1.setCodeName("故障");
//            time = "2017-11-29 10:10:11";
//            sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            alarm1.setAlarmTimestamp(sdf.parse(time));
//            alarm1.setLevelName("一般");
//            alarm1.setDescription("告警描述");
//            alarm1.setProposal("处理建议");
//            alarmList.add(alarm1);
//            Alarm alarm2 = new Alarm();
//            alarm2.setAlarmId("3");
//            alarm2.setRealName("基站213");
//            alarm2.setDomainName("r105@pdt.cn");
//            alarm2.setCodeName("故障");
//            time = "2017-11-28 10:10:12";
//            sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            alarm2.setAlarmTimestamp(sdf.parse(time));
//            alarm2.setLevelName("轻微");
//            alarm2.setDescription("告警描述");
//            alarm2.setProposal("处理建议");
//            alarmList.add(alarm2);
//            Alarm alarm3 = new Alarm();
//            alarm3.setAlarmId("4");
//            alarm3.setRealName("有线固定台服务器");
//            alarm3.setDomainName("srs22@pdt.cn");
//            alarm3.setCodeName("故障1");
//            time = "2017-11-29 10:10:12";
//            sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            alarm3.setAlarmTimestamp(sdf.parse(time));
//            alarm3.setLevelName("严重");
//            alarm3.setDescription("告警描述");
//            alarm3.setProposal("处理建议");
//            alarmList.add(alarm3);
//            Alarm alarm4 = new Alarm();
//            alarm4.setAlarmId("5");
//            alarm4.setRealName("基站213");
//            alarm4.setDomainName("r105@pdt.cn");
//            alarm4.setCodeName("故障");
//            time = "2017-12-01 10:10:12";
//            sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            alarm4.setAlarmTimestamp(sdf.parse(time));
//            alarm4.setLevelName("严重");
//            alarm4.setDescription("告警描述");
//            alarm4.setProposal("处理建议");
//            alarmList.add(alarm4);
            for (int i=0;i<alarmList.size();i++) {
                //根据设备域名检查告警数据，若不满足，则删除，不上报
                if (facDomainList.contains(alarmList.get(i).getDomainName())) alarmReportList.add(alarmList.get(i));
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
