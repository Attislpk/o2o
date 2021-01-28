package com.imooc.o2o.entity;

import lombok.Data;

import java.util.Date;

@Data
public class HeadLine {
    private Long lineId;
    private String lineName;
    private String lineLink;
    private String lineImg;
    private Integer priority;
    private Integer status;
    private Date createTime;
    private Date lastEditTime;
}
