package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.ShopDao;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.exception.ShopOperationException;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PageToRowIndexUtil;
import com.imooc.o2o.util.PathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

@Service
public class ShopServiceImpl implements ShopService{
    private static final Logger logger = LoggerFactory.getLogger(ShopServiceImpl.class);

    @Autowired
    private ShopDao shopDao;

    @Override
    public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
        int rowIndex = PageToRowIndexUtil.pageToRow(pageIndex, pageSize);
        List<Shop> shops = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
        int count = shopDao.queryShopCount(shopCondition);
        ShopExecution shopExecution = new ShopExecution();
        if (shops != null){
            shopExecution.setShopList(shops);
            shopExecution.setCount(count);
        }else {
            shopExecution.setState(ShopStateEnum.INNER_ERROR.getState());
        }
        return shopExecution;
    }

    @Override
    public Shop getByShopId(long shopId) {
         return shopDao.queryByShopId(shopId);
    }

    /**
     *
     * @param shop 新的shop对象，携带需要修改的shop信息
     * @param shopImgInputStream 图片输入流
     * @param fileName 文件名
     * @return
     * @throws ShopOperationException
     */
    @Override
    @Transactional
    public ShopExecution modifyShop(Shop shop, InputStream shopImgInputStream, String fileName) throws ShopOperationException{
       if (shop == null || shop.getShopId() == null){
           return new ShopExecution(ShopStateEnum.NULL_SHOPID);
       }else {
           try {
               //1.判断是否需要处理图片
               if (shopImgInputStream != null && fileName != null && !"".equals(fileName)){
                   //确保每次获取的shop信息都是最新的  oldShop是原shop
                   Shop oldShop = shopDao.queryByShopId(shop.getShopId());
                   if (oldShop.getShopImg() != null){
                       ImageUtil.deletePath(oldShop.getShopImg());
                   }
               }
               //更新shop的图片信息
               addShopImg(shop,shopImgInputStream,fileName);

               //2.更新店铺其他信息
               shop.setLastEditTime(new Date());
               int effectNum = shopDao.updateShop(shop);
               if (effectNum <= 0){
                   return new ShopExecution(ShopStateEnum.INNER_ERROR);
               }else {
                   //查询出最新的shop对象，返回到shopExecution中
                   shop = shopDao.queryByShopId(shop.getShopId());
                   return new ShopExecution(ShopStateEnum.SUCCESS,shop);
               }
           }catch (Exception e){
               throw new ShopOperationException("shop信息修改失败："+e.getMessage());
           }
       }
    }

    //添加shop需要保证shop表中数据的插入和img的插入是同时完成的，出现运行时异常则需要回滚    添加店铺信息+添加图片信息+更新图片信息
    @Transactional
    @Override
    public ShopExecution addShop(Shop shop, InputStream shopImgInputStream, String fileName) throws ShopOperationException{
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
                if (shopImgInputStream !=null){
                    //存储图片
                    try {
                        //将shop和shopImg绑定起来, 可能抛出异常
                        addShopImg(shop, shopImgInputStream, fileName);
                    }catch (Exception e){
                        throw new ShopOperationException("add shopImgInputStream error:"+e.getMessage());
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
     * @param  shopImgInputStream，图片输入流
     */
    private void addShopImg(Shop shop, InputStream shopImgInputStream, String fileName) {
        //img的目标路径
        String destPath = PathUtil.getShopImagePath(shop.getShopId()); //"/upload/item/shop/"+shopId+"/"
        //获取img的相对路径
        logger.debug("destPath:"+destPath);
        String shopImgAddr = ImageUtil.generateThumbnail(shopImgInputStream,destPath,fileName); //destPath + realFileName + extension;
        //设置shop图片的相对路径
        shop.setShopImg(shopImgAddr); //相对路径+imgname.jpg
    }
}
