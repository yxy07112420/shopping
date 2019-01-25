package com.neuedu.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
     * hashset
     * @param key
     * @param value
     * @return
     */
    public Long hashSet(String key,String filed,String value){
        Long result = null;
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            result = jedis.hset(key, filed, value);
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
     * 根据key值获取所有的filed值
     * @param key
     * @return
     */
    public Set hashGetFiled(String key){
        Set result = null;
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            result = jedis.hkeys(key);
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
     * 判断filed是否存在
     * @param key
     * @param filed
     * @return
     */
    public Boolean hexists(String key,String filed){
        Boolean result = false;
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            result = jedis.hexists(key,filed);
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
     * 获取filed对应的value值
     * @param key
     * @param filed
     * @return
     */
    public List<String>  hmget(String key,String filed){
        List<String> result = new ArrayList<String>();
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            result = jedis.hmget(key,filed);
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
     * 删除key对应的filed的value
     * @param key
     * @param filed
     * @return
     */
    public Long delValue(String key,String filed){
        Long result = null;
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            result = jedis.hdel(key, filed);
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

    /**
     * 清除缓存
     */
    public void flushdb(){
        Jedis jedis = jedisPool.getResource();
        String flushDB = jedis.flushDB();
    }
}

