package com.kenick.sport.service.buyer;

import com.kenick.sport.pojo.buyer.BuyerCart;
import com.kenick.sport.pojo.buyer.BuyerItem;
import com.kenick.sport.pojo.user.Buyer;

import java.util.List;
import java.util.Map;

public interface BuyerService {
    /**
     *  根据用户名查询用户信息
     * @param username 用户名
     * @return 用户信息
     */
    Buyer selectBuyerByUsername(String username);

    /**
     *  将对应amount数量的sku添加到购物车中，若购物车buyerCart中存在相同购物项，则进行合并
     * @param skuId sku id
     * @return 购物项
     */
    BuyerCart addSkuToBuyerCart(Long skuId, Integer amount, BuyerCart buyerCart);

    /**
     *  从redis中获取redisKey对应的hashmap，并将它转换为购物项集合
     * @param redisKey redis key
     * @return 购物项集合
     */
    List<BuyerItem> getBuyerItemListFromRedis(String redisKey);

    /**
     *  将库存信息保存到redis中
     * @return 是否成功
     */
    Boolean saveSkuMapToRedis(String redisKey,Map<Long,Integer> skuMap);

    /**
     *  将redisKey redisValue保存到redis中
     * @param redisKey redis中的key
     * @param redisValue redis中的value
     * @return 是否成功
     */
    Boolean saveStringToRedis(String redisKey,String redisValue);

    /**
     *  从redis中获取redisKey对应的value string值
     * @param redisKey redis中的key
     * @return redisKey对应的value值
     */
    String getStringFromRedis(String redisKey);
}
