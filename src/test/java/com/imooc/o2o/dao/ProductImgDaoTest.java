package com.imooc.o2o.dao;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.ProductImg;
import junit.framework.TestCase;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public class ProductImgDaoTest extends BaseTest {

    @Autowired
    private ProductImgDao productImgDao;


    @Test
    public void testBQueryProductImgList() {
        List<ProductImg> list = productImgDao.queryProductImgList(1L);
        for (ProductImg productImg : list) {
            System.out.println(productImg);
        }
    }

    @Test
    public void testABatchInsertProductImg() {
        ProductImg productImg1 = new ProductImg();
        productImg1.setImgAddr("图片1");
        productImg1.setImgDesc("测试1");
        productImg1.setPriority(1);
        productImg1.setCreateTime(new Date());
        productImg1.setProductId(1L);

        ProductImg productImg2 = new ProductImg();
        productImg2.setImgAddr("图片2");
        productImg2.setImgDesc("测试2");
        productImg2.setPriority(2);
        productImg2.setCreateTime(new Date());
        productImg2.setProductId(1L);

        List<ProductImg> list = new ArrayList<>();
        list.add(productImg1);
        list.add(productImg2);
        int effectNum = productImgDao.batchInsertProductImg(list);
        System.out.println(effectNum);

    }

    @Test

    public void testCDeleteProductImgByProductId() {
        long productId = 1L;
        int effectNum = productImgDao.deleteProductImgByProductId(productId);
        System.out.println(effectNum);
    }
}