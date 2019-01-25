package com.neuedu.interceptor.interceptorDemo;

import com.neuedu.interceptor.AdminLoginInterceptor;
import com.neuedu.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

//@SpringBootConfiguration
public class LoginInterceptorConfig implements WebMvcConfigurer {
    @Autowired
    LoginInterceptor loginFilter;
    @Autowired
    AdminLoginInterceptor adminLoginInterceptor;
    /**
     * 自定义拦截器
     * 继承WebMvcConfigurerAdapter，重写addInterceptors方法
     * 添加自定义拦截器和拦截路径，此处对所有请求进行拦截，除了登录界面和登录接口
     * addPathPatterns方法用于添加拦截路径，excludePathPatterns方法用于添加不需要拦截的路径
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //前端用户
        List<String> portalExclude = new ArrayList<String>();
        portalExclude.add("/portal/user/login.do/**");
        portalExclude.add("/portal/product/**");
        portalExclude.add("/portal/user/register.do/**");
        portalExclude.add("/portal/user/forget_user_question.do/**");
        portalExclude.add("/portal/user/forget_check_question.do/**");
        portalExclude.add("/portal/user/forget_checkUser_password.do/**");
        portalExclude.add("/portal/user/check_valid.do/**");
        portalExclude.add("/manage/user/login.do/**");
        registry.addInterceptor(loginFilter).addPathPatterns("/**").excludePathPatterns(portalExclude);
        //管理员用户
        registry.addInterceptor(adminLoginInterceptor).addPathPatterns("/manage/**");
    }
}
