package com.imooc.o2o.dto;

import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.enums.ProductCategoryEnum;
import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@ToString
public class ProductCategoryExecution implements Serializable {
    @Serial
    private static final long serialVersionUID = -5549178811586495538L;
    //结果状态
    private int state;

    //状态标识
    private String stateInfo;

    //返回List结果
    private List<ProductCategory> productCategoryList;

    public ProductCategoryExecution(){}

    //失败时执行的构造方法
    public ProductCategoryExecution(ProductCategoryEnum productCategoryEnum){
        this.state = productCategoryEnum.getState();
        this.stateInfo = productCategoryEnum.getStateInfo();
    }

    //成功时执行的构造方法
    public ProductCategoryExecution(ProductCategoryEnum productCategoryEnum, List<ProductCategory> productCategoryList){
        this.state = productCategoryEnum.getState();
        this.stateInfo = productCategoryEnum.getStateInfo();
        this.productCategoryList = productCategoryList;
    }
}
