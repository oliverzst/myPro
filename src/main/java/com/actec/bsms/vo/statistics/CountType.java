package com.actec.bsms.vo.statistics;

/**
 * 统计项类
 *
 * @author zhangst
 * @create 2017-12-21 10:35 AM
 */

public class CountType {

    String name;    //名称
    int count;      //次数
    long aveTime;   //平均时间

    public CountType() {}

    public CountType(String name, int count, long aveTime) {
        this.name = name;
        this.count = count;
        this.aveTime = aveTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getAveTime() {
        return aveTime;
    }

    public void setAveTime(long aveTime) {
        this.aveTime = aveTime;
    }

}
