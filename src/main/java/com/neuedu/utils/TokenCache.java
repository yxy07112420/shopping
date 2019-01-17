package com.neuedu.utils;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class TokenCache {
    private static LoadingCache<String,String> loadingCache= CacheBuilder.newBuilder()
            .initialCapacity(1000)//初始化缓存为100
            .maximumSize(10000)//设置缓存项最大值为10000
            .expireAfterAccess(12, TimeUnit.HOURS)//设置回收时间为12小时
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String s) throws Exception {
                    return "null";
                }
            });
    public static void set(String key,String value){
        loadingCache.put(key,value);
    }
    public static String get(String key){
        String value = null;
        try {
            value = loadingCache.get(key);
            if ("null".equals(value)){
                return null;
            }
            return value;
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
