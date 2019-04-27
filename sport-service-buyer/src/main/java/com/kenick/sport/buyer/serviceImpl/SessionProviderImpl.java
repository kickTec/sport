package com.kenick.sport.buyer.serviceImpl;


import com.kenick.sport.common.constant.LoginConstant;
import com.kenick.sport.service.login.SessionProviderService;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;

public class SessionProviderImpl implements SessionProviderService {

    @Resource
    private Jedis jedis;

    private Integer userExpires = LoginConstant.REDIS_EXPIRES;
    public void setUserExpires(Integer userExpires){
        this.userExpires = userExpires;
    }

    @Override
    public void setAttributeForUsername(String key, String username) {
        jedis.set(LoginConstant.USER_SESSION+":"+key, username,"NX","EX",userExpires);
    }

    @Override
    public String getAttributeForUsername(String key) {
        String username = jedis.get(LoginConstant.USER_SESSION + ":" + key);
        if(username != null){
            jedis.expire(LoginConstant.USER_SESSION+":"+key,userExpires);
            return username;
        }
        return null;
    }
}
