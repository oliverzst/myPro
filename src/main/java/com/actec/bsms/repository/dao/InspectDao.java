package com.actec.bsms.repository.dao;

import com.actec.bsms.entity.Inspect;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务DAO接口
 * @author zhangst
 * @create 2017-11-13 5:28 PM
 */
public interface InspectDao extends CrudDao<Inspect> {

    void createTable();

    /**
     * 根据ID查询
     * @param
     * @return
     */
    Inspect findById(@Param("id") int id);

    /**
     * 根据ID查询历史
     * @param
     * @return
     */
    Inspect findByIdByTableName(@Param("id") int id, @Param("tableName") String tableName);

    /**
     * 查询所有历史巡检
     * @param
     * @return
     */
    List<Inspect> findHistoryByTableName(@Param("tableName") String tableName);

    /**
     * 根据设备查询指定类型巡检，type=0自主巡检、type=1任务巡检
     * @param
     * @return
     */
    List<Inspect> findByFacilityDomain(@Param("facilityDomain") String facilityDomain, @Param("type") int type);

    /**
     * 根据设备和用户查询指定类型巡检，type=0自主巡检、type=1任务巡检
     * @param
     * @return
     */
    List<Inspect> findByFacilityAndUser(@Param("facilityDomain") String facilityDomain, @Param("userId") int userId, @Param("type") int type);

    Inspect findByFacilityAndUserAndInspectDeviceType(@Param("facilityDomain") String facilityDomain, @Param("userId") int userId, @Param("inspectDeviceTpyeId") int inspectDeviceTpyeId, @Param("type") int type);

    /**
     * 根据执行人查询巡检
     * @param
     * @return
     */
    List<Inspect> findByInspectBy(@Param("userId") int userId);

    /**
     * 根据执行人查询已完成巡检
     * @param
     * @return
     */
    List<Inspect> findByInspectByEnd(@Param("userId") int userId);

    /**
     * 按年月执行人已完成巡检
     * @param
     * @return
     */
    List<Inspect> findByInspectByMonth(@Param("year") int year, @Param("month") int month, @Param("userId") int userId);

    /**
     * 根据设备查询已完成巡检
     * @param
     * @return
     */
    List<Inspect> findHistoryInspectByFacilityDomain(@Param("facilityDomain") String facilityDomain);

    /**
     * 按年月查询设备已完成巡检
     * @param
     * @return
     */
    List<Inspect> findHistoryInspectByFacilityDomainByMonth(@Param("year") int year, @Param("month") int month, @Param("facilityDomain") String facilityDomain);

}
