/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/infosoul/mserver">Freelance</a> All rights reserved.
 */
package com.actec.bsms.repository.dao;

import com.actec.bsms.entity.Module;
import org.apache.ibatis.annotations.Param;

/**
 * 角色DAO接口
 * @author Freelance
 * @version 2013-12-05
 */
@MyBatisDao
public interface ModuleDao extends CrudDao<Module> {

    void createTable();
    void initTable();
    void initTable1();
    void initTable2();
    void initTable3();
    void initTable4();
    void initTable5();
    void initTable6();
    void initTable7();

    /**
     * 删除设备和模块关联数据
     * @param moduleId,inspectDeviceTypeId
     * @return
     */
    public int deleteInspectDeviceModule(@Param("moduleId") int moduleId, @Param("inspectDeviceTypeId") int inspectDeviceTypeId);

    /**
     * 添加设备和模块关联数据
     * @param moduleId,inspectDeviceTypeId
     * @return
     */
    int insertInspectDeviceModule(@Param("moduleId") int moduleId, @Param("inspectDeviceTypeId") int inspectDeviceTypeId);

    /**
     * 查找最后一个元素
     * @return
     */
    Module findLast();

}
