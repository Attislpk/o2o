package com.imooc.o2o.service;

import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;

public interface ShopService {

    //传入InputStrem而不是file，可以降低commonsMultipartFile和file相互转换导致的异常，inputstream无法携带文件名，因此要单独传入
    ShopExecution addShop(Shop shop, InputStream shopImgInputStream, String fileName);

}

