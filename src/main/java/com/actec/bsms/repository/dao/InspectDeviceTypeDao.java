/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/infosoul/mserver">Freelance</a> All rights reserved.
 */
package com.actec.bsms.repository.dao;

import com.actec.bsms.entity.InspectDeviceType;
import org.apache.ibatis.annotations.Param;

/**
 * 角色DAO接口
 * @author Freelance
 * @version 2013-12-05
 */
@MyBatisDao
public interface InspectDeviceTypeDao extends CrudDao<InspectDeviceType> {

    void createTable();
    void initTable();
    void initTable1();
    void initTable2();
    void initTable3();

    /**
     * 删除设备和目录关联数据
     * @param inspectDeviceType
     * @return
     */
    public int deleteInspectDeviceMenu(InspectDeviceType inspectDeviceType);
    public int deleteInspectDeviceModule(InspectDeviceType inspectDeviceType);
    public int insertInspectDeviceMenu(InspectDeviceType inspectDeviceType);
    public int insertInspectDeviceModule(InspectDeviceType inspectDeviceType);

    /**
     * 通过id查找数据
     * @param id
     * @return
     */
    public InspectDeviceType findById(@Param("id") int id);
}
