package com.imooc.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.ImageHolder;
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
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/shopadmin")
public class ShopManagementController {
    private final Logger logger = LoggerFactory.getLogger(ProductCategoryManagementController.class);

    @Autowired
    private ShopService shopService;

    @Autowired
    private ShopCategoryService shopCategoryService;

    @Autowired
    private AreaService areaService;



    //如果用户直接访问商品界面，判断用户访问的合法性
    @RequestMapping(value = "/getshopmanagementinfo",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> getShopManagementInfo(HttpServletRequest servletRequest){
        Map<String,Object> modelMap = new HashMap<>();
        long shopId = HttpServletRequestUtil.getLong(servletRequest, "shopId");
        if (shopId <= 0){
            //如果没有传递shopId信息，尝试从request的session中查找出shop信息(非法访问/首次访问)
            Object currentShopObj = servletRequest.getSession().getAttribute("currentShop");
            if (currentShopObj == null){
                //如果session中不存在shop信息，说明是非法访问
                modelMap.put("redirect",true);
                modelMap.put("url","/o2o/shopadmin/shoplist");
            }else {
                //session中存在shop信息，是合法访问
                Shop currentShop = (Shop) currentShopObj;
                modelMap.put("redirect",false);
                modelMap.put("shopId",currentShop.getShopId());
            }
        }else {
            //request携带了shopId信息(非首次访问), 将shopId存放入session中
            Shop currentShop = new Shop();
            currentShop.setShopId(shopId);
            //在session中存入currentShop信息
            servletRequest.getSession().setAttribute("currentShop",currentShop);
            modelMap.put("redirect",false);
        }
        return modelMap;
    }

    //获取店铺列表shoplist
    @RequestMapping(value = "/getshoplist", method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> getShopList(HttpServletRequest servletRequest){
        Map<String,Object> modelMap = new HashMap<>();
        PersonInfo user = new PersonInfo();
        user.setUserId(1L);
        user.setUserName("test");
        servletRequest.getSession().setAttribute("user",user);
        user = (PersonInfo) servletRequest.getSession().getAttribute("user");
        logger.debug("当前session中的owner信息："+servletRequest.getSession().getAttribute("user"));
        try {
            Shop shopCondition = new Shop();
            shopCondition.setOwner(user);
            ShopExecution shopExecution = shopService.getShopList(shopCondition, 0, 100);
            modelMap.put("shopList",shopExecution.getShopList());
            modelMap.put("user",user);
            modelMap.put("success",true);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());

        }
        return modelMap;
    }


    @RequestMapping(value = "/getshopinitinfo", method = RequestMethod.GET)
    private Map<String, Object> getShopInitInfo() {
        Map<String, Object> modelMap = new HashMap<>();
        List<ShopCategory> shopCategoryList;
        List<Area> areaList;
        try {
            areaList = areaService.getAreaList();
            shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
            modelMap.put("areaList", areaList);
            modelMap.put("shopCategoryList", shopCategoryList);
            modelMap.put("success", true);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }


    @RequestMapping(value = "/registershop", method = RequestMethod.POST)
    private Map<String, Object> registerShop(HttpServletRequest httpServletRequest) throws IOException {
        //验证码比对, 并封装对象
        Map<String, Object> modelMap = CodeUtil.codeVerify(httpServletRequest);

        //1.接受从前端传来的相关参数并转换成对应的POJO对象
        String shopStr = HttpServletRequestUtil.getString(httpServletRequest, "shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try {
            //public <T> T readValue(JsonParser p, Class<T> valueType)
            shop = mapper.readValue(shopStr, Shop.class); //将shopStr封装到Shop对象中,即该shop对象拥有了名称属性
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        CommonsMultipartFile shopImg;
        //上传的图片存放在ServletContext中
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(httpServletRequest.getServletContext());
        if (commonsMultipartResolver.isMultipart(httpServletRequest)) {
            //将httpServletRequest转为MultipartServeletRequest， 输入文件流
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) httpServletRequest;
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "上传图片为空");
            return modelMap;
        }

        //2.注册店铺
        if (shop != null && shopImg != null) {
            //使用Session获取/设置用户信息
            PersonInfo owner = (PersonInfo) httpServletRequest.getSession().getAttribute("user");
            shop.setOwner(owner);
            ShopExecution shopExecution;
            ImageHolder imageHolder = new ImageHolder(shopImg.getOriginalFilename(),shopImg.getInputStream());
            try {
                shopExecution = shopService.addShop(shop,imageHolder);
                if (shopExecution.getState() == ShopStateEnum.CHECK.getState()) {
                    modelMap.put("success", true);
                    //店铺和用户的关系是多对一，因此ssesion中还需要存储该用户相关的店铺信息  当店铺添加成功后:
                    List<Shop> shopList = (List<Shop>) httpServletRequest.getSession().getAttribute("shopList");
                        //如果是首次创建店铺
                    if (shopList == null || shopList.size() == 0){
                        shopList = new ArrayList<Shop>();
                    }
                    shopList.add(shopExecution.getShop());
                    //将该shopList存入session中
                    httpServletRequest.getSession().setAttribute("shopList",shopList);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", shopExecution.getStateInfo());
                }

            } catch (ShopOperationException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
            }
            //3.无论是否成功都返回modelMap
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入店铺信息");
            return modelMap;
        }
    }

    @RequestMapping(value = "/getshopbyid", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopById(HttpServletRequest request) {
        HashMap<String, Object> modelMap = new HashMap<>();
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        if (shopId > -1) {
            try {
                Shop shop = shopService.getByShopId(shopId);
                List<Area> areaList = areaService.getAreaList();
                modelMap.put("shop", shop);
                modelMap.put("areaList", areaList);
                modelMap.put("success", true);
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty shopId");
        }
        return modelMap;
    }

    @RequestMapping(value = "/modifyshop", method = RequestMethod.POST)
    private Map<String, Object> modifyshop(HttpServletRequest httpServletRequest) throws IOException {
        Map<String, Object> modelMap = CodeUtil.codeVerify(httpServletRequest);

        //1.接受从前端传来的相关参数并转换成对应的POJO对象
        String shopStr = HttpServletRequestUtil.getString(httpServletRequest, "shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try {
            //public <T> T readValue(JsonParser p, Class<T> valueType)
            shop = mapper.readValue(shopStr, Shop.class); //将shopStr封装到Shop对象中,即该shop对象拥有了名称属性
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        
        CommonsMultipartFile shopImg = null;
        //上传的图片存放在ServletContext中
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(httpServletRequest.getServletContext());
        if (commonsMultipartResolver.isMultipart(httpServletRequest)) {
            //将httpServletRequest转为MultipartServeletRequest， 输入文件流
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) httpServletRequest;
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        }

        //2.修改店铺信息
        if (shop != null && shop.getShopId() != null) {
            ShopExecution shopExecution;
            ImageHolder imageHolder = new ImageHolder(shopImg.getOriginalFilename(),shopImg.getInputStream());
            try {
                if (shopImg == null){
                    shopExecution = shopService.modifyShop(shop,null);
                }else {
                    shopExecution = shopService.modifyShop(shop, imageHolder);
                }
                if (shopExecution.getState() == ShopStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", "请输入店铺id");
                }
            } catch (ShopOperationException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
            }
            //3.无论是否成功都返回modelMap
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入店铺信息");
            return modelMap;
        }
    }



    //将CommonsMultipartFile转换为File的辅助方法  CommonsMultipartFile implements MultipartFile, Serializable
    private static void inputStreamToFile(InputStream ins, File file) {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = ins.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            throw new RuntimeException("调用inputStreamToFile异常：" + e.getMessage());
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (ins != null) {
                    ins.close();
                }
            } catch (IOException e) {
                throw new RuntimeException("inputStream关闭异常：" + e.getMessage());
            }
        }
    }
}
