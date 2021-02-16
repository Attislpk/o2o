package com.imooc.o2o.dao;

import com.imooc.o2o.entity.ProductImg;

import java.util.List;


public interface ProductImgDao {

	List<ProductImg> queryProductImgList(long productId);

	/**
	 * 批量添加图片
	 * @param productImgList
	 * @return
	 */
	int batchInsertProductImg(List<ProductImg> productImgList);

	/**
	 * 删除指定商品下的所有详情图，为新上传的商品详情图做准备
	 * @param productId  商品id
	 * @return
	 */
	int deleteProductImgByProductId(long productId);
}
