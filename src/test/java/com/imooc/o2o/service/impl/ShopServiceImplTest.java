package com.imooc.o2o.service.impl;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.imooc.o2o.BaseTest;
import com.imooc.o2o.dao.ShopDao;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.exception.ShopOperationException;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.ImageUtil;
import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

public class ShopServiceImplTest extends BaseTest {

    @Autowired
    private ShopService shopService;


    @Test
    public void testGetShopList(){
        Shop shopCondition = new Shop();
        ShopCategory shopCategory = new ShopCategory();
        shopCategory.setShopCategoryId(1L);
        shopCondition.setShopCategory(shopCategory);
        ShopExecution shopExecution = shopService.getShopList(shopCondition, 1, 3);
        System.out.println(shopExecution);
    }

    @Test
    public void testAddShop() throws FileNotFoundException {
        Area area = new Area();
        area.setAreaId(2);

        PersonInfo owner = new PersonInfo();
        owner.setUserId(1L);

        ShopCategory shopCategory = new ShopCategory();
        shopCategory.setShopCategoryId(1L);

        Shop shop = new Shop(1L,"serviceTest","serviceTest","serviceTest","serviceTest",
                "serviceTest",1,new Date(),new Date(), ShopStateEnum.CHECK.getState(),"审核中",area,owner,shopCategory);

        File shopImg = new File("C:/Users/1/Desktop/car.jpg");
        InputStream shopImgInputStream = new FileInputStream(shopImg); //此处确保文件路径存在，因此直接将异常抛出不需要进行try-catch处理
        ShopExecution shopExecution = shopService.addShop(shop, shopImgInputStream, shopImg.getName());
        System.out.println(shopExecution);
    }

    @Test
    public void testModifyShop()throws ShopOperationException, FileNotFoundException {
        Shop shop = new Shop();
        shop.setShopId(1L);
        shop.setShopName("修改后的店铺名称");
        File shopImg = new File("C:/Users/1/Desktop/QQ.jpg");
        InputStream is = new FileInputStream(shopImg);
        ShopExecution shopExecution = shopService.modifyShop(shop, is, "QQ.jpg");
        System.out.println(shopExecution);
    }
}