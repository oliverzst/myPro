package com.actec.bsms.repository.dao;

import com.actec.bsms.entity.Watch;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 值守DAO接口
 * @author zhangst
 * @create 2017-11-13 5:28 PM
 */
public interface WatchDao extends CrudDao<Watch> {

    void createTable();

    Watch findByTaskId(@Param("taskId") int taskId);

    List<Watch> findHistoryByUserId(@Param("userId") int userId);

    List<Watch> findHistoryByUserIdAndMonth(@Param("year") int year, @Param("month") int month, @Param("userId") int userId);

}
