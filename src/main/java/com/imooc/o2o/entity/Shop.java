package com.imooc.o2o.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Shop {
    private Long shopId;
    private String shopName;
    private String shopDesc;
    private String shopAddr;
    private String phone;
    private String shopImg;
    private Integer priority;
    private Date createTime;
    private Date lastEditTime;
    //-1不可用 0审核中 1可用
    private Integer status;
    //超级管理员给店家的提醒
    private String advice;
    private Area area;
    private PersonInfo owner;
    private ShopCategory shopCategory;
}
