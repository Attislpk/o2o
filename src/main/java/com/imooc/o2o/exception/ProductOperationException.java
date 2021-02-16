package com.imooc.o2o.exception;

public class ProductOperationException extends RuntimeException{

    public ProductOperationException(String errMsg){
        super(errMsg);
    }
}
