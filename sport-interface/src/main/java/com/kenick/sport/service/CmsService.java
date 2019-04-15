package com.kenick.sport.service;

import com.kenick.sport.pojo.product.Product;
import com.kenick.sport.pojo.product.Sku;

import java.util.List;

public interface CmsService {
    /**
     *  通过商品id获取商品信息
     * @param productId 商品id
     * @return 商品信息
     */
    Product selectProductById(Long productId);

    /**
     * 根据商品id查询库存大于0的列表
     * @param productId 商品id
     * @return 库存列表
     */
    List<Sku> selectSkuListByProductIdAnStockMoreThanZero(Long productId);
}
