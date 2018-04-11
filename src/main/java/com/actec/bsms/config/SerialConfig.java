package com.actec.bsms.config;

import com.actec.bsms.repository.serial.SerialProtocolCodecFactory;
import com.actec.bsms.repository.serial.SerialHandler;
import com.actec.bsms.utils.Global;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.serial.SerialAddress;
import org.apache.mina.transport.serial.SerialConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Mina串口通信配置
 *
 * @author zhangst
 * @create 2018-04-09 6:04 PM
 */
@Configuration
public class SerialConfig {

    private static Logger log = LoggerFactory.getLogger(SerialConfig.class);
    public static String PORT = Global.getConfig("com.port");

    private ConnectFuture future;

    public ConnectFuture getFuture() {
        return future;
    }

    @Bean
    public LoggingFilter loggingFilter() {
        return new LoggingFilter();
    }

    @Bean
    public SerialProtocolCodecFactory serialProtocolCodecFactory() {
        return new SerialProtocolCodecFactory();
    }

    @Bean
    public IoHandlerAdapter ioHandlerAdapter() {
        return new SerialHandler();
    }

    @Bean
    public SerialConnector serialCon()
    {
        //创建串口连接
        SerialConnector connector = new SerialConnector();
        //绑定处理handler
        connector.setHandler(ioHandlerAdapter());
        //设置日志过滤器
        connector.getFilterChain().addLast("logger",loggingFilter());
        //设置编解码器过滤器
//        connector.getFilterChain().addLast("codec",
//                new ProtocolCodecFilter(serialProtocolCodecFactory()));
        //配置串口连接
        SerialAddress address = new SerialAddress
                (PORT, 57600, SerialAddress.DataBits.DATABITS_8, SerialAddress.StopBits.BITS_1 ,
                        SerialAddress.Parity.EVEN, SerialAddress.FlowControl.NONE);

        future = connector.connect(address);
        try {
            future.await();
//            IoSession sessin = future.getSession();
//            pooler= new SerialPooler();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        log.info("Serial Server Started");
        return connector;
    }

    public void close()
    {
        future.cancel();
        log.info("UDP Server closed");
    }

}
