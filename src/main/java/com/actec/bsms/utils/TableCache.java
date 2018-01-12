package com.actec.bsms.utils;


import com.actec.bsms.repository.dao.TableDao;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by wdl on 2017/3/28.
 */
public class TableCache {

    private static Set<String> tableCache = new HashSet<>();
    private static Set<String> tableColumnCache = new HashSet<>();

    private static TableDao tableDao = ApplicationContextHelper.getBean(TableDao.class);

    public static boolean tableExisted(String tableName) {
        if (tableCache.contains(tableName)) {
            return true;
        } else {
            if (tableDao.tableNum(tableName) >= 1) {
                tableCache.add(tableName);
                return true;
            } 
            return false;
        }
    }

    public static boolean tableColumnExisted(String tableName, String columnName){
        if (tableColumnCache.contains(tableName + "," + columnName)){
            return true;
        }else {
            int colimn = tableDao.columnExisted(columnName, tableName);
            if (colimn == 0){
                tableDao.addIntColumn(columnName, tableName);
                colimn = tableDao.columnExisted(columnName, tableName);
            }
            if (colimn >= 1){
                tableColumnCache.add(tableName + "," + columnName);
                return true;
            }
            return false;
        }
    }

    public static void addToCache(String tableName) {
        tableCache.add(tableName);
    }


}
