package com.actec.bsms.repository.dao;

import com.actec.bsms.entity.Facility;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务DAO接口
 * @author zhangst
 * @create 2017-11-13 5:28 PM
 */
@MyBatisDao
public interface FacilityDao extends CrudDao<Facility> {

    void createTable();

    void delete(@Param("id") int id);

    List<Facility> findAll();

    /**
     * 根据域名查询设备
     * @param domain
     * @return
     */
    Facility findByDomain(@Param("domain") String domain);

    /**
     * 删除关联表中的数据
     * @param domain
     * @return
     */
    void deleteFacilityGroup(@Param("domain") String domain);

}
