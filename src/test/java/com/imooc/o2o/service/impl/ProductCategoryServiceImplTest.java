package com.imooc.o2o.service.impl;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.service.ProductCategoryService;
import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

public class ProductCategoryServiceImplTest extends BaseTest {
    @Autowired
    private ProductCategoryService productCategoryService;

    @Test
    public void testGetProductCategoryList() {
        Shop shop = new Shop();
        shop.setShopId(1L);
        List<ProductCategory> productCategoryList = productCategoryService.getProductCategoryList(shop.getShopId());
        for (ProductCategory productCategory : productCategoryList) {
            System.out.println(productCategory);
        }

    }
}