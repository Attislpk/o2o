package com.imooc.o2o.entity;

import lombok.Data;

import java.util.Date;

@Data
public class WechatAuth {
    private long wechatAuthId;
    private String openId;
    private Date createTime;
    //关联用户id
    private PersonInfo personInfo;
}
