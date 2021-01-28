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
import com.imooc.o2o.service.ShopService;
import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.util.Date;

public class ShopServiceImplTest extends BaseTest {

    @Autowired
    private ShopService shopService;

    @Test
    public void testAddShop() {
        Area area = new Area();
        area.setAreaId(2);

        PersonInfo owner = new PersonInfo();
        owner.setUserId(1L);

        ShopCategory shopCategory = new ShopCategory();
        shopCategory.setShopCategoryId(1L);

        Shop shop = new Shop(1L,"serviceTest","serviceTest","serviceTest","serviceTest",
                "serviceTest",1,new Date(),new Date(), ShopStateEnum.CHECK.getState(),"审核中",area,owner,shopCategory);

        File shopImg = new File("C:/Users/1/Desktop/car.jpg");

        ShopExecution shopExecution = shopService.addShop(shop, shopImg);
        System.out.println(shopExecution);

    }
}