package com.neuedu.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * 日志文件(SpringAop)
 */
//@Component
//@Aspect
public class AspectCommon {

    //创建切入点表达式
    @Pointcut("execution(* com.neuedu.service.serviceImpl.ProductServiceImpl.*(..))")
    public void pointcut(){}

    //通知
    /**
     * 前置通知
     */
    @Before("pointcut()")
    public void before(){
        System.out.println("===========before===============");
    }
    /**
     * 后置通知
     */
    @After("pointcut()")
    public void after(){
        System.out.println("===========after===============");
    }
    /**
     * 异常通知
     */
    @AfterThrowing("pointcut()")
    public void afterThrowing(){
        System.out.println("===========afterThrowing===============");
    }

    /**
     * 最终通知
     */
    @AfterReturning("pointcut()")
    public void afterReturning(){
        System.out.println("===========afterReturning===============");
    }
    /**
     * 环绕通知
     */
//    @Around("pointcut()")
//    public Object around(ProceedingJoinPoint point){
//        Object proceed = null;
//        try {
//            System.out.println("============before=============");
//            //执行切入点匹配的方法
//            proceed = point.proceed();
//            System.out.println("==============after==============");
//        } catch (Throwable throwable) {
//            System.out.println("==============afterThrowing==============");
//            throwable.printStackTrace();
//        }
//        System.out.println("===========afterReturnning===============");
//        return proceed;
//    }
}
