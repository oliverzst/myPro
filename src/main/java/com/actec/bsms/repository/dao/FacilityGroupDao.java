package com.actec.bsms.repository.dao;

import com.actec.bsms.entity.FacilityGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务DAO接口
 * @author zhangst
 * @create 2017-11-13 5:28 PM
 */
@MyBatisDao
public interface FacilityGroupDao extends CrudDao<FacilityGroup> {
    /**
     * 创建设备组表
     */
    void createTable();
    void initTable();
    /**
     * 创建设备设备组关联表
     */
    void createFacilityGroupTable();
    /**
     * 删除设备和设备组关联数据
     * @param facilityGroup
     * @return
     */
    int deleteFacilityGroup(FacilityGroup facilityGroup);

    /**
     * 插入设备和设备组关联数据
     * @param facilityGroup
     * @return
     */
    int insertFacilityGroup(FacilityGroup facilityGroup);

    /**
     * 插入设备和设备组关联数据
     * @param name
     * @return
     */
    FacilityGroup findByName(@Param("name") String name);

    /**
     * 查找所有设备组
     * @return
     */
    List<FacilityGroup> findAll();

}
