package com.imooc.o2o.util;

import javax.servlet.http.HttpServletRequest;

public class HttpServletRequestUtil {

    public static int getInt(HttpServletRequest httpServletRequest, String key){
        try {
            return Integer.decode(httpServletRequest.getParameter(key));
        }catch (Exception e){
            return -1;
        }
    }

    public static long getLong(HttpServletRequest httpServletRequest, String key){
        try {
            return Long.parseLong(httpServletRequest.getParameter(key));
        }catch (Exception e){
            return -1L;
        }
    }

    public static double getDouble(HttpServletRequest httpServletRequest, String key){
        try {
            return Double.parseDouble(httpServletRequest.getParameter(key));
        }catch (Exception e){
            return -1d;
        }
    }

    public static boolean getBoolean(HttpServletRequest httpServletRequest, String key){
        try {
            return Boolean.valueOf(httpServletRequest.getParameter(key));
        }catch (Exception e){
            return false;
        }
    }

    public static String getString(HttpServletRequest httpServletRequest, String key){
        try {
            String parameter = httpServletRequest.getParameter(key);
            if (parameter != null){
                parameter = parameter.trim();
            }
            if ("".equals(parameter)){
                parameter = null;
            }
            return parameter;
        }catch (Exception e){
            return null;
        }
    }
}
