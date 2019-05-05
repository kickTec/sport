package com.kenick.sport.buyer.serviceImpl;

import com.kenick.sport.mapper.product.ColorMapper;
import com.kenick.sport.mapper.product.ProductMapper;
import com.kenick.sport.mapper.product.SkuMapper;
import com.kenick.sport.mapper.user.BuyerMapper;
import com.kenick.sport.pojo.buyer.BuyerCart;
import com.kenick.sport.pojo.buyer.BuyerItem;
import com.kenick.sport.pojo.product.Color;
import com.kenick.sport.pojo.product.Product;
import com.kenick.sport.pojo.product.Sku;
import com.kenick.sport.pojo.user.Buyer;
import com.kenick.sport.pojo.user.BuyerQuery;
import com.kenick.sport.service.buyer.BuyerService;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("buyerService")
public class BuyerServiceImpl implements BuyerService {
    @Resource
    private BuyerMapper buyerMapper;

    @Resource
    private SkuMapper skuMapper;

    @Resource
    private ProductMapper productMapper;

    @Resource
    private ColorMapper colorMapper;

    @Resource
    private Jedis jedis;

    @Override
    public Buyer selectBuyerByUsername(String username) {
        BuyerQuery buyerQuery = new BuyerQuery();
        buyerQuery.createCriteria().andUsernameEqualTo(username);
        List<Buyer> buyers = buyerMapper.selectByExample(buyerQuery);

        if(buyers != null && buyers.size()>0){
            return buyers.get(0);
        }
        return null;
    }

    @Override
    public BuyerCart addSkuToBuyerCart(Long skuId, Integer amount, BuyerCart buyerCart) {
        try {
            if(buyerCart == null){
                buyerCart = new BuyerCart();
            }

            // 购物项
            List<BuyerItem> buyerItems;
            if(buyerCart.getBuyerItems() == null){
                buyerItems = new ArrayList<>();
            }else{
                buyerItems = buyerCart.getBuyerItems();
            }

            // 添加购物项到购物车中
            Sku sku = skuMapper.selectByPrimaryKey(skuId);
            // 购物车中是否有该项
            boolean skuIdExist = false;
            for(BuyerItem buyerItem:buyerItems){
                if(buyerItem.getSkuId().equals(skuId)){ // 购物车中已有该商品
                    skuIdExist = true;
                    int selectAmount = buyerItem.getAmount() + amount;
                    buyerItem.setAmount(selectAmount);
                    if(selectAmount > sku.getStock()){
                        buyerItem.setHave(false); // 无货
                    }else{
                        buyerItem.setHave(true); // 有货
                    }
                    break;
                }
            }
            if(!skuIdExist){ // 购物车中无该商品
                Product product = productMapper.selectByPrimaryKey(sku.getProductId());
                Color color = colorMapper.selectByPrimaryKey(sku.getColorId());
                BuyerItem buyerItem = new BuyerItem();
                buyerItem.setSkuId(skuId);
                if(product.getImages() != null){
                    buyerItem.setProductUrl(product.getImages()[0]);
                }
                buyerItem.setProductName(URLEncoder.encode(product.getName(),"UTF-8"));
                buyerItem.setSkuColor(color.getName());
                buyerItem.setSkuSize(sku.getSize());
                buyerItem.setSkuPrice(sku.getPrice());

                // 是否有货判断
                int selectAmount = amount;
                buyerItem.setAmount(selectAmount);
                if(selectAmount > sku.getStock()){
                    buyerItem.setHave(false);
                }else{
                    buyerItem.setHave(true);
                }
                buyerItems.add(buyerItem);
            }
            buyerCart.setBuyerItems(buyerItems);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buyerCart;
    }

    @Override
    public List<BuyerItem> getBuyerItemListFromRedis(String redisKey) {
        List<BuyerItem> buyerItemList = new ArrayList<>();
        Map<String, String> skuMap = jedis.hgetAll(redisKey);
        Set<String> keySet = skuMap.keySet();
        for(String skuId:keySet){
            BuyerItem buyerItem = new BuyerItem();

            // 设置库存信息
            Sku sku = skuMapper.selectByPrimaryKey(Long.parseLong(skuId));
            buyerItem.setSkuId(sku.getId());
            buyerItem.setSkuPrice(sku.getPrice());
            buyerItem.setSkuSize(sku.getSize());
            Integer skuAmount = Integer.parseInt(skuMap.get(skuId));
            buyerItem.setAmount(skuAmount);
            if(skuAmount > sku.getStock()){
                buyerItem.setHave(false);
            }else{
                buyerItem.setHave(true);
            }

            // 设置商品信息
            Product product = productMapper.selectByPrimaryKey(sku.getProductId());
            buyerItem.setProductName(product.getName());
            if(product.getImages() != null){
                buyerItem.setProductUrl(product.getImages()[0]);
            }

            // 设置颜色信息
            Color color = colorMapper.selectByPrimaryKey(sku.getColorId());
            buyerItem.setSkuColor(color.getName());

            buyerItemList.add(buyerItem);
        }
        return buyerItemList;
    }

    @Override
    public Boolean saveSkuMapToRedis(String redisKey,Map<Long, Integer> skuMap) {
        boolean flag;
        try {
            Set<Long> skuIdSet = skuMap.keySet();
            for (Long skuId : skuIdSet) {
                Integer skuAmount = skuMap.get(skuId);
                jedis.hset(redisKey, skuId + "", skuAmount + "");
            }
            flag = true;
        }catch (Exception e){
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public Boolean saveStringToRedis(String redisKey, String redisValue) {
        String code = jedis.set(redisKey, redisValue);
        return "OK".equals(code);
    }

    @Override
    public String getStringFromRedis(String redisKey) {
        return jedis.get(redisKey);
    }

    @Override
    public String redisHget(String hKey, String mapKey) {
        String ret = jedis.hget(hKey, mapKey);
        return ret==null?"":ret;
    }

    @Override
    public Boolean redisHSet(String hKey, String mapKey, String mapValue) {
        jedis.hset(hKey, mapKey, mapValue);
        return true;
    }

    @Override
    public Long getOrderIdFromRedis() {
        return jedis.incr("orderId");
    }

    @Override
    public Long getBuyerIdByUsername(String username) {
        Long buyerId = null;
        BuyerQuery buyerQuery = new BuyerQuery();
        buyerQuery.createCriteria().andUsernameEqualTo(username);
        buyerQuery.setFields("id");
        buyerQuery.setPageNo(1);
        buyerQuery.setPageSize(1);
        List<Buyer> buyers = buyerMapper.selectByExample(buyerQuery);
        if(buyers.size() > 0){
            buyerId = buyers.get(0).getId();
        }
        return buyerId;
    }
}
