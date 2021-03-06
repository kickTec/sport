package com.kenick.sport.product.serviceImpl;

import com.kenick.sport.mapper.product.ColorMapper;
import com.kenick.sport.mapper.product.ProductMapper;
import com.kenick.sport.mapper.product.SkuMapper;
import com.kenick.sport.pojo.product.Color;
import com.kenick.sport.pojo.product.Product;
import com.kenick.sport.pojo.product.Sku;
import com.kenick.sport.pojo.product.SkuQuery;
import com.kenick.sport.service.product.SkuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("skuService")
public class SkuServiceImpl implements SkuService {
    @Resource
    private SkuMapper skuMapper;

    @Resource
    private ProductMapper productMapper;

    @Resource
    private ColorMapper colorMapper;

    @Override
    public List<Sku> selectSkuByProductId(Long productId) {
        SkuQuery skuQuery = new SkuQuery();
        skuQuery.createCriteria().andProductIdEqualTo(productId);
        return skuMapper.selectByExample(skuQuery);
    }

    @Override
    public Integer updateSku(Sku sku) {
        return skuMapper.updateByPrimaryKeySelective(sku);
    }

    @Override
    public List<Sku> selectSkuAndColorByProductId(Long productId) {
        return skuMapper.selectSkuAndColorByProductId(productId);
    }

    @Override
    public Float selectLowestPriceByProductId(Long productId) {
        Float price = 0.0f;
        SkuQuery skuQuery = new SkuQuery();
        skuQuery.createCriteria().andProductIdEqualTo(productId);
        skuQuery.setFields("price");
        skuQuery.setOrderByClause("price asc");
        skuQuery.setPageNo(1);
        skuQuery.setPageSize(1);
        List<Sku> skus = skuMapper.selectByExample(skuQuery);
        if(skus.size() > 0){
            price = skus.get(0).getPrice();
        }
        return price;
    }

    @Override
    public Sku selectSkuBySkuId(Long skuId) {
        Sku sku = skuMapper.selectByPrimaryKey(skuId);
        Product product = productMapper.selectByPrimaryKey(sku.getProductId());
        sku.setProduct(product);
        Color color = colorMapper.selectByPrimaryKey(sku.getColorId());
        sku.setColor(color);
        return sku;
    }
}
