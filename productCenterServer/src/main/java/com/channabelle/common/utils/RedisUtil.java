package com.channabelle.common.utils;

import redis.clients.jedis.Jedis;

import java.util.UUID;

public class RedisUtil {
    private static Jedis client = null;
    private final static String SERVER_ADDRESS = "localhost";
    private final static int DEFAULT_EXPIRE_SECOND = 60 * 10;

    private Jedis newClient() {
        return new Jedis(SERVER_ADDRESS);
    }

    private Jedis getClient() {
        if(null == client) {
            client = newClient();
        }
        return client;
    }

    public String getValue(String key) {
        getClient().expire(key, DEFAULT_EXPIRE_SECOND);
        return getClient().get(key);
    }

    public String setValue(String key, String value) {
        return getClient().setex(key, DEFAULT_EXPIRE_SECOND, value);
    }

    public String addValue(String value) {
        String key = UUID.randomUUID().toString();
        getClient().setex(key, DEFAULT_EXPIRE_SECOND, value);
        return key;
    }

    public long delKey(String key) {
        return getClient().del(key);
    }

    public boolean existKey(String key) {
        return getClient().exists(key);
    }

    public static void main(String[] args) {
        // 连接本地的 Redis 服务
        // (new RedisUtil()).setValue("aaa", "bbb");
        // (new RedisUtil()).getValue("aaa");

    }
}
