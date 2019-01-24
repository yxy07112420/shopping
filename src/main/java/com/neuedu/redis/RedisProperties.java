package com.neuedu.redis;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 创建连接池的实例
 */
@Data
@Component
@ConfigurationProperties
public class RedisProperties {
    //最大连接数
    @Value("${redis.max.total}")
    private int maxTotal;
     //最大空闲数
     @Value("${redis.max.idle}")
    private int maxIdle;
    //最小空闲数
    @Value("${redis.min.idle}")
    private int minIdle;
    //ip
    @Value("${redis.ip}")
    private String redisIp;
    //获取实例时，校验实例书否有效
    @Value("${redis.test.borrow}")
    private boolean testBorrow;
    //在把redis实例放回到连接池时，检查实例是否有效
    @Value("${redis.test.return}")
    private boolean testReturn;
    //连接密码
    @Value("${redis.password}")
    private String redisPassword;
    //端口
    @Value("${redis.port}")
    private int redisPort;
}
