package com.kenick.sport.product.serviceImpl;

import com.kenick.sport.mapper.product.SkuMapper;
import com.kenick.sport.pojo.product.Sku;
import com.kenick.sport.pojo.product.SkuQuery;
import com.kenick.sport.service.product.SkuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("skuService")
public class SkuServiceImpl implements SkuService {
    @Resource
    SkuMapper skuMapper;

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
}
