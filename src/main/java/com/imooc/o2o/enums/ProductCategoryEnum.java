package com.imooc.o2o.enums;

public enum ProductCategoryEnum {

    SUCCESS(1,"操作成功"),
    INNER_ERROR(-1001,"添加失败"),
    EMPTY_LIST(-1002,"操作数小于1");

    private int state;
    private String stateInfo;

    //私有构造器
    private ProductCategoryEnum(int state, String stateInfo){
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
    public static ProductCategoryEnum stateOf(int state){
        for (ProductCategoryEnum stateEnum:values()){
            if (stateEnum.getState() == state){
                return stateEnum;
            }
        }
        return null;
    }
}
