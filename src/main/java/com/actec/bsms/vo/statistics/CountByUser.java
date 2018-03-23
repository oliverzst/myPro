package com.actec.bsms.vo.statistics;

import java.util.List;

/**
 * 按年月统计分类
 *
 * @author zhangst
 * @create 2017-12-21 11:07 AM
 */

public class CountByUser {

    List<EveryDayOrMonth> every;

    List<CountType> totalType;

    List<CountType> totalDevice;

    public List<EveryDayOrMonth> getEvery() {
        return every;
    }

    public void setEvery(List<EveryDayOrMonth> every) {
        this.every = every;
    }

    public List<CountType> getTotalType() {
        return totalType;
    }

    public void setTotalType(List<CountType> totalType) {
        this.totalType = totalType;
    }

    public List<CountType> getTotalDevice() {
        return totalDevice;
    }

    public void setTotalDevice(List<CountType> totalDevice) {
        this.totalDevice = totalDevice;
    }
}
