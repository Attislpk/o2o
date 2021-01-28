package com.imooc.o2o.service;

import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.lang.reflect.Field;

public interface ShopService {

    ShopExecution addShop(Shop shop, File shopImg);

}

