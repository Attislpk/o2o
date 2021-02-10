package com.imooc.o2o.service;

import com.imooc.o2o.dto.ProductCategoryExecution;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.exception.ProductCategoryOperationException;

import java.util.List;

public interface ProductCategoryService {

    List<ProductCategory> getProductCategoryList(long shopId);


    ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList)throws ProductCategoryOperationException;

    /**
     * 将该类别下的商品内的类别id置空，再删除掉该商品类别
     * @param productCategoryId
     * @param shopId
     * @return
     * @throws ProductCategoryOperationException
     */
    ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId) throws ProductCategoryOperationException;
}
