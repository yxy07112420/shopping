package com.neuedu.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 封装一些redis的api
 */
@Component
public class RedisApi {
    @Autowired
    private JedisPool jedisPool;
    /**
     * set
     * @param key
     * @param value
     */
    public String set(String key,String value){
        String result = null;
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            result = jedis.set(key, value);
        }catch (Exception e){
            jedisPool.returnBrokenResource(jedis);
        }finally {
            if(null!=jedis){
                jedisPool.returnResource(jedis);
            }
        }
        return result;
    }
    /**
     * setex
     * @param key
     * @param value
     * @param seconds
     */
    public String setex(String key,int seconds,String value){
        String result = null;
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            result = jedis.setex(key,seconds, value);
        }catch (Exception e){
            jedisPool.returnBrokenResource(jedis);
        }finally {
            if(null!=jedis){
                jedisPool.returnResource(jedis);
            }
        }
        return result;
    }
    /**
     * get
     * @param key
     */
    public String get(String key){
        String result = null;
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            result = jedis.get(key);
        }catch (Exception e){
            jedisPool.returnBrokenResource(jedis);
        }finally {
            if(null!=jedis){
                jedisPool.returnResource(jedis);
            }
        }
        return result;
    }
    /**
     * del
     * @param key
     */
    public Long del(String key){
        Long result = null;
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            result = jedis.del(key);
        }catch (Exception e){
            jedisPool.returnBrokenResource(jedis);
        }finally {
            if(null!=jedis){
                jedisPool.returnResource(jedis);
            }
        }
        return result;
    }
    /**
     * expire
     * @param key
     * @param seconds
     */
    public Long expire(String key,int seconds){
        Long result = null;
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            result = jedis.expire(key,seconds);
        }catch (Exception e){
            jedisPool.returnBrokenResource(jedis);
        }finally {
            if(null!=jedis){
                jedisPool.returnResource(jedis);
            }
        }
        return result;
    }
}

