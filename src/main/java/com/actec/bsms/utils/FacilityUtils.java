package com.actec.bsms.utils;

import com.actec.bsms.entity.Facility;
import com.actec.bsms.entity.RcuInfo;

import java.math.BigDecimal;

/**
 * 设备工具类
 *
 * @author zhangst
 * @create 2017-11-26 11:03 AM
 */
public class FacilityUtils {

    public static Facility rcuInfoToFacility(RcuInfo rcuInfo, Facility facility) {
        facility.setName(rcuInfo.getName()!=null?rcuInfo.getName():"");
        facility.setIp(rcuInfo.getIp_addr()!=null?rcuInfo.getIp_addr():"");
        facility.setDomain(rcuInfo.getDomain_name()!=null?rcuInfo.getDomain_name():"");
        facility.setCchFreq(rcuInfo.getCchFreq()!=null?rcuInfo.getCchFreq():"");
        facility.setSysNumber(rcuInfo.getRcu_lai());
        facility.setLongitude(new BigDecimal(rcuInfo.getGn()).setScale(5,BigDecimal.ROUND_HALF_UP));
        facility.setLatitude(new BigDecimal(rcuInfo.getGe()).setScale(5,BigDecimal.ROUND_HALF_UP));
        facility.setManufacturer(rcuInfo.getManufacturer()!=null?rcuInfo.getManufacturer():"");
        facility.setTchCount(rcuInfo.getTchCount());
        facility.setRcuStatus(rcuInfo.getRcuStatus());
        facility.setAddress(rcuInfo.getTsAddress()!=null?rcuInfo.getTsAddress():"");
        if (facility.getStatus()==null) {
            facility.setStatus("");
        }
        facility.setType(Facility.facilityType.BASE_STATION.getValue());
        //facility.setAddress(rcuInfo.getTs_address()!=null?rcuInfo.getTs_address():"");
        return facility;
    }

}
