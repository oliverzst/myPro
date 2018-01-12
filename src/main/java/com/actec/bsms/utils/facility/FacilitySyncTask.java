package com.actec.bsms.utils.facility;

import com.actec.bsms.repository.socket.BaseStationSocket;
import com.alibaba.fastjson.JSON;

/**
 * 设备同步线程
 *
 * @author zhangst
 * @create 2017-11-27 3:54 PM
 */

public class FacilitySyncTask implements Runnable{

    private static String rcuInfos = "[]";
    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(10000L);
                if (!rcuInfos.equals(JSON.toJSONString(BaseStationSocket.BASE_STASTION_LIST))) {
                    //更新设备数据
                    FacilityUtils.updateFacilitys(BaseStationSocket.BASE_STASTION_LIST);
                    rcuInfos = JSON.toJSONString(BaseStationSocket.BASE_STASTION_LIST);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
