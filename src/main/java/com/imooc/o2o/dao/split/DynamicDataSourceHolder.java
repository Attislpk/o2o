package com.imooc.o2o.dao.split;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据库信息存放类
 */
public class DynamicDataSourceHolder {
    private static Logger logger = LoggerFactory.getLogger(DynamicDataSourceHolder.class);
    private static ThreadLocal<String> contextHolder = new ThreadLocal<String>(); //存放数据库信息
    public static final String DB_MASTER= "master";
    public static final String DB_SLAVE= "slave";

    /**
     * 获取数据库类型
     * @return 数据库类型
     */
    public static String getDbType(){
        String db = contextHolder.get();
        if (db == null){
            return db=DB_MASTER;
        }
        return db;
    }

    /**
     * 设置数据库类型
     */
    public static void setDbType(String dbType){
        logger.debug("使用的数据源为： "+dbType);
        contextHolder.set(dbType);
    }

    /**
     * 清楚存储的数据库类型
     */
    public static void clearDbType(){
        contextHolder.remove();
    }

}
