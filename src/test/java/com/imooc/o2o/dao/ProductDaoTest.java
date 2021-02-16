package com.imooc.o2o.dao;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.ProductImg;
import com.imooc.o2o.entity.Shop;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public class ProductDaoTest extends BaseTest {
    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductImgDao productImgDao;

    @Test
    public void testBQueryProductList() {
    }

    @Test
    public void testQueryProductCount() {
    }

    @Test
    public void testCQueryProductByProductId() {
        long productId = 1L;
        //初始化两个商品详情图实例，作为productId=1下的商品的详情图片
        //批量插入到商品详情图表中
        ProductImg productImg1 = new ProductImg();
        productImg1.setImgAddr("图片1");
        productImg1.setImgDesc("测试图片1");
        productImg1.setPriority(1);
        productImg1.setCreateTime(new Date());
        productImg1.setProductId(productId);

        ProductImg productImg2 = new ProductImg();
        productImg2.setImgAddr("图片2");
        productImg2.setImgDesc("测试图片2");
        productImg2.setPriority(1);
        productImg2.setCreateTime(new Date());
        productImg2.setProductId(productId);

        List<ProductImg> productImgList = new ArrayList<>();
        productImgList.add(productImg2);
        productImgList.add(productImg1);
        int effectNum = productImgDao.batchInsertProductImg(productImgList);
        assertEquals(2,effectNum);
        //查询productId=1的商品信息并校验返回的详情图实例列表size是否为2
        Product product = productDao.queryProductByProductId(productId);
        assertEquals(2,product.getProductImgList().size());
        //删除新增的商品详情图实例
        effectNum = productImgDao.deleteProductImgByProductId(productId);
        assertEquals(2,effectNum);
    }

    @Test
    public void testAInsertProduct() {
        Shop shop = new Shop();
        shop.setShopId(1L);
        ProductCategory productCategory = new ProductCategory();
        productCategory.setShopId(1L);
        productCategory.setProductCategoryId(1L);

        Product product1= new Product();
        product1.setProductName("test1");
        product1.setProductDesc("测试des1");
        product1.setImgAddr("test1");
        product1.setPriority(1);
        product1.setStatus(1);
        product1.setCreateTime(new Date());
        product1.setLastEditTime(new Date());
        product1.setShop(shop);
        product1.setProductCategory(productCategory);

        Product product2= new Product();
        product2.setProductName("test2");
        product2.setProductDesc("测试des2");
        product2.setImgAddr("test2");
        product2.setPriority(1);
        product2.setStatus(1);
        product2.setCreateTime(new Date());
        product2.setLastEditTime(new Date());
        product2.setShop(shop);
        product2.setProductCategory(productCategory);

        Product product3= new Product();
        product3.setProductName("test3");
        product3.setProductDesc("测试des3");
        product3.setImgAddr("test3");
        product3.setPriority(1);
        product3.setStatus(1);
        product3.setCreateTime(new Date());
        product3.setLastEditTime(new Date());
        product3.setShop(shop);
        product3.setProductCategory(productCategory);

        System.out.println(productDao.insertProduct(product1));
        System.out.println(productDao.insertProduct(product2));
        System.out.println(productDao.insertProduct(product3));
    }

    @Test
    public void testUpdateProduct() {
        Product product = new Product();
        ProductCategory productCategory = new ProductCategory();
        Shop shop = new Shop();
        shop.setShopId(1L);
        productCategory.setProductCategoryId(2L);
        product.setProductId(1L);
        product.setShop(shop);
        product.setProductName("测试商品1");
        product.setProductCategory(productCategory);
        //修改productId=1的商品的名称(shopId=1L)
        int effectNum = productDao.updateProduct(product);
        assertEquals(1,effectNum);
    }

    @Test
    public void testUpdateProductCategoryToNull() {
    }

    @Test
    public void testDeleteProduct() {
    }
}