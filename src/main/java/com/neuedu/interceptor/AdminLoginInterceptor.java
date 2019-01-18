package com.neuedu.interceptor;

import com.google.gson.Gson;
import com.neuedu.common.ResponseCord;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 后台拦截器
 */
@Component
public class AdminLoginInterceptor implements HandlerInterceptor {
    @Autowired
    UserInfoService userInfoService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        //校验session中是否有用户信息
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute(ResponseCord.CURRENTUSER);
        //验证用户权限是否为管理员用户
        if(userInfo != null){
            System.out.println(userInfo);
            if(userInfo.getRole() != ResponseCord.UserEnum.USER_ADMIN.getCode()){
                response.reset();
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter writer = response.getWriter();
                ServerResponse serverResponse =  ServerResponse.responseIsError("用户权限错误");
                Gson gson = new Gson();
                String json = gson.toJson(serverResponse);
                writer.write(json);
                writer.flush();
                writer.close();
                return false;
            }
        }
        return true;
    }
}
