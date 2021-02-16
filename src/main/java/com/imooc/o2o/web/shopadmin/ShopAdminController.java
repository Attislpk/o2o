package com.imooc.o2o.web.shopadmin;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/shopadmin")
public class ShopAdminController {

    @RequestMapping(value = "/shopoperation", method = RequestMethod.GET)
    public String shopOperation(){
        return "/shop/shopoperation"; //  /WEB-INF/html/shop/shopoperation.html
    }

    //访问shoplist.html
    @RequestMapping(value = "/shoplist", method = RequestMethod.GET)
    public String getShopList(){
        return "/shop/shoplist";
    }

    //访问shopmanagement.html
    @RequestMapping(value = "/shopmanagement", method = RequestMethod.GET)
    public String shopManagement(){
        return "/shop/shopmanagement";
    }

    //访问productcategorymanagement.html
    @RequestMapping(value = "/productcategorymanagement", method = RequestMethod.GET)
    public String productCategoryManagement(){
        return "/shop/productcategorymanagement";
    }


    //访问productopearation.html
    @RequestMapping(value = "/productoperation")
    public String productOperation(){
        return "shop/productoperation";
    }

}
