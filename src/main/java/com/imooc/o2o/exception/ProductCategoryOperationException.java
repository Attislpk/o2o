package com.imooc.o2o.exception;

public class ProductCategoryOperationException extends RuntimeException{

    public ProductCategoryOperationException(String errMsg){
        super(errMsg);
    }
}
