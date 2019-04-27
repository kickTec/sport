package com.kenick.sport.portal.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.kenick.sport.common.utils.LoginUtil;
import com.kenick.sport.pojo.buyer.BuyerCart;
import com.kenick.sport.pojo.buyer.BuyerItem;
import com.kenick.sport.portal.constant.PortalConstant;
import com.kenick.sport.service.buyer.BuyerService;
import com.kenick.sport.service.login.SessionProviderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

@Controller
@RequestMapping("/shopping")
public class ShoppingController{
    @Resource
    private BuyerService buyerService;

    @Resource
    private SessionProviderService sessionProviderService;

    @RequestMapping("/buyerCart")
    public String buyerCart(Long skuId, Integer amount, RedirectAttributes model,
                            HttpServletRequest request, HttpServletResponse response){
        try {
            // 从cookie中获取购物车
            BuyerCart buyerCart = getBuyerCartFromCookie(request);

            // 已经登录
            if(isLogin(request,response)){
                // 从redis中获取购物车信息
                List<BuyerItem> redisBuyerItems = buyerService.getBuyerItemListFromRedis(PortalConstant.BUYER_CART_REDIS_KEY);

                // 同步cookie和redis中的购物信息
                for(BuyerItem redisItem:redisBuyerItems){
                    buyerCart.addBuyerItem(redisItem);
                }

                // 删除cookie购物信息
                Cookie cookie = new Cookie(PortalConstant.BUYER_CART_COOKIE_KEY, "");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);

                // 添加商品到购物车中
                buyerCart = buyerService.addSkuToBuyerCart(skuId,amount,buyerCart);

                // 将用户购物信息存储到redis
                Map<Long,Integer> buyerItem = getBuyerItemFromCart(buyerCart);
                buyerService.saveSkuMapToRedis(PortalConstant.BUYER_CART_REDIS_KEY,buyerItem);
            }else{ // 未登录
                // 获取已选择的购物项
                Map<Long, Integer> selectedBuyerItemMap = getSelectedBuyerItemFromCookie(PortalConstant.BUYER_ITEM_SELECT_COOKIE_KEY, request);
                selectedBuyerItemMap.put(skuId,amount);

                // 添加商品到购物车中
                buyerCart = buyerService.addSkuToBuyerCart(skuId,amount,buyerCart);

                // 设置购物车已选择项
                setBuyerCartSelectedAmount(buyerCart, selectedBuyerItemMap);

                // cookie中添加或更新购物信息
                Map<Long,Integer> buyerItem = getBuyerItemFromCart(buyerCart);
                Cookie buyerCartCookie = new Cookie(PortalConstant.BUYER_CART_COOKIE_KEY, JSON.toJSONString(buyerItem));
                buyerCartCookie.setMaxAge(3600);
                buyerCartCookie.setPath("/");
                response.addCookie(buyerCartCookie);
                Cookie buyerItemCookie = new Cookie(PortalConstant.BUYER_ITEM_SELECT_COOKIE_KEY, JSON.toJSONString(selectedBuyerItemMap));
                buyerItemCookie.setMaxAge(3600);
                buyerItemCookie.setPath("/");
                response.addCookie(buyerItemCookie);
            }

            // 返回购物信息到页面
            urlDecodeBuyerCart(buyerCart);
            buyerCart.setFee(10D);
            calcBuyerCart(buyerCart);
            model.addFlashAttribute("buyerCart", buyerCart);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/shopping/toCart";
    }

    @RequestMapping("/toCart")
    public String toCart(BuyerCart buyerCart,Model model,HttpServletRequest request,HttpServletResponse response){
        if(isLogin(request,response)){ // 已登录
            if(buyerCart == null){
                buyerCart = new BuyerCart();
            }
            List<BuyerItem> redisBuyerItemList = buyerService.getBuyerItemListFromRedis(PortalConstant.BUYER_CART_REDIS_KEY);
            if(redisBuyerItemList != null){
                buyerCart.setBuyerItems(redisBuyerItemList);
            }
            String selectItemJson = buyerService.getStringFromRedis(PortalConstant.BUYER_ITEM_SELECT_REDIS_KEY);
            Map<Long, Integer> selectedInfoMap = parseSelectedItem("orderInfo", selectItemJson);
            setBuyerCartSelectedAmount(buyerCart,selectedInfoMap);
        }else{ // 未登录
            if(buyerCart == null || buyerCart.getBuyerItems() == null){
                buyerCart = getBuyerCartFromCookie(request);
                // 获取已选择的购物项
                Map<Long, Integer> selectedBuyerItemMap = getSelectedBuyerItemFromCookie(PortalConstant.BUYER_ITEM_SELECT_COOKIE_KEY, request);
                // 设置购物车已选择项
                setBuyerCartSelectedAmount(buyerCart, selectedBuyerItemMap);
            }
        }
        calcBuyerCart(buyerCart);
        urlDecodeBuyerCart(buyerCart);
        model.addAttribute("buyerCart",buyerCart);
        return "cart";
    }

