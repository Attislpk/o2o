package com.imooc.o2o.dao;

import com.imooc.o2o.entity.ProductCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductCategoryDao {


    /**
     * 返回某shopId下的所有商品类别
     * 根据shopId查询商品类别
     * @param shopId
     * @return 商品类别
     */
    List<ProductCategory> queryProductCategoryList(long shopId);


    int batchInserProductCategory(List<ProductCategory> productCategoryList);

    int deleteProductCategory(@Param("productCategoryId")long productCategoryId, @Param("shopId")long shopId);
}
