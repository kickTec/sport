package com.kenick.sport.serviceImpl.product;

import com.kenick.sport.mapper.product.ProductMapper;
import com.kenick.sport.pojo.product.Product;
import com.kenick.sport.pojo.product.ProductQuery;
import com.kenick.sport.service.product.ProductService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("productService")
public class ProductServiceImpl implements ProductService {
    @Resource
    private ProductMapper productMapper;

    @Override
    public List<Product> selectProductByQuery(String productName, Long brandId, Boolean isShow, Integer pageNo, Integer pageSize) {
        // 查询条件
        ProductQuery productQuery = new ProductQuery();
        ProductQuery.Criteria criteria = productQuery.createCriteria();
        if(productName != null && !"".equals(productName)){
            criteria.andNameLike("%"+productName+"%");
        }
        if(brandId != null){
            criteria.andBrandIdEqualTo(brandId);
        }
        if(isShow != null){
            criteria.andIsShowEqualTo(isShow);
        }

        // 分页
        if(pageNo == null || pageNo <1){
            pageNo = 1;
        }
        if(pageSize == null || pageSize<0){
            pageSize = 5;
        }

        productQuery.setPageNo(pageNo);
        productQuery.setPageSize(pageSize);

        return productMapper.selectByExample(productQuery);
    }

    @Override
    public Integer getProductTotalSize(String productName, Long brandId, Boolean isShow) {
        ProductQuery productQuery = new ProductQuery();
        ProductQuery.Criteria criteria = productQuery.createCriteria();
        if(productName != null){
            criteria.andNameLike("%"+productName+"%");
        }
        if(brandId != null){
            criteria.andBrandIdEqualTo(brandId);
        }
        if(isShow != null){
            criteria.andIsShowEqualTo(isShow);
        }
        return productMapper.countByExample(productQuery);
    }
}
