package com.imooc.o2o.service;

import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.exception.ShopOperationException;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;

public interface ShopService {


    /**
     * 根据shopCondition分页返回相应店铺列表
     * @param shopCondition
     * @param pageIndex 第几页
     * @param pageSize 每页的记录条数
     * @return
     */
    public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize);


    /**
     * 根据shopId获取shop对象
     * @param ShopId shopId
     * @return 查询到的shop对象
     */
    Shop getByShopId(long ShopId);

    /**
     * 修改shop信息
     * @param shop 新的shop对象，携带需要修改的shop信息
     * @param shopImgInputStream 图片输入流
     * @param fileName 文件名
     * @return shopExecution对象，携带shop以及相关的操作状态信息
     * 修改店铺涉及事务处理，需要抛出运行时异常
     */
    ShopExecution modifyShop(Shop shop, ImageHolder thumbnail) throws ShopOperationException;

    //传入InputStrem而不是file，可以降低commonsMultipartFile和file相互转换导致的异常，inputstream只包括文件信息
    // 无法携带文件名，因此要单独传入   添加店铺涉及事务处理，需要抛出运行时异常
    ShopExecution addShop(Shop shop, ImageHolder thumbnail) throws ShopOperationException;

}

