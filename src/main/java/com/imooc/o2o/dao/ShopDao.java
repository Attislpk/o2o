package com.imooc.o2o.dao;

import com.imooc.o2o.entity.Shop;

/**
 * 对shop进行操作的接口
 */
public interface ShopDao {

    /**
     * 向数据库中插入shop
     * @return 影响的行数
     */
    int insertShop(Shop shop);


    /**
     * 对shop进行更新
     * @param shop 新shop的信息
     * @return 影响的条数
     */
    int updateShop(Shop shop);
}
