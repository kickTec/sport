package com.kenick.sport.cms.serviceImpl;

import com.kenick.sport.mapper.product.ColorMapper;
import com.kenick.sport.mapper.product.ProductMapper;
import com.kenick.sport.mapper.product.SkuMapper;
import com.kenick.sport.pojo.product.Product;
import com.kenick.sport.pojo.product.Sku;
import com.kenick.sport.pojo.product.SkuQuery;
import com.kenick.sport.service.CmsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("cmsService")
public class CmsServiceImpl implements CmsService {
    @Resource
    private ProductMapper productMapper;

    @Resource
    private SkuMapper skuMapper;

    @Resource
    private ColorMapper colorMapper;

    @Override
    public Product selectProductById(Long productId) {
        return productMapper.selectByPrimaryKey(productId);
    }

    @Override
    public List<Sku> selectSkuListByProductIdAnStockMoreThanZero(Long productId) {
        SkuQuery skuQuery = new SkuQuery();
        skuQuery.createCriteria().andProductIdEqualTo(productId).andStockGreaterThan(0);
        List<Sku> skus = skuMapper.selectByExample(skuQuery);
        for(Sku sku:skus){
            sku.setColor(colorMapper.selectByPrimaryKey(sku.getColorId()));
        }
        return skus;
    }
}
