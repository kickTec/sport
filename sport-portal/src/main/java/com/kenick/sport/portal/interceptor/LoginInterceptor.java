package com.kenick.sport.portal.interceptor;

import com.alibaba.fastjson.JSON;
import com.kenick.sport.common.utils.LoginUtil;
import com.kenick.sport.portal.constant.PortalConstant;
import com.kenick.sport.service.buyer.BuyerService;
import com.kenick.sport.service.login.SessionProviderService;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
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
        if(username == null || "".equals(username)){ // 跳转到原链接或者首页
            String referer = request.getHeader("Referer"); // 默认跳转到原链接
            if(referer == null){ // 不是从网页跳转过来的，跳转到首页
                referer = "http://"+request.getServerName()+":"+request.getServerPort();
            }
            if(referer.contains("/shopping/toCart")){ // 原链接为购物车，在购物车中添加cookie，记录已选择购物项
                InputStream inputStream = request.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                StringBuilder stringBuilder = new StringBuilder();
                while((line = bufferedReader.readLine())!=null){
                    stringBuilder.append(line);
                }
                String paramStr = URLDecoder.decode(stringBuilder.toString(),"UTF-8");
                Map<String, String> orderInfoMap = getOrderInfoFromParam(paramStr);
                String orderInfoJSON = JSON.toJSONString(orderInfoMap);
                Boolean ret = buyerService.saveStringToRedis(PortalConstant.BUYER_ITEM_SELECT_REDIS_KEY, orderInfoJSON);
                if(!ret){
                    buyerService.saveStringToRedis(PortalConstant.BUYER_ITEM_SELECT_REDIS_KEY, orderInfoJSON);
                }
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