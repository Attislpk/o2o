package com.imooc.o2o.dto;

import com.imooc.o2o.entity.Product;
import com.imooc.o2o.enums.ProductStateEnum;
import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@ToString
public class ProductExecution implements Serializable {

    @Serial
    private static final long serialVersionUID = -532698506218862734L;
    //结果状态
    private int state;

    //状态标识
    private String stateInfo;

    //操作的是商品  增删改商品时用
    private Product product;

    //返回List结果  查询商品列表时使用
    private List<Product> productList;

    //商品数量
    private int count;

    public ProductExecution(){}

    //失败时执行的构造方法
    public ProductExecution(ProductStateEnum productStateEnum){
        this.state = productStateEnum.getState();
        this.stateInfo = productStateEnum.getStateInfo();
    }

    //成功时执行的构造方法 单个商品
    public ProductExecution(ProductStateEnum productStateEnum, Product product){
        this.state = productStateEnum.getState();
        this.stateInfo = productStateEnum.getStateInfo();
        this.product = product;
    }

    //成功时执行的构造方法  商品列表
    public ProductExecution(ProductStateEnum productStateEnum, List<Product> productList){
        this.state = productStateEnum.getState();
        this.stateInfo = productStateEnum.getStateInfo();
        this.productList = productList;
    }
}
