package com.imooc.o2o.util;

import com.google.code.kaptcha.Constants;

import javax.servlet.http.HttpServletRequest;

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
}
