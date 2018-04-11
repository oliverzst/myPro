package com.actec.bsms.repository.serial;

import org.apache.mina.core.buffer.IoBuffer;

/**
 * 自定义串口信息类
 *
 * @author zhangst
 * @create 2018-04-10 4:40 PM
 */

public class SendMessages implements java.io.Serializable {

    public String msg="0";

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getmsgleng(){
        return msg.getBytes().length;
    }

    public void encoder(IoBuffer b){
        b.put(msg.getBytes());
        //b.putInt(1);
    }

}
