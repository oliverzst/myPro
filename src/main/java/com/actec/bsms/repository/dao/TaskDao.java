package com.actec.bsms.repository.dao;

import com.actec.bsms.entity.Task;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务DAO接口
 * @author zhangst
 * @create 2017-11-13 5:28 PM
 */
public interface TaskDao extends CrudDao<Task> {

    void createTable();

    void createMonthTable(@Param("year") int year, @Param("month") int month);

    void updateTaskTable(@Param("year") int year, @Param("month") int month);

    void deleteMonthTable(@Param("year") int year, @Param("month") int month);

    void deleteTask(@Param("id") int id);

    /**
     * 根据设备查询指定类型任务，type=1巡检任务、type=2值守任务
     * @param
     * @return
     */
    List<Task> findByFacilityDomain(@Param("facilityDomain") String facilityDomain, @Param("type") int type);

    /**
     * 查询所有还未完成的任务
     * @param
     * @return
     */
    List<Task> findNoFinishList();

    /**
     * 查询所最新添加的任务
     * @param
     * @return
     */
    Task findNewTask();

    /**
     * 根据设备和用户查询指定类型任务，type=1巡检任务、type=2值守任务
     * @param
     * @return
     */
    List<Task> findByFacilityAndUser(@Param("facilityDomain") String facilityDomain, @Param("userId") int userId, @Param("type") int type);

    /**
     * 根据设备和用户和设备巡检类型查询指定类型任务，type=1巡检任务、type=2值守任务
     * @param
     * @return
     */
    List<Task> findByFacilityAndUserAndInspectDeviceType(@Param("facilityDomain") String facilityDomain, @Param("userId") int userId,  @Param("inspectDeviceTypeId") int inspectDeviceTypeId, @Param("type") int type);

    /**
     * 根据设备和任务发布者、接受者查询指定类型任务，type=1巡检任务、type=2值守任务
     * @param
     * @return
     */
    Task findByFacAndReleaseByAndInspectBy(@Param("facilityDomain") String facilityDomain, @Param("releaseBy") int releaseBy, @Param("inspectBy") int inspectBy, @Param("type") int type);

    /**
     * 根据设备和用户ID查询指定类型任务，type=1巡检任务、type=2值守任务
     * @param
     * @return
     */
    List<Task> findByFacilityDomainAndUserId(@Param("userId") int userId, @Param("facilityDomain") String facilityDomain, @Param("type") int type);

    /**
     * 根据执行人查询任务
     * @param
     * @return
     */
    List<Task> findByInspectBy(@Param("userId") int userId, @Param("type") int type);

    /**
     * 根据执行人查询已完成任务
     * @param
     * @return
     */
    List<Task> findByInspectByEnd(@Param("userId") int userId, @Param("type") int type);

    /**
     * 按年月执行人已完成任务
     * @param
     * @return
     */
    List<Task> findByInspectByMonth(@Param("year") int year, @Param("month") int month, @Param("userId") int userId, @Param("type") int type);

    /**
     * 根据设备查询已完成任务
     * @param
     * @return
     */
    List<Task> findHistoryTaskByFacilityDomain(@Param("facilityDomain") String facilityDomain, @Param("type") int type);

    /**
     * 按年月查询设备已完成任务
     * @param
     * @return
     */
    List<Task> findHistoryTaskByFacilityDomainByMonth(@Param("year") int year, @Param("month") int month, @Param("facilityDomain") String facilityDomain, @Param("type") int type);

    /**
     * 检查发布任务是否重复
     * @param
     * @return
     */
    List<Task> checkTaskExist(@Param("taskName") String taskName, @Param("description") String description, @Param("facilityDomain") String facilityDomain, @Param("applyBy") int applyBy);

    List<Task> checkReceivedTaskExist(@Param("taskName") String taskName, @Param("description") String description, @Param("facilityDomain") String facilityDomain, @Param("applyBy") int applyBy);

}
