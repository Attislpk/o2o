package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.ShopDao;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.exception.ShopOperationException;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PathUtil;
import com.mysql.cj.log.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
@Service
public class ShopServiceImpl implements ShopService{
    private static final Logger logger = LoggerFactory.getLogger(ShopServiceImpl.class);

    @Autowired
    private ShopDao shopDao;

    //添加shop需要保证shop表中数据的插入和img的插入是同时完成的，出现运行时异常则需要回滚    添加店铺信息+添加图片信息+更新图片信息
    @Transactional
    @Override
    public ShopExecution addShop(Shop shop, File shopImg) {
        //空值判断
        if (shop == null){
            //返回执行失败的shopExecution
            return new ShopExecution(ShopStateEnum.MISS_MESSAGE);
        }
        try {
            //店铺信息赋初值
            shop.setStatus(0);
            shop.setCreateTime(new Date());
            shop.setLastEditTime(new Date());
            //添加店铺信息
            int effectNum = shopDao.insertShop(shop);
            logger.info(String.valueOf(effectNum));
            //添加失败
            if (effectNum <= 0){
                throw new ShopOperationException("add shop error!");
            }else {
                if (shopImg!=null){
                    //存储图片
                    try {
                        //将shop和shopImg绑定起来, 可能抛出异常
                        addShopImg(shop,shopImg);
                    }catch (Exception e){
                        throw new ShopOperationException("add shopImg error:"+e.getMessage());
                    }
                    //更新店铺的图片地址, 上述try块中已经完成了shop和img的图片绑定，此处进行更新
                    effectNum = shopDao.updateShop(shop);
                    if (effectNum <= 0){
                        throw new ShopOperationException("图片地址更新失败");
                    }
                }
            }
        }catch (Exception e){
            throw new ShopOperationException("addShop error:" + e.getMessage());
        }
        return new ShopExecution(ShopStateEnum.CHECK,shop);
    }

    /**
     * 将shop对象和shopImg进行绑定
     * @param shop shop对象
     * @param shopImg shopImg，图片存放的地址
     */
    private void addShopImg(Shop shop, File shopImg) {
        //img的目标路径
        String destPath = PathUtil.getShopImagePath(shop.getShopId()); //"/upload/item/shop/"+shopId+"/"
        //获取img的相对路径
        logger.debug("destPath:"+destPath);
        String shopImgAddr = ImageUtil.generateThumbnail(shopImg,destPath); //destPath + realFileName + extension;
        //设置shop图片的相对路径
        shop.setShopImg(shopImgAddr); //相对路径+imgname.jpg
    }
}
