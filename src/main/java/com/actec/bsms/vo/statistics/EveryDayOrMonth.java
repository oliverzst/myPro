package com.actec.bsms.vo.statistics;

import java.util.List;

/**
 * 按天或按月分类
 *
 * @author zhangst
 * @create 2017-12-21 10:48 AM
 */

public class EveryDayOrMonth {

    String dateKey;

    List<CountType> countList;

    public String getDateKey() {
        return dateKey;
    }

    public void setDateKey(String dateKey) {
        this.dateKey = dateKey;
    }

    public List<CountType> getCountList() {
        return countList;
    }

    public void setCountList(List<CountType> countList) {
        this.countList = countList;
    }
}
