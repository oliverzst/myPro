/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/infosoul/mserver">Freelance</a> All rights reserved.
 */
package com.actec.bsms.repository.dao;

import com.actec.bsms.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 用户DAO接口
 * @author zhangsiteng
 * @version 2017-11-14
 */
@MyBatisDao
public interface UserDao extends CrudDao<User> {

    void createTable();
    void initTable();
    void initTable1();
    void initTable2();

    /**
     * 根据登录名称查询用户
     * @param
     * @return
     */
    User findByLoginName(@Param("loginName") String loginName);

    /**
     * 获取最近添加的用户
     * @return
     */
    User findLastInsertUser();

    /**
     * 获取最近添加的用户
     * @return
     */
    void batchUpdate(@Param("userList") List<User> userList);

    /**
     * 根据登录名称查询用户
     * @param
     * @return
     */
    int checkRegister(@Param("loginName") String loginName, @Param("phone") String phone, @Param("name") String name);

    /**
     * 更新登录信息，如：登录IP、登录时间
     * @param deviceId
     * @return
     */
    int updateLoginInfo(@Param("id") int id, @Param("deviceId") String deviceId, @Param("loginDate") Date loginDate);

    /**
     * 修改密码
     * @param password
     * @return
     */
    void modifyPassword(@Param("id") int id, @Param("password") String password);

    /**
     * 根据登录名称查询用户
     * @param deviceId
     * @return
     */
    User findByDevice(@Param("deviceId") String deviceId);

    /**
     * 根据roleId查询下级用户
     * @param roleId
     * @return
     */
    List<User> findByRoleId(@Param("roleId") int roleId);

}
