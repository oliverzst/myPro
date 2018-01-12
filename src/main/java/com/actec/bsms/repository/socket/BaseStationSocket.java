package com.actec.bsms.repository.socket;

import com.actec.bsms.entity.RcuInfo;
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
public class BaseStationSocket extends AbstractVerticle{
    private static Logger logger = LoggerFactory.getLogger(BaseStationSocket.class);

    private static final int BSMS_SWITCHER_PORT = 8400;
    public static final List<RcuInfo> BASE_STASTION_LIST = Lists.newArrayList();
    private static final int BUFFER_SIZE = 1024 * 1024 * 10;

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        DatagramSocketOptions datagramSocketOptions = new DatagramSocketOptions();
        datagramSocketOptions.setReceiveBufferSize(BUFFER_SIZE);
        DatagramSocket socket = vertx.createDatagramSocket(datagramSocketOptions);
        socket.listen(BSMS_SWITCHER_PORT, "0.0.0.0", fromClientResult -> {
            if (fromClientResult.succeeded()) {
                socket.handler(datagramPacket -> {
                    Buffer buf = datagramPacket.data();

                    List<RcuInfo> currentRcus = JSON.parseArray(buf.toString(), RcuInfo.class);
                    BASE_STASTION_LIST.clear();
                    BASE_STASTION_LIST.addAll(currentRcus);
                    logger.debug("received base station list : " + RcuInfo.listToString(currentRcus));
                    socket.send("success", datagramPacket.sender().port(), datagramPacket.sender().host(), toClientResult -> {
                        logger.debug("send success to client");
                    });
                });
            }
        });
    }

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(new BaseStationSocket());
    }

}
