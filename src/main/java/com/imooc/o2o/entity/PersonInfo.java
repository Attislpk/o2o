package com.imooc.o2o.entity;

import lombok.Data;

import java.util.Date;

@Data
public class PersonInfo {
    private Long userId;
    private String userName;
    private String profileImg;
    private String email;
    private String gender;
    //0-禁用 1-启用
    private Integer status;
    //0-顾客，1-商家，2-超级管理员
    private Integer userType;
    private Date createTime;
    private Date lastEditTime;
}
