package com.imooc.o2o.dao;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.dao.ShopCategoryDao;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ShopCategoryDaoTest extends BaseTest {

    @Autowired
    private ShopCategoryDao shopCategoryDao;

    @Test
    public void testQuerySubshopCategory() {
        //无where条件
        List<ShopCategory> shopCategories = shopCategoryDao.QuerySubshopCategory(new ShopCategory());
        for (ShopCategory shopCategory : shopCategories) {
            System.out.println(shopCategory);
        }
    }

    @Test
    public void testQuerySubshopCategory2() {
        //带where条件
        ShopCategory testShopCategory = new ShopCategory();
        ShopCategory parentShopCategory = new ShopCategory();
        parentShopCategory.setShopCategoryId(1L);
        testShopCategory.setParent(parentShopCategory);

        //查找testShopCategory的父类parentShopCategory下的所有子类
        List<ShopCategory> shopCategories = shopCategoryDao.QuerySubshopCategory(testShopCategory);
        for (ShopCategory shopCategory : shopCategories) {
            System.out.println(shopCategory);
        }
    }
}