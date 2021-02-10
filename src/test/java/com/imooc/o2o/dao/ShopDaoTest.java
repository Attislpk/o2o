package com.imooc.o2o.dao;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.dao.ShopDao;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.Test;
import java.util.Date;
import java.util.List;

public class ShopDaoTest extends BaseTest {

    @Autowired
    private ShopDao shopDao;



    @Test
    @Ignore
    public void testqueryShopListAndCount(){
        Shop shopCondition = new Shop();
        PersonInfo owner = new PersonInfo();
        owner.setUserId(1L);
        shopCondition.setOwner(owner);
        List<Shop> shops = shopDao.queryShopList(shopCondition, 0, 3);
        for (Shop shop : shops) {
            System.out.println(shop);
        }

        int count = shopDao.queryShopCount(shopCondition);
        System.out.println("店铺列表总数:"+count);

    }



    @Test
    @Ignore
    public void testQueryByShopId(){
        long shopId = 1;
        Shop shop = shopDao.queryByShopId(shopId);
        System.out.println(shop);
    }


    @Test
    @Ignore
    public void testInsertShop() {
        Area area = new Area();
        area.setAreaId(2);

        PersonInfo owner = new PersonInfo();
        owner.setUserId(1L);

        ShopCategory shopCategory = new ShopCategory();
        shopCategory.setShopCategoryId(1L);

        Shop shop = new Shop(1L,"test","test","test","test",
                "test",1,new Date(),new Date(),1,"审核中",area,owner,shopCategory);
        int effectNum = shopDao.insertShop(shop);
        System.out.println(effectNum);
    }

    @Test
    @Ignore
    public void testUpdateShop(){
        Shop shop = new Shop();
        shop.setShopId(1L);
        shop.setShopDesc("修改描述");
        shop.setAdvice("审核通过");
        shop.setLastEditTime(new Date());
        int effectNum = shopDao.updateShop(shop);
        System.out.println(effectNum);
    }
}