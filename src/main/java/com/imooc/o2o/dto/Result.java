package com.imooc.o2o.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 对shopExecution的改进，可以存放任意类型的数据
 * @param <T>
 */
@NoArgsConstructor
@Data
public class Result <T>{

    //是否成功
    private boolean success;

    //存放的数据
    private T data;

    //错误信息
    private String errMsg;

    //errorCode
    private int errorCode;


    //success时的构造器
    public Result(boolean success, T data){
        this.success = success;
        this.data = data;
    }

    //success=false时的构造器
    public Result(boolean success, String errMsg, int errorCode){
        this.success = success;
        this.errMsg = errMsg;
        this.errorCode = errorCode;
    }


}
