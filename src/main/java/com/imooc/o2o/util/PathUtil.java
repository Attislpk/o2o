package com.imooc.o2o.util;

import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.enums.ShopStateEnum;

import java.io.File;
import java.nio.file.Path;
import java.util.Date;

/**
 * 对图片路径进行处理
 */
public class PathUtil {
    private static final String separator = System.getProperty("file.separator");

    /**
     * 获取不同操作系统下图片存放的根路径
     * @return 图片存放的根路径位置
     */
    public static String getImgBasePath(){
        String os = System.getProperty("os.name");
        String basePath = "";
        if (os.toLowerCase().startsWith("win")){
            basePath = "E:/JAVACODE/projectdev/image/";
        }else {
            basePath = "/home/pengkun/image/";
        }
        return basePath.replace("/",separator);
    }

    /**
     * 返回商家上传图片的相对路径
     * @param shopId 商家id
     * @return 图片上传路径
     */
    public static String getShopImagePath(long shopId){
        String imagePath = "upload/item/shop/"+shopId+"/";
        return imagePath.replace("/",separator);
    }
}
