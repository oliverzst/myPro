package com.actec.bsms.repository.serial;

import com.actec.bsms.utils.DataUtils;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 串口通信客户端Handler
 *
 * @author zhangst
 * @create 2018-04-10 11:04 AM
 */

public class SerialHandler extends IoHandlerAdapter {

    private final Logger LOG = LoggerFactory.getLogger(SerialHandler.class);

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        // TODO Auto-generated method stub
        String receivedMessage = DataUtils.ioBufferToString(message);
        LOG.warn("串口客户端接收到消息：" + receivedMessage);
        if (receivedMessage.equals("1111")) {
            //收到心跳包
            LOG.warn("收到心跳包");
            session.write("1112");
        }
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        // TODO Auto-generated method stub
        super.messageSent(session, message);
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        // TODO Auto-generated method stub
        LOG.error("串口客户端关闭");
        session.close(true);
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        // TODO Auto-generated method stub
        super.sessionCreated(session);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        // TODO Auto-generated method stub
        System.out.println("串口客户端空闲");
        if(status==IdleStatus.BOTH_IDLE){
//            this.messageSent(session, "空闲测试");
//            SendMessages send=new SendMessages();
//            send.setMsg("aaaaa");
//            this.sendMessages(session, send);
        }
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        // TODO Auto-generated method stub
        LOG.warn("串口客户端打开");
        //10秒内没有读写就设置为空闲通道
//        session.setIdleTime(IdleStatus.BOTH_IDLE, 100);
        //自定义包解析
        //ProtocolCodecFactory codec=new TestProtocolCodecFactory();
        //arg0.getFilterChain().addFirst("test", new ProtocolCodecFilter(codec));
//        session.getFilterChain().addLast("a", new ProtocolCodecFilter(new TestProtocolCodecFactory()));
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable arg1) throws Exception {
        // TODO Auto-generated method stub
        LOG.error("串口客户端异常:"+arg1);
//        sessionClosed(session);
    }

    private void sendMessages(IoSession arg0, Object arg1){
        LOG.warn("sendMessages 发送");
        arg0.write(arg1);
    }

}
