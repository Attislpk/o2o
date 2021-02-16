package com.imooc.o2o.web.shopadmin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ProductStateEnum;
import com.imooc.o2o.exception.ProductOperationException;
import com.imooc.o2o.service.ProductCategoryService;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/shopadmin")
public class ProductManagementController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductCategoryService productCategoryService;

    private static final int IMAGEMAXCOUNT = 6;


    @RequestMapping(value = "/addproduct", method = RequestMethod.POST)
    public Map<String, Object> addProduct(HttpServletRequest request) {
        //验证码校验
        Map<String, Object> modelMap = CodeUtil.codeVerify(request);

        //前端接受变量的初始化信息
        String productStr = HttpServletRequestUtil.getString(request, "productStr");
        ObjectMapper mapper = new ObjectMapper();
        Product product = null;
        MultipartHttpServletRequest multipartHttpServletRequest = null;
        ImageHolder thumbnail = null;
        List<ImageHolder> productImgList = new ArrayList<ImageHolder>();
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getServletContext());
        try {
            //若请求中存在文件流，则取出相关的文件(缩略图和详情图)
            if (multipartResolver.isMultipart(request)) {
                thumbnail = imageHandler((MultipartHttpServletRequest) request, thumbnail, productImgList);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "上传图片不能为空");
            }
        } catch (IOException e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
        }
        try {
            //尝试获取从前端传过来的表单string流并将其转换为Product实体类
            product = mapper.readValue(productStr, Product.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
        }
        //若product信息，缩略图and详情图列表非空，则开始进行商品添加操作
        if (product != null && thumbnail != null && productImgList.size() > 0) {
            try {
                //从session中获取当前店铺的id并赋值给product，减少对前端数据的依赖
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                product.setShop(currentShop);
                //执行添加操作
                ProductExecution productExecution = productService.addProduct(product, thumbnail, productImgList);
                if (productExecution.getState() == ProductStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", productExecution.getStateInfo());
                }
            } catch (ProductOperationException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入商品信息");
        }
        return modelMap;
    }

    /**
     * 用于对上传图片进行处理的方法
     * @param request 请求
     * @param thumbnail 缩略图
     * @param productImgList 详情图列表
     * @return
     * @throws IOException
     */
    private ImageHolder imageHandler(MultipartHttpServletRequest request, ImageHolder thumbnail, List<ImageHolder> productImgList) throws IOException {
        MultipartHttpServletRequest multipartHttpServletRequest;
        multipartHttpServletRequest = request;
        //取出缩略图并创建ImageHolder
        CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartHttpServletRequest.getFile("thumbnail");
        thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(), thumbnailFile.getInputStream());
        //取出详情图列表并构建List<ImageHolder>对象，最多支持六张图片上传
        for (int i = 0; i < IMAGEMAXCOUNT; i++) {
            //获取request中的图片对象
            CommonsMultipartFile productImgFile = (CommonsMultipartFile) multipartHttpServletRequest.getFile("productImg" + i);
            if (productImgFile != null) {
                //如果第i个图片详情流不为空，则加入List
                ImageHolder imageHolder = new ImageHolder(productImgFile.getOriginalFilename(), productImgFile.getInputStream());
                productImgList.add(imageHolder);
            } else {
                break;
            }
        }
        return thumbnail;
    }

    @RequestMapping(value = "/getproductbyid",method = RequestMethod.GET)
    private Map<String,Object> getProductById(@RequestParam Long productId){ //@RequestParam表示该参数必须填入
        Map<String,Object> modelMap = new HashMap<>();
        //非空判断
        if (productId > -1){
            //获取商品信息
            Product product = productService.getProductById(productId);
            //获取该店铺下的商品类别列表
            List<ProductCategory> productCategoryList = productCategoryService.getProductCategoryList(product.getShop().getShopId());
            modelMap.put("product",product);
            modelMap.put("productCategoryList",productCategoryList);
            modelMap.put("success",true);
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","empty productId");
        }
        return modelMap;
    }


    @RequestMapping(value = "/modifyproduct", method = RequestMethod.POST)
    private Map<String,Object> modifyProduct(HttpServletRequest request){
        //判断是商品编辑时调用还是上下架时调用，如果是前者需要进行验证码判断，后者无需进行验证码判断
        boolean statusChange = HttpServletRequestUtil.getBoolean(request,"statusChange");
        Map<String,Object> modelMap = new HashMap<>();
        ////如果是商品编辑(statusChange=false)，则需要进行验证码判断
        if (!statusChange){
            modelMap = CodeUtil.codeVerify(request);
        }
        //接受前端参数变量的初始值，包括商品，缩略图，详情图列表等实体类, 和addproduct中的逻辑相同
        ObjectMapper mapper = new ObjectMapper();
        Product product = null;
        ImageHolder thumbnail = null;
        List<ImageHolder> imageHolderList = new ArrayList<>();
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getServletContext());

        //如果请求中存在文件流，则取出相关的图片,对图片进行处理
        try {
            if (commonsMultipartResolver.isMultipart(request)){
                thumbnail = imageHandler((MultipartHttpServletRequest) request, thumbnail, imageHolderList);
            }
        } catch (IOException e) {
            modelMap.put("success",false);
            modelMap.put("errMsg",e.toString());
        }
        //对product实体类进行处理
        try {
            String productStr = HttpServletRequestUtil.getString(request, "productStr");
            //尝试获取前端传递过来的表单string流并将其转换为Product实体类
            product = mapper.readValue(productStr, Product.class);
        } catch (Exception e) {
            modelMap.put("success",false);
            modelMap.put("errMsg",e.toString());
        }
        //非空判断
        if (product!=null){
            try {
                //从session中获取当前店铺的id，从而减少对前端数据的依赖
                Shop shop = (Shop) request.getSession().getAttribute("currentShop");
                product.setShop(shop);
                ProductExecution productExecution = productService.modifyProduct(product, thumbnail, imageHolderList);
                if (productExecution.getState() ==ProductStateEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                }else {
                    modelMap.put("success",false);
                    modelMap.put("errMsg",productExecution.getStateInfo());
                }
            }catch (RuntimeException e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.toString());
            }
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","商品信息不能为空");
        }
        return modelMap;
    }
}
