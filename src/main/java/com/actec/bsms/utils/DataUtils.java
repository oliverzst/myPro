package com.actec.bsms.utils;

import org.apache.mina.core.buffer.IoBuffer;

import java.io.UnsupportedEncodingException;

/**
 * 串口数据转换工具类
 *
 * @author zhangst
 * @create 2018-04-10 3:06 PM
 */

public class DataUtils {

    public static String ioBufferToString(IoBuffer iobuffer){
        iobuffer.flip();    //调换buffer当前位置，并将当前位置设置成0
        byte[] b = new byte[iobuffer.limit()];
        iobuffer.get(b);
        //此处用StringBuffer是因为　String类是字符串常量，是不可更改的常量。而StringBuffer是字符串变量，它的对象是可以扩充和修改的。
        StringBuffer stringBuffer = new StringBuffer();

        for(int i = 0; i < b.length; i++){
            System.out.println("====" + b[i]);
            stringBuffer.append((char) b[i]); //可以根据需要自己改变类型
            System.out.println(b[i] +"---------" +i);
        }
        return stringBuffer.toString();
    }

    public static String byteToString(byte [] b)
    {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < b.length; i++)
        {
            stringBuffer.append((char) b [i]);
        }
        return stringBuffer.toString();
    }


    public static IoBuffer stringToIoBuffer(String str)
    {
        byte bt[] = new byte[0];
        try {
            //转为GBK码，解决中文乱码问题
            bt = str.getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        IoBuffer ioBuffer = IoBuffer.allocate(bt.length);
        ioBuffer.put(bt, 0, bt.length);
        ioBuffer.flip();
        return ioBuffer;
    }

    public static IoBuffer byteToIoBuffer(byte [] bt,int length)
    {

        IoBuffer ioBuffer = IoBuffer.allocate(length);
        ioBuffer.put(bt, 0, length);
        ioBuffer.flip();
        return ioBuffer;
    }

    public static String ioBufferToString(Object message)
    {
        if (!(message instanceof IoBuffer))
        {
            return "";
        }
        IoBuffer ioBuffer = (IoBuffer) message;
        byte[] b = new byte [ioBuffer.limit()];
        ioBuffer.get(b);

        String result = null;
        try {
            //解决中文乱码问题
            result = new String(b,"GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**将int转为低字节在前，高字节在后的byte数组
       b[0] = 11111111(0xff) & 01100001
        b[1] = 11111111(0xff) & (n >> 8)00000000
        b[2] = 11111111(0xff) & (n >> 8)00000000
        b[3] = 11111111(0xff) & (n >> 8)00000000
    */
    public static byte[] intToByte(int a) {
        return new byte[] {
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }

    //将低字节在前转为int，高字节在后的byte数组(与IntToByteArray1想对应)
    public static int byteToInt(byte[] bArr) {
        if(bArr.length!=4){
            return -1;
        }
        return   bArr[3] & 0xFF |
                (bArr[2] & 0xFF) << 8 |
                (bArr[1] & 0xFF) << 16 |
                (bArr[0] & 0xFF) << 24;
    }

}
