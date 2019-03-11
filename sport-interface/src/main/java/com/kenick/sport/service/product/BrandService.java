package com.kenick.sport.service.product;

import com.kenick.sport.pojo.product.Brand;
import com.kenick.sport.pojo.product.BrandQuery;

import java.util.List;

public interface BrandService {
    /**
     * 根据条件查询品牌信息 不分页
     *
     * @param brandQuery 查询条件
     * @return 品牌集合
     */
    List<Brand> selectBrandListByQueryNoPage(BrandQuery brandQuery);

    /**
     * 根据条件查询品牌总条目
     *
     * @param brandQuery 查询条件
     * @return 条目总数
     */
    Integer selectBrandTotalByQuery(BrandQuery brandQuery);

    /**
     * 根据条件查询品牌信息 分页
     *
     * @param brandQuery 查询条件
     * @param pageNo     当前页码
     * @param pageSize   每页大小
     * @return 品牌信息集合
     */
    List<Brand> selectBrandListByQueryAndPage(BrandQuery brandQuery, Integer pageNo, Integer pageSize);

    /**
     * 根据brandId查询获取Brand对象
     *
     * @param brandId 品牌ID
     * @return 品牌对象
     */
    Brand selectBrandById(String brandId);

    /**
     * 修改品牌信息
     * @param brand 新对象
     * @return 修改结果
     *          -3 无此id的品牌信息
     *          -2 id为null
     *          -1 不需修改
     *          >-1 底层返回受影响行数
     */
    Integer updateBrand(Brand brand);

    /**
     * 添加品牌信息
     * @param brand 品牌信息
     * @return 是否添加成功
     */
    Boolean addBrand(Brand brand);

    /**
     * 删除品牌信息
     * @param brandId 品牌id
     * @return 操作是否成功
     */
    Boolean deleteBrand(Integer brandId);

    /**
     * 批量删除品牌信息
     * @param idArray 品牌id数组
     * @return 操作是否成功
     */
    Boolean deleteBrandByArray(String[] idArray);
}