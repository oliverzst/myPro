package com.actec.bsms.repository.serial;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * 串口通信编解码工厂类
 *
 * @author zhangst
 * @create 2018-04-10 4:11 PM
 */

public class SerialProtocolCodecFactory  implements ProtocolCodecFactory {

    private SerialProtocolDecoder decoder;
    private SerialProtocolEncoder encoder;

    public SerialProtocolCodecFactory() {
        encoder = new SerialProtocolEncoder();
        decoder = new SerialProtocolDecoder();
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
        return decoder;
    }

    @Override
    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
        return encoder;
    }

}
