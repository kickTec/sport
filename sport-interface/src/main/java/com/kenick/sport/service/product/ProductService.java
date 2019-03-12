package com.kenick.sport.service.product;

import com.kenick.sport.pojo.product.Product;

import java.util.List;

public interface ProductService {
    /**
     * 根据条件查询所有商品信息
     * @param productName 商品名称
     * @param brandId 品牌id
     * @param isShow 上架
     * @param pageNo 当前页码
     * @param pageSize 每页大小
     * @return 商品集合
     */
    List<Product> selectProductByQuery(String productName,Long brandId,Boolean isShow,Integer pageNo,Integer pageSize);

    /**
     * 查询符合条件的商品总数
     * @param productName 商品名称
     * @param brandId 品牌id
     * @param isShow 上下架
     * @return 商品总数
     */
    Integer getProductTotalSize(String productName,Long brandId,Boolean isShow);
}
