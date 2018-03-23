package com.actec.bsms.repository.dao;

import com.actec.bsms.entity.TroubleShoot;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 故障处理记录DAO接口
 * @author zhangst
 * @create 2017-12-13 3:28 PM
 */
public interface TroubleShootDao extends CrudDao<TroubleShoot> {

    void createTable();

    List<TroubleShoot> findHistory(@Param("userId") int userId, @Param("moduleId") int moduleId, @Param("inspectDeviceType") int inspectDeviceType, @Param("facilityDomain") String facilityDomain);

    List<TroubleShoot> findHistoryByMonth(@Param("userId") int userId, @Param("moduleId") int moduleId, @Param("inspectDeviceType") int inspectDeviceType, @Param("facilityDomain") String facilityDomain, @Param("year") int year, @Param("month") int month);

    void deleteByInspect(@Param("inspectId") int inspectId);
}
