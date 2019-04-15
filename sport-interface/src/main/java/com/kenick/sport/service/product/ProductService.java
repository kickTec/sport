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

    /**
     *  保存商品信息
     * @param product 商品信息
     * @return 受影响的行
     */
    Integer saveProduct(Product product) throws Exception;

    /**
     *  通过id删除商品信息
     * @param productId 商品id
     * @return 受影响的行
     */
    Integer deleteProductById(Long productId) throws Exception;

    /**
     * 商品上架
     * @param ids 商品id数组
     * @return 0:上架成功
     */
    void isShow(String[] ids) throws Exception;

    /**
     * 根据商品id获取商品信息
     * @param productId 商品id
     * @return 商品信息
     */
    Product selectProductById(Long productId);
}
