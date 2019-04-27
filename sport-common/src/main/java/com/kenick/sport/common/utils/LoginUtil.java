package com.kenick.sport.common.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

public class LoginUtil {
    /**
     *  获取票据，如果没有则随机生成，同时将该票据通过response设置到浏览器本地
     * @return 票据
     */
    public static String getCSESSIONID(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();

        // 从cookie中获取票据
        if(cookies != null && cookies.length >0){
            for(Cookie cookie:cookies){
                if("CSESSIONID".equals(cookie.getName())){
                    return cookie.getValue();
                }
            }
        }

        // 第一次操作，自动生成票据
        String CSESSIONID = UUID.randomUUID().toString().replace("-", "");
        Cookie cookie = new Cookie("CSESSIONID", CSESSIONID);
        cookie.setMaxAge(60*60);
        cookie.setPath("/");
        response.addCookie(cookie);
        return CSESSIONID;
    }
}
