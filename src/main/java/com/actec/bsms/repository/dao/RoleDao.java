/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/infosoul/mserver">Freelance</a> All rights reserved.
 */
package com.actec.bsms.repository.dao;

import com.actec.bsms.entity.Role;

/**
 * 角色DAO接口
 * @author Freelance
 * @version 2013-12-05
 */
@MyBatisDao
public interface RoleDao extends CrudDao<Role> {
    void createTable();
    void initTable();
    void initTable1();
    void initTable2();
}
