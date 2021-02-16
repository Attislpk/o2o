package com.imooc.o2o.service.impl;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ProductStateEnum;
import com.imooc.o2o.exception.ShopOperationException;
import com.imooc.o2o.service.ProductService;
import junit.framework.TestCase;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public class ProductServiceImplTest extends BaseTest {
    @Autowired
    private ProductService productService;

    @Test
    @Ignore
    public void testAddProduct() throws ShopOperationException, FileNotFoundException {
        //创建product, shop, productCategory 对象
        Product product = new Product();
        Shop shop = new Shop();
        shop.setShopId(1L);
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryId(1L);
        product.setShop(shop);
        product.setProductCategory(productCategory);
        product.setProductName("测试商品1");
        product.setProductDesc("测试商品1 ");
        product.setPriority(20);
        product.setCreateTime(new Date());
        product.setStatus(ProductStateEnum.SUCCESS.getState());
        //创建缩略图文件流
        File thumbnailFile = new File("C:/Users/1/Desktop/car.jpg");
        InputStream inputStream = new FileInputStream(thumbnailFile);
        ImageHolder thumbnail = new ImageHolder(thumbnailFile.getName(), inputStream);
        //创建商品详情图文件
        File productImg1 = new File("C:/Users/1/Desktop/car.jpg");
        File productImg2 = new File("C:/Users/1/Desktop/QQ.jpg");
        InputStream stream1 = new FileInputStream(productImg1);
        InputStream stream2 = new FileInputStream(productImg2);
        List<ImageHolder> productImgList = new ArrayList<>();
        productImgList.add(new ImageHolder(productImg1.getName(), stream1));
        productImgList.add(new ImageHolder(productImg2.getName(), stream2));
        //添加商品并验证
        ProductExecution productExecution = productService.addProduct(product, thumbnail, productImgList);
        System.out.println(productExecution);

    }

    @Test
    public void testBModifyProduct() throws FileNotFoundException {
        Product product = new Product();
        Shop shop = new Shop();
        shop.setShopId(1L);
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryId(1L);
        product.setShop(shop);
        product.setProductId(1L);
        product.setProductCategory(productCategory);
        product.setProductName("正式的商品");
        product.setProductDesc("正式的商品");
        //创建缩略图文件流
        File thumbnailFile = new File("C:/Users/1/Desktop/QQ.jpg");
        InputStream inputStream = new FileInputStream(thumbnailFile);
        ImageHolder thumbnail = new ImageHolder(thumbnailFile.getName(), inputStream);
        //创建两个商品详情图文件并将他们添加到商品详情图列表中
        File productImg1 = new File("C:/Users/1/Desktop/pp.jpg");
        File productImg2 = new File("C:/Users/1/Desktop/house.jpg");
        InputStream stream1 = new FileInputStream(productImg1);
        InputStream stream2 = new FileInputStream(productImg2);
        List<ImageHolder> productImgList = new ArrayList<>();
        productImgList.add(new ImageHolder(productImg1.getName(), stream1));
        productImgList.add(new ImageHolder(productImg2.getName(), stream2));
        //验证
        ProductExecution productExecution = productService.modifyProduct(product, thumbnail, productImgList);
        System.out.println(productExecution);
    }

    @Test
    public void testGetProductById() {
        long productId = 1L;
        Product productById = productService.getProductById(productId);
        System.out.println(productById);
    }
}