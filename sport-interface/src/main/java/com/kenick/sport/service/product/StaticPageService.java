package com.kenick.sport.service.product;

import java.util.Map;

public interface StaticPageService {
    /**
     *  商品详情页 页面静态化 freemarker
     * @param rootMap 页面动态数据
     * @param productId 商品id
     */
    void freeMarkerProductDetail(Map<String,Object> rootMap,String productId);
}
