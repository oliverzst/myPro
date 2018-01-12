/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/infosoul/mserver">Freelance</a> All rights reserved.
 */
package com.actec.bsms.repository.dao;

import com.actec.bsms.entity.Menu;
import org.apache.ibatis.annotations.Param;

/**
 * 角色DAO接口
 * @author Freelance
 * @version 2013-12-05
 */
@MyBatisDao
public interface MenuDao extends CrudDao<Menu> {

    void createTable();
    void initTable();
    void initTable1();
    void initTable2();
    void initTable3();
    void initTable4();
    void initTable5();
    void initTable6();

    /**
     * 删除设备和目录关联数据
     * @param menuId,inspectDeviceTypeId
     * @return
     */
    int deleteInspectDeviceMenu(@Param("menuId") int menuId, @Param("inspectDeviceTypeId") int inspectDeviceTypeId);

    /**
     * 添加设备和目录关联数据
     * @param menuId,inspectDeviceTypeId
     * @return
     */
    int insertInspectDeviceMenu(@Param("menuId") int menuId, @Param("inspectDeviceTypeId") int inspectDeviceTypeId);

    /**
     * 查找最后一个元素
     * @return
     */
    Menu findLast();

}
