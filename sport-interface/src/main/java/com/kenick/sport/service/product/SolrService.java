package com.kenick.sport.service.product;

import com.kenick.sport.pojo.page.Pagination;
import com.kenick.sport.pojo.product.Product;

import java.util.List;

public interface SolrService {
    /**
     *  搜索符合content条件的商品总数
     * @param content 搜索条件
     * @return 商品总数
     */
    Long searchProductSum(String content);

    /**
     *  搜索符合content条件的商品集合
     * @param content 搜索条件
     * @param pageNo 当前页
     * @param pageSize 每页大小
     * @return 商品集合
     */
    List<Product> searchProductList(String content,Integer pageNo,Integer pageSize);

    /**
     *  根据关键字进行检索
     * @param keyword 关键字
     * @param pageNo 当前页码
     * @return 分页对象
     */
    Pagination selectProductListFromSolr(String keyword,String brandId,String price,Integer pageNo) throws Exception;
}
