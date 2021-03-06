package com.imooc.o2o.enums;

import redis.clients.jedis.ShardedJedisPipeline;

public enum ShopStateEnum {
    CHECK(0,"审核中"),
    OFFLINE(-1,"非法店铺"),
    SUCCESS(1,"操作成功"),
    PASS(2,"通过认证"),
    INNER_ERROR(-1001,"系统内部错误"),
    NULL_SHOPID(-1002,"shopId为空"),
    MISS_MESSAGE(-1003,"shop信息不完整");
    private int state;
    private String stateInfo;

    //私有构造器
    private ShopStateEnum(int state, String stateInfo){
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    //根据state获取stateEnum对象
    public static ShopStateEnum stateOf(int state){
        for (ShopStateEnum stateEnum:values()){
            if (stateEnum.getState() == state){
                return stateEnum;
            }
        }
        return null;
    }
}
