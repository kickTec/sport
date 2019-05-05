package com.kenick.sport.portal.constant;

public interface PortalConstant {
    String BUYER_CART_COOKIE_KEY = "SPORT_BUYER_CART"; // 购物车
    String BUYER_CART_REDIS_KEY = "SPORT_BUYER_CART";

    String BUYER_ITEM_SELECT_COOKIE_KEY = "BUYER_ITEM_SELECT_KEY"; // 已选择购物项
    String BUYER_ITEM_SELECT_REDIS_KEY = "BUYER_ITEM_SELECT_KEY";

    String BUYER_ITEM_SELECT_LOGIN = "BUYER_ITEM_SELECT_LOGIN"; // 登录拦截时记录购物项
}
