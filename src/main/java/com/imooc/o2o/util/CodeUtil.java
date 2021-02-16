package com.imooc.o2o.util;

import com.google.code.kaptcha.Constants;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class CodeUtil {
    public static boolean checkVerifyCode(HttpServletRequest httpServletRequest){
        //生成的验证码
        String verifyCodeExpected = (String)httpServletRequest.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
        //实际输入的验证码
        String verifyCodeInput = HttpServletRequestUtil.getString(httpServletRequest, "verifyCodeInput");

        if (verifyCodeInput == null || !verifyCodeExpected.equals(verifyCodeInput)){
            return false;
        }
        return true;
    }


        //验证码比对方法
        public static Map<String, Object> codeVerify(HttpServletRequest httpServletRequest){
            Map<String, Object> modelMap = new HashMap<>();
            //验证码比对
            if (!CodeUtil.checkVerifyCode(httpServletRequest)) {
                modelMap.put("success", false);
                modelMap.put("errMsg", "验证码输入错误");
            }
            return modelMap;
        }

}
