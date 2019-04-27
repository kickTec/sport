package com.kenick.sport.service.login;

public interface SessionProviderService {
    /**
     *  将用户信息保存到redis中
     * @param key  键值
     * @param username 用户名
     */
    void setAttributeForUsername(String key,String username);

    /**
     *  从redis中获取用户信息
     * @param key 键值
     * @return 用户信息
     */
    String getAttributeForUsername(String key);
}
