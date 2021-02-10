package com.imooc.o2o.dao;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.enums.ProductCategoryEnum;
import junit.framework.TestCase;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING) //按照名称顺序进行测试
public class ProductCategoryDaoTest extends BaseTest {
    @Autowired
    private ProductCategoryDao productCategoryDao;


    @Test
    public void testBQueryProductCategoryList() {
        long shopId = 1;
        List<ProductCategory> productCategories = productCategoryDao.queryProductCategoryList(shopId);
        for (ProductCategory productCategory : productCategories) {
            System.out.println(productCategory);
        }
    }

    @Test
    public void testABatchInsertProductCategory(){
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryName("商品类别1");
        productCategory.setPriority(1);
        productCategory.setShopId(1L);
        ProductCategory productCategory2 = new ProductCategory();
        productCategory2.setCreateTime(new Date());
        productCategory2.setProductCategoryName("商品类别2");
        productCategory2.setPriority(2);
        productCategory2.setShopId(1L);
        productCategory2.setCreateTime(new Date());
        List<ProductCategory> productCategoryList = new ArrayList<>();
        productCategoryList.add(productCategory);
        productCategoryList.add(productCategory2);

        int effectNum = productCategoryDao.batchInserProductCategory(productCategoryList);
        System.out.println(effectNum);
    }

    @Test
    public void testCdeleteProductCategory(){
       long shopId = 1L;
        List<ProductCategory> productCategoryList = productCategoryDao.queryProductCategoryList(shopId);
        for (ProductCategory productCategory : productCategoryList) {
            if ("商品类别1".equals(productCategory.getProductCategoryName()) || "商品类别2".equals(productCategory.getProductCategoryName())){
                int effectNum = productCategoryDao.deleteProductCategory(productCategory.getProductCategoryId(), shopId);
                System.out.println(effectNum);
            }
        }
    }
}