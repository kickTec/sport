package com.kenick.sport.cms.serviceImpl;

import com.kenick.sport.common.utils.JsonUtil;
import com.kenick.sport.mapper.ad.AdvertisingMapper;
import com.kenick.sport.mapper.ad.PositionMapper;
import com.kenick.sport.pojo.ad.Advertising;
import com.kenick.sport.pojo.ad.AdvertisingQuery;
import com.kenick.sport.pojo.ad.Position;
import com.kenick.sport.pojo.ad.PositionQuery;
import com.kenick.sport.service.product.AdService;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.List;

@Service("adService")
public class AdServiceImpl implements AdService {
    @Resource
    private PositionMapper positionMapper;

    @Resource
    private AdvertisingMapper advertisingMapper;

    @Resource
    private Jedis jedis;

    @Override
    public List<Position> selectPositionListByParentId(Long parentId) {
        PositionQuery positionQuery = new PositionQuery();
        positionQuery.createCriteria().andParentIdEqualTo(parentId);
        positionQuery.setOrderByClause("id asc");
        return positionMapper.selectByExample(positionQuery);
    }

    @Override
    public List<Advertising> selectAdListByPositionId(Long positionId) {
        AdvertisingQuery advertisingQuery = new AdvertisingQuery();
        advertisingQuery.createCriteria().andPositionIdEqualTo(positionId);
        advertisingQuery.setOrderByClause("id asc");
        List<Advertising> adList = advertisingMapper.selectByExample(advertisingQuery);
        for(Advertising ad:adList){
            Position position = positionMapper.selectByPrimaryKey(ad.getPositionId());
            ad.setPosition(position);
        }
        return adList;
    }

    @Override
    public void insertAd(Advertising advertising) {
        advertisingMapper.insert(advertising);
    }

    @Override
    public String selectAdListByPositionIdForPortal(Long positionId) {
        String adJson;
        String redisJson = jedis.get("adJson");
        if(redisJson == null || "".equals(redisJson)){
            AdvertisingQuery advertisingQuery = new AdvertisingQuery();
            advertisingQuery.createCriteria().andPositionIdEqualTo(positionId);
            advertisingQuery.setOrderByClause("id asc");
            List<Advertising> advertisingList = advertisingMapper.selectByExample(advertisingQuery);
            adJson = JsonUtil.parseObjectToJson(advertisingList);
            jedis.set("adJson",adJson,"NX","EX",3600);
        }

        return redisJson;
    }
}
