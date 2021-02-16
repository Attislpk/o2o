package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.ProductDao;
import com.imooc.o2o.dao.ProductImgDao;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductImg;
import com.imooc.o2o.enums.ProductStateEnum;
import com.imooc.o2o.exception.ProductOperationException;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductImgDao productImgDao;

    /**
     * 1. 处理缩略图，获取缩略图的相对路径并赋值给product
     * 2. 向tb_product中写入商品信息，获取productId
     * 3. 结合productId批量处理商品详情图
     * 4. 将商品详情图列表批量插入tb_product_img中
     */
    @Override
    @Transactional
    public ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgHolderList) throws ProductOperationException {
        //空值判断
        if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
            //给商品设置默认属性
            product.setCreateTime(new Date());
            product.setLastEditTime(new Date());
            //默认为上架状态
            product.setStatus(1);
            //1.如果商品缩略图不为空,则获取缩略图的相对路径并赋值给product
            if (thumbnail != null) {
                addThumbnail(product, thumbnail);
            }
            try {
                //2.向tb_product中写入商品信息，获取productId(useGeneratedKey自动生成)
                //创建商品信息
                int effectNum = productDao.insertProduct(product);
                if (effectNum <= 0) {
                    throw new ProductOperationException("商品创建失败哦");
                }
            } catch (Exception e) {
                throw new ProductOperationException("商品创建失败！ " + e.getMessage());
            }
            //3.如果商品详情图不为空，则向该商品中添加商品详情图
            if (productImgHolderList != null && productImgHolderList.size() > 0) {
                addProductImgList(product, productImgHolderList);
            }
            return new ProductExecution(ProductStateEnum.SUCCESS, product);
        } else {
            return new ProductExecution(ProductStateEnum.EMPTY);
        }
    }

    @Override
    public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
        return null;
    }

    @Override
    public Product getProductById(long productId) {
        return productDao.queryProductByProductId(productId);
    }

    @Override
    /**
     * 1.若缩略图参数有值，则先处理缩略图，如果本来就存在缩略图，则需要先删除缩略图再添加新图，之后获取缩略图的相对路径赋值给product
     * 2.如果商品详情图列表存在，则进行相同的操作
     * 3.将tb_product_img下该商品的原商品详情图记录全部清除
     * 4.更新tb_product的信息
     */
    public ProductExecution modifyProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList) throws ProductOperationException {
        //1。空值判断
        if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
            //更新处理时间
            product.setLastEditTime(new Date());
            //如果有新上传的缩略图，则先删除原缩略图再添加
            if (thumbnail != null) {
                Product originalProduct = productDao.queryProductByProductId(product.getProductId());
                if (originalProduct.getImgAddr() != null) {
                    //删除原缩略图
                    ImageUtil.deletePath(originalProduct.getImgAddr());
                }
                //添加新的缩略图+
                addThumbnail(product, thumbnail);
            }
            //对详情图进行同样的操作
            if (productImgList != null && productImgList.size() > 0) {
                deleteProductImgList(product.getProductId());
                addProductImgList(product, productImgList);
            }
            try {
                int effectNum = productDao.updateProduct(product);
                if (effectNum <= 0) {
                    throw new ProductOperationException("商品信息更新失败");
                }
                return new ProductExecution(ProductStateEnum.SUCCESS, product);
            } catch (Exception e) {
                throw new ProductOperationException("商品信息更新失败:" + e.toString());
            }
        } else {
            return new ProductExecution(ProductStateEnum.EMPTY);
        }
    }

    /**
     * 批量将商品和商品详情图进行绑定，详情图的位置存放在tb_product_img表中
     *
     * @param product              商品
     * @param productImgHolderList 商品详情图list
     */
    private void addProductImgList(Product product, List<ImageHolder> productImgHolderList) {
        //获取图片存储路径，此处直接存放到店铺的文件夹下
        String shopImagePath = PathUtil.getShopImagePath(product.getShop().getShopId());
        List<ProductImg> productImgList = new ArrayList<>();
        //遍历详情图，并添加到productImg实体类中, 一个图片对应一个productImg对象
        for (ImageHolder imageHolder : productImgHolderList) {
            String imgAddr = ImageUtil.generateNormalImg(imageHolder, shopImagePath);
            ProductImg productImg = new ProductImg();
            productImg.setImgAddr(imgAddr);
            productImg.setProductId(product.getProductId());
            productImg.setCreateTime(new Date());
            productImgList.add(productImg);
        }
        //将productImgList中的信息插入到tb_product_img表中
        if (productImgList.size() > 0) {
            try {
                int effectNum = productImgDao.batchInsertProductImg(productImgList);
                if (effectNum <= 0) {
                    throw new ProductOperationException("创建商品详情图失败");
                }
            } catch (Exception e) {
                throw new ProductOperationException("创建商品详情图失败" + e.toString());
            }
        }

    }

    /**
     * 商品绑定缩略图的方法, 缩略图的地址存放在tb_product表中
     *
     * @param product   商品
     * @param thumbnail 缩略图
     */
    private void addThumbnail(Product product, ImageHolder thumbnail) {
        String shopImagePath = PathUtil.getShopImagePath(product.getShop().getShopId());
        String thumbnailAddr = ImageUtil.generateThumbnail(thumbnail, shopImagePath);
        product.setImgAddr(thumbnailAddr);
    }


    /**
     * 删除某个商品下的所有详情图
     *
     * @param productId
     */
    private void deleteProductImgList(Long productId) {
        List<ProductImg> productImgList = productImgDao.queryProductImgList(productId);
        //删除原product下的productImgList
        for (ProductImg productImg : productImgList) {
            ImageUtil.deletePath(productImg.getImgAddr());
        }
        //删除tb_product_img表中的图片信息
        productImgDao.deleteProductImgByProductId(productId);
    }
}
