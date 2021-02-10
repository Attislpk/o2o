package com.imooc.o2o.web.shopadmin;

import com.imooc.o2o.dto.ProductCategoryExecution;
import com.imooc.o2o.dto.Result;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ProductCategoryEnum;
import com.imooc.o2o.exception.ProductCategoryOperationException;
import com.imooc.o2o.service.ProductCategoryService;
import org.apache.ibatis.annotations.ResultType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/shopadmin")
public class ProductCategoryManagementController {
    private Logger logger = LoggerFactory.getLogger(ProductCategoryManagementController.class);

    @Autowired
    private ProductCategoryService productCategoryService;

    @RequestMapping(value = "/getproductcategorylist", method = RequestMethod.GET)
    private Result<List<ProductCategory>> getProductCategoryList(HttpServletRequest request){

        //能从session中取出shop对象
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        logger.debug("currentShop信息:"+currentShop);
        if (currentShop != null && currentShop.getShopId() > 0){
            List<ProductCategory> productCategoryList = productCategoryService.getProductCategoryList(currentShop.getShopId());
            return new Result<>(true,productCategoryList);
        }else {
            return new Result<List<ProductCategory>>(false, ProductCategoryEnum.INNER_ERROR.getStateInfo(), ProductCategoryEnum.INNER_ERROR.getState());
        }
    }

    @RequestMapping(value = "/addproductcategorys", method = RequestMethod.POST)
    private Map<String,Object> addProductCategorys(@RequestBody List<ProductCategory> productCategoryList, HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        for (ProductCategory pc : productCategoryList){
            //为所有的productrCategory设置session中取得的同一个shopId
            pc.setShopId(currentShop.getShopId());
        }if (productCategoryList != null && productCategoryList.size() > 0){
            try {
                ProductCategoryExecution productCategoryExecution = productCategoryService.batchAddProductCategory(productCategoryList);
                if (productCategoryExecution.getState() == ProductCategoryEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                }else {
                    modelMap.put("success",false);
                    modelMap.put("errMsg",productCategoryExecution.getStateInfo());
                }
            }catch (ProductCategoryOperationException e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());
            }
        }
        return modelMap;
    }

    @RequestMapping(value = "/removeproductcategory", method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> removeProductCategory(Long productCategoryId,HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        if (productCategoryId != null && productCategoryId > 0){
            try {
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                ProductCategoryExecution productCategoryExecution = productCategoryService.deleteProductCategory(productCategoryId, currentShop.getShopId());
                if (productCategoryExecution.getState() == ProductCategoryEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                }else {
                    modelMap.put("success",false);
                    modelMap.put("errMsg", productCategoryExecution.getStateInfo());
                }
            }catch (RuntimeException e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.toString());
            }
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","请至少选择一个商品类别");
        }
        return modelMap;
    }
}
