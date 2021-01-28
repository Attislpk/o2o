package com.imooc.o2o.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Product {
    private Long productId;
    private String productName;
    private String productDesc;

    //缩略图
    private String imgAddr;
    private String normalPrice;
    private String promotionPrice;
    private Integer priority;
    private Date createTime;
    private Date lastEditTime;
    private Integer status;

    private List<ProductImg> productImgList;
    private ProductCategory productCategory;
    private Shop shop;

}
