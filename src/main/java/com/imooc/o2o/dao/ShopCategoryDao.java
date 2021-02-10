package com.imooc.o2o.dao;

import com.imooc.o2o.entity.ShopCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopCategoryDao {

    //传入一个shopCategory,查询该shopCategory的父shopCategory下所有的子shopCategory
    List<ShopCategory> querySubshopCategory(@Param("shopCategoryCondition") ShopCategory shopCategoryCondition);
}
