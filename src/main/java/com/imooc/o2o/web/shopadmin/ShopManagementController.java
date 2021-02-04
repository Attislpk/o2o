package com.imooc.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.exception.ShopOperationException;
import com.imooc.o2o.service.AreaService;
import com.imooc.o2o.service.ShopCategoryService;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.service.impl.ShopServiceImpl;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/shopadmin")
public class ShopManagementController {

    @Autowired
    private ShopService shopService;

    @Autowired
    private ShopCategoryService shopCategoryService;

    @Autowired
    private AreaService areaService;

    @RequestMapping(value = "/getshopinitinfo",method = RequestMethod.GET)
    private Map<String,Object> getShopInitInfo(){
        Map<String, Object> modelMap = new HashMap<>();
        List<ShopCategory> shopCategoryList;
        List<Area> areaList;
        try {
            areaList = areaService.getAreaList();
            shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
            modelMap.put("areaList",areaList);
            modelMap.put("shopCategoryList",shopCategoryList);
            modelMap.put("success",true);
        }catch (Exception e){
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }


    @RequestMapping(value = "/registershop", method = RequestMethod.POST)
    private Map<String,Object> registerShop(HttpServletRequest httpServletRequest, String key){
        Map<String,Object> modelMap = new HashMap<>();
        //验证码比对
        if (!CodeUtil.checkVerifyCode(httpServletRequest)){
            modelMap.put("success",false);
            modelMap.put("errMsg","验证码输入错误");
            return modelMap;
        }

        //1.接受从前端传来的相关参数并转换成对应的POJO对象
        String shopStr = HttpServletRequestUtil.getString(httpServletRequest,"shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try {
            //public <T> T readValue(JsonParser p, Class<T> valueType)
            shop = mapper.readValue(shopStr, Shop.class); //将shopStr封装到Shop对象中,即该shop对象拥有了名称属性
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }
        CommonsMultipartFile shopImg;
        //上传的图片存放在ServletContext中
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(httpServletRequest.getServletContext());
        if (commonsMultipartResolver.isMultipart(httpServletRequest)){
            //将httpServletRequest转为MultipartServeletRequest， 输入文件流
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) httpServletRequest;
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","上传图片为空");
            return modelMap;
        }

        //2.注册店铺
        if (shop != null && shopImg != null){
            PersonInfo owner = new PersonInfo();
            //Session TODO
            owner.setUserId(1L);
            shop.setOwner(owner);
            ShopExecution se;
            try {
                se = shopService.addShop(shop,shopImg.getInputStream(),shopImg.getOriginalFilename());
                if (se.getState() == ShopStateEnum.CHECK.getState()){
                    modelMap.put("success",true);}
                else {
                    modelMap.put("success",false);
                    modelMap.put("errMsg",se.getStateInfo());
                }

            }catch (ShopOperationException e) {
                modelMap.put("success",false);
                modelMap.put("errMsg",e.toString());
            }catch (IOException e) {
                modelMap.put("success",false);
                modelMap.put("errMsg",e.toString());
            }
            //3.无论是否成功都返回modelMap
            return modelMap;
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","请输入店铺信息");
            return modelMap;
        }
    }

    //将CommonsMultipartFile转换为File的辅助方法  CommonsMultipartFile implements MultipartFile, Serializable
    private static void inputStreamToFile(InputStream ins, File file){
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = ins.read(buffer))!=-1){
                os.write(buffer,0,bytesRead);
            }
        }catch (Exception e){
            throw new RuntimeException("调用inputStreamToFile异常："+e.getMessage());
        }finally{
            try {
                if (os != null){
                    os.close();
                }
                if (ins != null){
                    ins.close();
                }
            }catch (IOException e){
                throw new RuntimeException("inputStream关闭异常："+e.getMessage());
            }
        }
    }
}
