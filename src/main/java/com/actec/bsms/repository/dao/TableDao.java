package com.actec.bsms.repository.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by wdl on 2017/3/28.
 */
@MyBatisDao
public interface TableDao extends CrudDao {
    int tableNum(@Param("tableName") String tableName);

    List<String> allTableNames(@Param("tableName") String tableName);

    void deleteTableByName(@Param("tableName") String tableName);

    void deleteColumn(@Param("columnName") String columnName, @Param("tableName") String tableName);

    int columnExisted(@Param("columnName") String columnName, @Param("tableName") String tableName);

    void addVarCharColumn(@Param("columnName") String columnName, @Param("tableName") String tableName);

    void addBigVarCharColumn(@Param("columnName") String columnName, @Param("tableName") String tableName);

    List<String> hasRecordTableNames(@Param("tableName") String recordTimeKey);

    void addIntColumn(@Param("columnName") String columnName, @Param("tableName") String tableName);

    void addDateColumn(@Param("columnName") String columnName, @Param("tableName") String tableName);
}
