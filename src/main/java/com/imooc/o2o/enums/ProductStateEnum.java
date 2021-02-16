package com.imooc.o2o.enums;

public enum ProductStateEnum {

    SUCCESS(1,"操作成功"),
    EMPTY(-1002,"商品为空");

    private int state;
    private String stateInfo;

    //私有构造器
    private ProductStateEnum(int state, String stateInfo){
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
    public static ProductStateEnum stateOf(int state){
        for (ProductStateEnum stateEnum:values()){
            if (stateEnum.getState() == state){
                return stateEnum;
            }
        }
        return null;
    }
}
