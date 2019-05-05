package com.kenick.sport.portal.interceptor;

import com.kenick.sport.common.utils.LoginUtil;
import com.kenick.sport.service.buyer.BuyerService;
import com.kenick.sport.service.login.SessionProviderService;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class LoginInterceptor implements HandlerInterceptor {
    @Resource
    private SessionProviderService sessionProviderService;

    @Resource
    private BuyerService buyerService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String username = sessionProviderService.getAttributeForUsername(LoginUtil.getCSESSIONID(request, response));
        if(username == null || "".equals(username)){ // 未登录跳转到原链接或者首页
            String referer = request.getHeader("Referer"); // 默认跳转到原链接
            if(referer == null){ // 不是从网页跳转过来的，跳转到首页
                referer = "http://"+request.getServerName()+":"+request.getServerPort();
            }
            String loginUrl = "http://localhost:8080/login/login.aspx?returnUrl="+ URLEncoder.encode(referer,"UTF-8");
            response.sendRedirect(loginUrl);
            return false;
        }
        return true;
    }

    // 从参数字符串中解析出key value
    private Map<String,String> getOrderInfoFromParam(String paramStr){
        Map<String, String> paramMap = new HashMap<String, String>();
        String[] paramArray = paramStr.split("&");
        for(String param:paramArray){
            String[] keyValueArray = param.split("=");
            if(keyValueArray.length == 2){
                paramMap.put(keyValueArray[0],keyValueArray[1]);
            }
        }
        return paramMap;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}