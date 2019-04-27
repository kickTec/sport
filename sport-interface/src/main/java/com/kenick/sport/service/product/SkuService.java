package com.kenick.sport.service.product;

import com.kenick.sport.pojo.product.Sku;

import java.util.List;

public interface SkuService {

    /**
     *  通过商品id获取所有库存信息
     * @param productId 商品id
     * @return 库存集合
     */
    List<Sku> selectSkuByProductId(Long productId);

    /**
     *  修改商品库存信息
     * @param sku 新库存信息
     * @return 受影响的行
     */
    Integer updateSku(Sku sku);

    /**
     *  根据商品id 查询包含颜色的库存信息
     * @param productId 商品id
     * @return 库存信息
     */
    List<Sku> selectSkuAndColorByProductId(Long productId);

    /**
     *  通过产品ID获取最低价
     * @param productId
     * @return
     */
    Float selectLowestPriceByProductId(Long productId);

    /**
     *  通过sku id获取sku
     * @param skuId sku主键
     * @return sku实体类
     */
    Sku selectSkuBySkuId(Long skuId);
}
