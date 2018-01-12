package com.actec.bsms.repository.socket;

import com.actec.bsms.entity.AlarmRealTime;
import com.alibaba.fastjson.JSON;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.datagram.DatagramSocketOptions;
import jersey.repackaged.com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by wdl on 2017/11/7.
 */
public class RealtimeAlarmSocket extends AbstractVerticle {

    private static Logger logger = LoggerFactory.getLogger(RealtimeAlarmSocket.class);
    private static final int BSMS_REALTIME_ALARM_PORT = 8500;
    private static final int BUFFER_SIZE = 1024 * 1024 * 10;
    public static final List<AlarmRealTime> REALTIME_ALARM_LIST = Lists.newArrayList();

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        DatagramSocketOptions datagramSocketOptions = new DatagramSocketOptions();
        datagramSocketOptions.setReceiveBufferSize(BUFFER_SIZE);
        DatagramSocket socket = vertx.createDatagramSocket(datagramSocketOptions);

        socket.listen(BSMS_REALTIME_ALARM_PORT, "0.0.0.0", asyncResult -> {
            if (asyncResult.succeeded()) {
                socket.handler(datagramPacket -> {
                    Buffer buf = datagramPacket.data();
                    List<AlarmRealTime> currentAlarms = JSON.parseArray(buf.toString(), AlarmRealTime.class);
                    logger.debug("received realTime alarm list : " + AlarmRealTime.listToString(currentAlarms));
                    REALTIME_ALARM_LIST.clear();
                    REALTIME_ALARM_LIST.addAll(currentAlarms);
                    socket.send("success", datagramPacket.sender().port(), datagramPacket.sender().host(), toClientResult -> {
                        logger.debug("send success to client");
                    });
                });
            }
        });
    }

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(new RealtimeAlarmSocket());
    }
}
