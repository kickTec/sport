package com.kenick.sport.mapper.product;

import com.kenick.sport.pojo.product.Brand;
import com.kenick.sport.pojo.product.BrandQuery;

import java.util.List;

public interface BrandMapper {
    /**
     * 查询所有符合条件的品牌 不分页
     * @param brandQuery 品牌条件
     * @return 符合条件品牌
     */
    List<Brand> queryBrandListByQueryAndNoPage(BrandQuery brandQuery);

    /**
     * 查询符合条件的品牌总数
     * @param brandQuery 品牌条件
     * @return 符合条件品牌数
     */
    Integer queryBrandSumByQueryAndPage(BrandQuery brandQuery);

    /**
     * 查询符合条件品牌 分页
     * @param brandQuery 品牌条件
     * @return 品牌集合
     */
    List<Brand> queryBrandListByQueryAndPage(BrandQuery brandQuery);

    /**
     * 查询指定ID的品牌
     * @param brandId 品牌ID
     * @return 指定ID品牌
     */
    Brand queryBrandById(String brandId);

    /**
     * 修改品牌 若属性为null，则不修改
     * @param brand 待修改的新Brand
     * @return 受影响行数
     */
    Integer updateBrand(Brand brand);

    /**
     * 添加品牌信息
     * @param brand 品牌信息
     * @return 受影响行数
     */
    Integer insertBrand(Brand brand);

    /**
     *  删除品牌信息
     * @param brandId 品牌id
     * @return 受影响行数
     */
    Integer deleteBrandById(Integer brandId);

    /**
     *  批量删除品牌信息
     * @param idList 品牌id List
     * @return 受影响的行
     */
    Integer deleteBrandByList(List<Integer> idList);
}