package com.neuedu.controller;

import com.neuedu.common.ServerResponse;
import com.neuedu.dao.UserInfoMapper;
import com.neuedu.json.JsonObjectMapperApi;
import com.neuedu.pojo.UserInfo;
import com.neuedu.redis.RedisApi;
import com.neuedu.redis.RedisProperties;
import org.apache.catalina.LifecycleState;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;

/**
 * controller的测试类
 */
@RestController
public class TestController {

    @Autowired
    RedisProperties redisProperties;
    //创建接口
    @Autowired
    UserInfoMapper userInfoMapper;
    //查询用户信息
    @RequestMapping(value = "/user/{userid}")
    public ServerResponse<UserInfo> getUser(@PathVariable Integer userid){
        UserInfo  userInfo =  userInfoMapper.selectByPrimaryKey(userid);
        if(userInfo != null){
            return ServerResponse.responseIsSuccess(null, userInfo);
        }else {
            return ServerResponse.responseIsError("error");
        }
    }
    @Autowired
    private JedisPool jedisPool;
    @RequestMapping("/redis")
    public String getJedis(){
        Jedis resource = jedisPool.getResource();
        String value = resource.set("root","root1");
        jedisPool.returnResource(resource);
        return value;
    }
    @Autowired
    private RedisApi redisApi;
    @RequestMapping("/key/{key}")
    public String getJedis(@PathVariable("key") String key){
        String value = redisApi.get(key);
        return value;
    }
    /**
     * json测试
     */
    @Autowired
    JsonObjectMapperApi jsonObjectMapperApi;
    @RequestMapping(value = "/json/{userid}")
    public ServerResponse<UserInfo> getUserByJson(@PathVariable Integer userid){
        UserInfo  userInfo =  userInfoMapper.selectByPrimaryKey(userid);
        String obj2String = jsonObjectMapperApi.obj2String(userInfo);
        System.out.println("o======="+obj2String);
        return ServerResponse.responseIsSuccess(null,userInfo);
    }
    @RequestMapping(value = "/jsonPretty/{userid}")
    public ServerResponse<UserInfo> getUserByJsonPretty(@PathVariable Integer userid){
        UserInfo  userInfo =  userInfoMapper.selectByPrimaryKey(userid);
        List<UserInfo> userInfoList = new ArrayList<UserInfo>();
        userInfoList.add(userInfo);
        String obj2String = jsonObjectMapperApi.obj2StringPretty(userInfoList);
        List userInfo1 = jsonObjectMapperApi.str2obj(obj2String, new TypeReference<List<UserInfo>>() {});
        System.out.println("o======="+userInfo1);
        return ServerResponse.responseIsSuccess(null,userInfo);
    }
}
