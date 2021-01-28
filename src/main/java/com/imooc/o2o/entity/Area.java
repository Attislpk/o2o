package com.imooc.o2o.entity;

import lombok.Data;

import java.net.Inet4Address;
import java.util.Date;

@Data
public class Area {
    private Integer areaId;
    private String areaName;
    private Integer priority;
    private Date createTime;
    private Date lastEditTime;
}
