package com.imooc.o2o.dao;

import com.imooc.o2o.entity.Shop;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Property;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

/**
 * 对shop进行操作的接口
 */
public interface ShopDao {

    /**
     * 分页查询店铺。可输入的条件有：店铺名(模糊查询)，店铺状态，店铺类别，区域Id，owner
     * 根据店铺信息查询数据
     * @param shopCondition 店铺条件信息
     * @param rowIndex  从第几行开始取数据
     * @param pageSize  返回的条数
     * @return
     */
    List<Shop> queryShopList(@Param("shopCondition")Shop shopCondition, @Param("rowIndex")int rowIndex, @Param("pageSize")int pageSize);

    /**
     *
     * @param shopCondition 店铺条件信息
     * @return 返回queryShopList总数
     */
    int queryShopCount(@Param("shopCondition")Shop shopCondition);


    /**
     * 查询店铺信息
     * @param shopId 店铺编号
     * @return 店铺信息
     */
    Shop queryByShopId(long shopId);


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
