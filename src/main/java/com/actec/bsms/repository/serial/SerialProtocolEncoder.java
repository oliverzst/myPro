package com.actec.bsms.repository.serial;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 * 串口通信编码器 直接发出去 不做处理
 *
 * @author zhangst
 * @create 2018-04-10 4:13 PM
 */

public class SerialProtocolEncoder  extends ProtocolEncoderAdapter {

    @Override
    public void encode(IoSession session, Object message,
                       ProtocolEncoderOutput out) throws Exception {
        out.write(message);
        out.flush();
    }

}