    @RequestMapping("/changeSkuSelect")
    public @ResponseBody Boolean changeSkuSelect(Long skuId,Integer skuAmount,Boolean checked,
            HttpServletRequest request,HttpServletResponse response){
        Map<Long,Integer> selectItemMap = new HashMap<>();
        String username = sessionProviderService.getAttributeForUsername(LoginUtil.getCSESSIONID(request, response));
        if(username != null && !"".equals(username)){ // 已登录

        }else{ // 未登录
            // 获取已选择的购物项
            selectItemMap = getSelectedBuyerItemFromCookie(PortalConstant.BUYER_ITEM_SELECT_COOKIE_KEY, request);
            BuyerCart buyerCart = getBuyerCartFromCookie(request);

            if(checked){
                selectItemMap.put(skuId, skuAmount);
            }else{
                selectItemMap.remove(skuId);
            }
            resetBuyerCartSkuAmount(buyerCart,skuId,skuAmount);

            Cookie buyerItemCookie = new Cookie(PortalConstant.BUYER_ITEM_SELECT_COOKIE_KEY, JSON.toJSONString(selectItemMap));
            buyerItemCookie.setMaxAge(3600);
            buyerItemCookie.setPath("/");
            response.addCookie(buyerItemCookie);

            Map<Long,Integer> buyerItem = getBuyerItemFromCart(buyerCart);
            Cookie buyerCartCookie = new Cookie(PortalConstant.BUYER_CART_COOKIE_KEY, JSON.toJSONString(buyerItem));
            buyerCartCookie.setMaxAge(3600);
            buyerCartCookie.setPath("/");
            response.addCookie(buyerCartCookie);
        }
        return true;
    }

    // 重置购物车中的购物项
    private void resetBuyerCartSkuAmount(BuyerCart buyerCart, Long skuId, Integer skuAmount) {
        if(buyerCart == null || buyerCart.getBuyerItems() == null){
            return;
        }
        List<BuyerItem> buyerItems = buyerCart.getBuyerItems();
        Iterator<BuyerItem> buyerItemIterator = buyerItems.iterator();
        while(buyerItemIterator.hasNext()){
            BuyerItem buyerItem = buyerItemIterator.next();
            if(buyerItem.getSkuId().equals(skuId)){
                buyerItem.setAmount(skuAmount);
            }
        }
    }

