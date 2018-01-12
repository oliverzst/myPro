package com.actec.bsms.utils.facility;

import com.actec.bsms.entity.Facility;
import com.actec.bsms.entity.FacilityGroup;
import com.actec.bsms.entity.RcuInfo;
import com.actec.bsms.service.FacilityGroupService;
import com.actec.bsms.service.FacilityService;
import com.actec.bsms.utils.ApplicationContextHelper;
import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.List;

/**
 * 设备工具类
 *
 * @author zhangst
 * @create 2017-11-26 11:03 AM
 */
public class FacilityUtils {

    private static FacilityService facilityService = ApplicationContextHelper.getBean(FacilityService.class);

    private static FacilityGroupService facilityGroupService = ApplicationContextHelper.getBean(FacilityGroupService.class);

    public static void updateFacilitys(List<RcuInfo> rcuInfoList) {
        List<String> facDomainList = Lists.newArrayList();
        for (int i=0;i<rcuInfoList.size();i++) {
            RcuInfo rcuInfo = rcuInfoList.get(i);
            Facility facility = facilityService.findByDomain(rcuInfo.getDomain_name());
            if (null==facility) {
                facility = new Facility();
            }
            facility = rcuInfoToFacility(rcuInfo, facility);
            facilityService.save(facility);
            facDomainList.add(rcuInfo.getDomain_name());
        }
        List<Facility> facilityList = facilityService.findAll();
        for (int j=0;j<facilityList.size();j++) {
            if (!facDomainList.contains(facilityList.get(j).getDomain())) {
                facilityService.delete(facilityList.get(j).getId());
            }
        }
        FacilityGroup facilityGroup = facilityGroupService.get(FacilityGroup.ALL_FACILITY, true);
        facilityGroup.setFacilityList(facilityService.findAll());
        facilityGroupService.updateFacilityGroup(facilityGroup);
    }

    private static Facility rcuInfoToFacility(RcuInfo rcuInfo, Facility facility) {
        facility.setName(rcuInfo.getName()!=null?rcuInfo.getName():"");
        facility.setIp(rcuInfo.getIp_addr()!=null?rcuInfo.getIp_addr():"");
        facility.setDomain(rcuInfo.getDomain_name()!=null?rcuInfo.getDomain_name():"");
        facility.setCchFreq(rcuInfo.getCchFreq()!=null?rcuInfo.getCchFreq():"");
        facility.setSysNumber(rcuInfo.getRcu_lai());
        facility.setLongitude(new BigDecimal(rcuInfo.getGn()).setScale(5,BigDecimal.ROUND_HALF_UP));
        facility.setLatitude(new BigDecimal(rcuInfo.getGe()).setScale(5,BigDecimal.ROUND_HALF_UP));
        facility.setManufacturer("海格恒通");
        facility.setTchCount(rcuInfo.getTchCount());
        facility.setRcuStatus(rcuInfo.getRcuStatus());
        if (facility.getStatus()==null) {
            facility.setStatus("");
        }
        facility.setType(Facility.facilityType.BASE_STATION.getValue());
        //facility.setAddress(rcuInfo.getTs_address()!=null?rcuInfo.getTs_address():"");
        return facility;
    }

}
