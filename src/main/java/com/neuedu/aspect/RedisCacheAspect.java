package com.neuedu.aspect;

import com.neuedu.common.ServerResponse;
import com.neuedu.dao.ProductMapper;
import com.neuedu.json.JsonObjectMapperApi;
import com.neuedu.pojo.Product;
import com.neuedu.redis.RedisApi;
import com.neuedu.service.ProductService;
import com.neuedu.utils.MD5Utils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * redis缓存aop
 */
@Component
@Aspect
public class RedisCacheAspect {
    @Autowired
    RedisApi redisApi;
    @Autowired
    JsonObjectMapperApi jsonObjectMapperApi;
    //创建一个切入点
    @Pointcut("execution(* com.neuedu.service.serviceImpl.ProductServiceImpl.*(..))")
    public void pointcut(){}

    //通知（环绕通知）
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint){
        Object o = null;
        try {
            //key:MD5(全类名+方法名+参数)
            StringBuffer stringBuffer = new StringBuffer();
            //获取目标的类名
            Object className = joinPoint.getTarget().getClass().getName();
            stringBuffer.append(className);
            //获取目标方法的方法名
            String methodName = joinPoint.getSignature().getName();
            stringBuffer.append(methodName);
            //方法中的参数
            Object[] objects = joinPoint.getArgs();
            if(objects != null){
                for (Object object:objects) {
                    stringBuffer.append(object);
                }
            }
            String key = MD5Utils.getMD5Code(stringBuffer.toString());
            //根据key值获取缓存
            String values = redisApi.get(key);
            if(methodName.startsWith("update") || methodName.startsWith("set")){
                //数据更新，需要清除缓存

            }
            if(values != null && !values.equals("")){
                System.out.println("=================读取到了缓存==================");
                return jsonObjectMapperApi.str2obj(values, ServerResponse.class);
            }
            //执行目标方法
            o = joinPoint.proceed();
            System.out.println("====读取数据库=====");
            if(o != null){
                String jsoncache = jsonObjectMapperApi.obj2String(o);
                System.out.println("====将数据库的内容写入到缓存=====");
                redisApi.set(key,jsoncache);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return o;
    }
}