    // 从cookie中获取已选择的购物项
    private Map<Long,Integer> getSelectedBuyerItemFromCookie(String cookieKey,HttpServletRequest request){
        String selectedItemJson = "";
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie:cookies){
            if(cookieKey.equals(cookie.getName())){
                selectedItemJson = cookie.getValue();
            }
        }
        if("".equals(selectedItemJson)){
            return new HashMap<>();
        }else{
            return JSON.parseObject(selectedItemJson,new TypeReference<Map<Long, Integer>>(){});
        }
    }

    // 从购物车中获取购物项
    private Map<Long, Integer> getBuyerItemFromCart(BuyerCart buyerCart) {
        HashMap<Long, Integer> buyerItemMap = new HashMap<>();
        if(buyerCart == null){
            return null;
        }
        List<BuyerItem> buyerItems = buyerCart.getBuyerItems();
        for(BuyerItem buyerItem:buyerItems){
            buyerItemMap.put(buyerItem.getSkuId(),buyerItem.getAmount());
        }
        return buyerItemMap;
    }

    // 设置购物车勾选项
    private void setBuyerCartSelectedAmount(BuyerCart buyerCart, Map<Long,Integer> selectedInfoMap){
        if(buyerCart == null || buyerCart.getBuyerItems() == null || selectedInfoMap == null || selectedInfoMap.size() == 0){
            return;
        }
        Set<Long> skuIdSet = selectedInfoMap.keySet();
        for(Long skuId:skuIdSet){
            Integer selectedAmount = selectedInfoMap.get(skuId);
            List<BuyerItem> buyerItemList = buyerCart.getBuyerItems();

            Iterator<BuyerItem> iterator = buyerItemList.iterator();
            while(iterator.hasNext()){
                BuyerItem buyerItem = iterator.next();
                if(skuId.equals(buyerItem.getSkuId())){
                    buyerItem.setSelected(true);
                    buyerItem.setAmount(selectedAmount);
                }
            }
        }
    }

    // 解析选中的购物项 "|" + skuId+"_"+skuAmount + "|" + skuId+"_"+skuAmount
    private Map<Long,Integer> parseSelectedItem(String paramKey,String paramJson){
        Map<Long, Integer> selectedItemMap= new HashMap<>();
        String orderInfo = "";
        Map paramMap = JSON.parseObject(paramJson, Map.class);
        Set keySet = paramMap.keySet();
        for(Object key:keySet){
            if(paramKey.equals(key.toString())){
                orderInfo = paramMap.get(key).toString();
                break;
            }
        }
        String[] skuArray = orderInfo.split("\\|");
        for(String skuStr:skuArray){
            if(skuStr != null && !"".equals(skuStr)){
                String[] itemArray = skuStr.split("_");
                if(itemArray.length == 2){
                    selectedItemMap.put(Long.parseLong(itemArray[0]),Integer.parseInt(itemArray[1]));
                }
            }
        }
        return selectedItemMap;
    }

    // 是否登录
    private Boolean isLogin(HttpServletRequest request, HttpServletResponse response){
        String csessionid = LoginUtil.getCSESSIONID(request, response);
        String username = sessionProviderService.getAttributeForUsername(csessionid);
        return username!=null && !"".equals(username);
    }

    // 计算购物车各项值
    private void calcBuyerCart(BuyerCart buyerCart){
        if(buyerCart == null){
            return;
        }
        int totalAmount = 0;
        double totalMoney = 0D;
        List<BuyerItem> buyerItems = buyerCart.getBuyerItems();
        for(BuyerItem buyerItem:buyerItems){
            totalAmount += buyerItem.getAmount();
            totalMoney += buyerItem.getSkuPrice() * buyerItem.getAmount();
        }
        Double totalFinalMoney = (totalMoney - buyerCart.getFee() < 0) ? 0 :(totalMoney - buyerCart.getFee());
        buyerCart.setProductAmount(totalAmount);
        buyerCart.setProductTotalPrice(totalMoney);
        buyerCart.setFinalTotalPrice(totalFinalMoney);
    }

    // 对购物车中文进行url反编码
    private void urlDecodeBuyerCart(BuyerCart buyerCart){
        try {
            List<BuyerItem> buyerItems = buyerCart.getBuyerItems();
            for(BuyerItem buyerItem:buyerItems){
                buyerItem.setProductName(URLDecoder.decode(buyerItem.getProductName(),"UTF-8"));
                buyerItem.setSkuColor(URLDecoder.decode(buyerItem.getSkuColor(),"UTF-8"));
            }
            buyerCart.setBuyerItems(buyerItems);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    // 从cookie中获取购物车信息
    private BuyerCart getBuyerCartFromCookie(HttpServletRequest request){
        // 从cookie中获取购物车
        BuyerCart cookieBuyerCart = new BuyerCart();
        try {
            Cookie[] cookies = request.getCookies();
            for(Cookie cookie:cookies){
                if(PortalConstant.BUYER_CART_COOKIE_KEY.equals(cookie.getName())){
                    String buyerCartJson = cookie.getValue();
                    if(buyerCartJson != null && !"".equals(buyerCartJson)){
                        Map objectMap = JSON.parseObject(buyerCartJson, Map.class);
                        Set skuIdSet = objectMap.keySet();
                        for(Object skuIdObj:skuIdSet){
                            Object amountObj = objectMap.get(skuIdObj);
                            cookieBuyerCart = buyerService.addSkuToBuyerCart(Long.parseLong(skuIdObj.toString()), Integer.parseInt(amountObj.toString()),cookieBuyerCart);
                        }
                    }
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cookieBuyerCart;
    }
}