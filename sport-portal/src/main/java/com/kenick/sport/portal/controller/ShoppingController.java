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
            // 获取登录用户名
            String username = sessionProviderService.getAttributeForUsername(LoginUtil.getCSESSIONID(request,response));

            // 从cookie中获取购物车
            BuyerCart cookieBuyerCart = getBuyerCartFromCookie(request);

            // 已经登录
            if(username != null && !"".equals(username)){
                // 从redis中获取购物信息
                List<BuyerItem> redisBuyerItems = buyerService.getBuyerItemListFromRedis(PortalConstant.BUYER_CART_REDIS_KEY+"_"+username);

                // 同步cookie和redis中的购物信息
                for(BuyerItem redisItem:redisBuyerItems){
                    cookieBuyerCart.addBuyerItem(redisItem, true);
                }

                // 添加商品到购物车中
                cookieBuyerCart = buyerService.addSkuToBuyerCart(skuId, amount, cookieBuyerCart);

                // 从cookie中获取已选择的购物项
                Map<Long, Integer> cookieSelectItemMap = getSelectedBuyerItemFromCookie(PortalConstant.BUYER_ITEM_SELECT_COOKIE_KEY, request);

                // 从redis中获取已选择的购物项 相关skuId amount相加
                String redisSelectBuyerItemJson = buyerService.redisHget(PortalConstant.BUYER_ITEM_SELECT_REDIS_KEY, username);
                Map<Long, Integer> redisSelectItemMap = new HashMap<>();
                if(redisSelectBuyerItemJson != null && !"".equals(redisSelectBuyerItemJson)){
                    redisSelectItemMap = JSON.parseObject(redisSelectBuyerItemJson, new TypeReference<Map<Long, Integer>>(){});
                }
                Integer oldAmount = redisSelectItemMap.get(skuId);
                oldAmount = (oldAmount == null) ? 0 : oldAmount;
                oldAmount = oldAmount + amount;
                redisSelectItemMap.put(skuId, oldAmount);

                // 合并cookie和redis中的已选择项
                Map<Long,Integer> mergeSelectMap = mergeCookieRedisMap(cookieSelectItemMap, redisSelectItemMap);

                // 设置购物车勾选项
                setBuyerCartSelectedAmount(cookieBuyerCart, mergeSelectMap);

                // 删除cookie购物信息
                Cookie cookie = new Cookie(PortalConstant.BUYER_CART_COOKIE_KEY, "");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);

                // 将用户购物信息存储到redis
                Map<Long,Integer> buyerItem = getBuyerItemFromCart(cookieBuyerCart);
                buyerService.saveSkuMapToRedis(PortalConstant.BUYER_CART_REDIS_KEY+"_"+username,buyerItem);

                // 将已选择项保存到redis
                buyerService.redisHSet(PortalConstant.BUYER_ITEM_SELECT_REDIS_KEY,username,JSON.toJSONString(mergeSelectMap));
            }else{ // 未登录
                // 获取已选择的购物项 相同skuId amount相加
                Map<Long, Integer> selectedBuyerItemMap = getSelectedBuyerItemFromCookie(PortalConstant.BUYER_ITEM_SELECT_COOKIE_KEY, request);
                Integer oldAmount = selectedBuyerItemMap.get(skuId);
                if(oldAmount != null){
                    selectedBuyerItemMap.put(skuId, oldAmount+amount);
                }else{
                    selectedBuyerItemMap.put(skuId, amount);
                }

                // 添加商品到购物车中
                cookieBuyerCart = buyerService.addSkuToBuyerCart(skuId, amount, cookieBuyerCart);

                // 设置购物车已选择项
                setBuyerCartSelectedAmount(cookieBuyerCart, selectedBuyerItemMap);

                // cookie中添加或更新购物信息
                Map<Long,Integer> buyerItem = getBuyerItemFromCart(cookieBuyerCart);
                Cookie buyerCartCookie = new Cookie(PortalConstant.BUYER_CART_COOKIE_KEY, JSON.toJSONString(buyerItem));
                buyerCartCookie.setMaxAge(3600);
                buyerCartCookie.setPath("/");
                response.addCookie(buyerCartCookie);

                // cookie中添加或更新已选项
                Cookie buyerItemCookie = new Cookie(PortalConstant.BUYER_ITEM_SELECT_COOKIE_KEY, JSON.toJSONString(selectedBuyerItemMap));
                buyerItemCookie.setMaxAge(3600);
                buyerItemCookie.setPath("/");
                response.addCookie(buyerItemCookie);
            }

            // 返回购物信息到页面
            urlDecodeBuyerCart(cookieBuyerCart);
            cookieBuyerCart.setFee(10D);
            calcBuyerCart(cookieBuyerCart);
            model.addFlashAttribute("buyerCart", cookieBuyerCart);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/shopping/toCart";
    }

    @RequestMapping("/toCart")
    public String toCart(BuyerCart buyerCart,Model model,HttpServletRequest request,HttpServletResponse response){
        String username = sessionProviderService.getAttributeForUsername(LoginUtil.getCSESSIONID(request,response));
        BuyerCart cookieBuyerCart = getBuyerCartFromCookie(request); // 从cookie中获取购物车

        if(username != null && !"".equals(username)){ // 已登录
            List<BuyerItem> redisBuyerItems = buyerService.getBuyerItemListFromRedis(PortalConstant.BUYER_CART_REDIS_KEY+"_"+username); // 从redis中获取购物车

            // 同步cookie和redis购物车中的购物信息
            for(BuyerItem redisItem:redisBuyerItems){
                cookieBuyerCart.addBuyerItem(redisItem, true);
            }
            if(buyerCart == null){
                buyerCart = new BuyerCart();
            }
            buyerCart.setBuyerItems(cookieBuyerCart.getBuyerItems());

            // 从cookie中获取已选择的购物项
            Map<Long, Integer> cookieSelectItemMap = getSelectedBuyerItemFromCookie(PortalConstant.BUYER_ITEM_SELECT_COOKIE_KEY, request);

            // 从redis中获取已选择的购物项
            String redisSelectBuyerItemJson = buyerService.redisHget(PortalConstant.BUYER_ITEM_SELECT_REDIS_KEY, username);
            Map<Long, Integer> redisSelectItemMap = new HashMap<>();
            if(redisSelectBuyerItemJson != null && !"".equals(redisSelectBuyerItemJson)){
                redisSelectItemMap = JSON.parseObject(redisSelectBuyerItemJson, new TypeReference<Map<Long, Integer>>(){});
            }

            // 合并cookie和redis中的已选择项
            Map<Long,Integer> mergeSelectMap = mergeCookieRedisMap(cookieSelectItemMap, redisSelectItemMap);
            setBuyerCartSelectedAmount(buyerCart, mergeSelectMap);

            // 删除cookie中的已选择项
            Cookie cookie = new Cookie(PortalConstant.BUYER_ITEM_SELECT_COOKIE_KEY, "");
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);

            // 将已选择项保存到redis
            buyerService.redisHSet(PortalConstant.BUYER_ITEM_SELECT_REDIS_KEY, username, JSON.toJSONString(mergeSelectMap));

            // 将用户购物信息存储到redis
            Map<Long,Integer> buyerItem = getBuyerItemFromCart(buyerCart);
            buyerService.saveSkuMapToRedis(PortalConstant.BUYER_CART_REDIS_KEY+"_"+username, buyerItem);
        }else{ // 未登录
            if(buyerCart.getBuyerItems().size() == 0){ // 不是从页面跳转过来，从cookie中获取购物信息
                for(BuyerItem buyerItem:cookieBuyerCart.getBuyerItems()){
                    buyerCart.addBuyerItem(buyerItem, true);
                }
            }

            // 从页面跳转过来
            // 获取已选择的购物项
            Map<Long, Integer> selectedBuyerItemMap = getSelectedBuyerItemFromCookie(PortalConstant.BUYER_ITEM_SELECT_COOKIE_KEY, request);
            setBuyerCartSelectedAmount(buyerCart, selectedBuyerItemMap);

            // 设置购物车已选择项
            setBuyerCartSelectedAmount(buyerCart, selectedBuyerItemMap);

            // cookie中添加或更新购物信息
            Map<Long,Integer> buyerItem = getBuyerItemFromCart(buyerCart);
            Cookie buyerCartCookie = new Cookie(PortalConstant.BUYER_CART_COOKIE_KEY, JSON.toJSONString(buyerItem));
            buyerCartCookie.setMaxAge(3600);
            buyerCartCookie.setPath("/");
            response.addCookie(buyerCartCookie);

            // cookie中添加或更新已选项
            Cookie buyerItemCookie = new Cookie(PortalConstant.BUYER_ITEM_SELECT_COOKIE_KEY, JSON.toJSONString(selectedBuyerItemMap));
            buyerItemCookie.setMaxAge(3600);
            buyerItemCookie.setPath("/");
            response.addCookie(buyerItemCookie);
        }
        calcBuyerCart(buyerCart);
        urlDecodeBuyerCart(buyerCart);
        model.addAttribute("buyerCart", buyerCart);
        return "cart";
    }

    @RequestMapping("/changeSkuSelect")
    public @ResponseBody Boolean changeSkuSelect(Long skuId,Integer skuAmount,Boolean checked,
            HttpServletRequest request,HttpServletResponse response){
        Map<Long,Integer> selectItemMap;
        String username = sessionProviderService.getAttributeForUsername(LoginUtil.getCSESSIONID(request, response));
        if(username != null && !"".equals(username)){ // 已登录
            BuyerCart buyerCart = new BuyerCart();
            Map<Long, Integer> selectItemRedisMap = getSelectedBuyerItemFromRedis(PortalConstant.BUYER_ITEM_SELECT_REDIS_KEY, username);
            List<BuyerItem> redisBuyerItemList = buyerService.getBuyerItemListFromRedis(PortalConstant.BUYER_CART_REDIS_KEY);
            if(redisBuyerItemList != null){
                buyerCart.setBuyerItems(redisBuyerItemList);
            }

            if(checked){
                selectItemRedisMap.put(skuId, skuAmount);
            }else{
                selectItemRedisMap.remove(skuId);
            }
            resetBuyerCartSkuAmount(buyerCart,skuId,skuAmount,checked);

            // 保存购物信息
            Map<Long,Integer> buyerItem = getBuyerItemFromCart(buyerCart);
            buyerService.saveSkuMapToRedis(PortalConstant.BUYER_CART_REDIS_KEY,buyerItem);
            buyerService.redisHSet(PortalConstant.BUYER_ITEM_SELECT_REDIS_KEY,username,JSON.toJSONString(selectItemRedisMap));
        }else{ // 未登录
            // 获取已选择的购物项
            selectItemMap = getSelectedBuyerItemFromCookie(PortalConstant.BUYER_ITEM_SELECT_COOKIE_KEY, request);
            BuyerCart buyerCart = getBuyerCartFromCookie(request);

            if(checked){
                selectItemMap.put(skuId, skuAmount);
            }else{
                selectItemMap.remove(skuId);
            }
            resetBuyerCartSkuAmount(buyerCart,skuId,skuAmount,checked);

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

    // 合并cookie 和 redis选择购物项map cookie中的已选择项优先
    private Map<Long, Integer> mergeCookieRedisMap(Map<Long, Integer> cookieSelectItemMap, Map<Long, Integer> redisSelectItemMap) {
        if(cookieSelectItemMap == null || redisSelectItemMap == null){
            return new HashMap<>();
        }
        Iterator<Long> skuIdIterator = redisSelectItemMap.keySet().iterator();
        while(skuIdIterator.hasNext()){
            Long skuId = skuIdIterator.next();
            if(!cookieSelectItemMap.containsKey(skuId)){
                cookieSelectItemMap.put(skuId,redisSelectItemMap.get(skuId));
            }
        }
        return cookieSelectItemMap;
    }

    // 重置购物车中的购物项
    private void resetBuyerCartSkuAmount(BuyerCart buyerCart, Long skuId, Integer skuAmount,Boolean checked) {
        if(buyerCart == null || buyerCart.getBuyerItems() == null){
            return;
        }
        List<BuyerItem> buyerItems = buyerCart.getBuyerItems();
        Iterator<BuyerItem> buyerItemIterator = buyerItems.iterator();
        while(buyerItemIterator.hasNext()){
            BuyerItem buyerItem = buyerItemIterator.next();
            if(buyerItem.getSkuId().equals(skuId)){
                buyerItem.setAmount(skuAmount);
                buyerItem.setSelected(checked);
            }
        }
    }

    // 从redis中获取已选择的购物项
    private Map<Long,Integer> getSelectedBuyerItemFromRedis(String hKey,String mapKey){
        String selectedItemJson = buyerService.redisHget(hKey,mapKey);
        if(selectedItemJson == null || "".equals(selectedItemJson)){
            return new HashMap<>();
        }else{
            return JSON.parseObject(selectedItemJson,new TypeReference<Map<Long, Integer>>(){});
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