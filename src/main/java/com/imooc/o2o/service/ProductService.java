package com.imooc.o2o.service;

import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.exception.ProductOperationException;
import org.springframework.web.servlet.tags.form.InputTag;

import java.io.InputStream;
import java.util.List;

public interface ProductService {

    /**
     * 商品的添加操作
     * @param product 商品
     * @param thumbnail 缩略图
     * @param productImgList 详情图列表
     * @return dto对象
     * @throws ProductOperationException
     */
    ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList) throws ProductOperationException;

    /**
     * 查询商品列表并分页， 可输入的条件包括：商品名（模糊查询），商品状态，店铺id，商品类别
     * @param productCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize);

    /**
     * 根据productId查询商品
     * @param productId
     * @return
     */
    Product getProductById(long productId);

    /**
     * 传入新的商品信息，缩略图信息，详情图列表对商品进行更新
     * @param product
     * @param thumbnail
     * @param productImgList
     * @return
     * @throws ProductOperationException
     */
    ProductExecution modifyProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList) throws ProductOperationException;

}
