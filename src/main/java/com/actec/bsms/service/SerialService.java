package com.actec.bsms.service;

import com.actec.bsms.config.SerialConfig;
import com.actec.bsms.utils.DataUtils;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 串口数据处理类
 *
 * @author zhangst
 * @create 2018-04-10 6:14 PM
 */
@Service
public class SerialService {

    @Autowired
    SerialConfig serialConfig;

    private final Logger LOG = LoggerFactory.getLogger(SerialService.class);

    public void sendMessage(String jsonString) {
        try {
            IoSession session = serialConfig.getFuture().getSession();
            //要发送的字符串
            //将字符串转换为IoBuffer类型后发送
            session.write(DataUtils.stringToIoBuffer(jsonString));
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }

}
