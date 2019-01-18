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
 * 自动登录拦截器
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    UserInfoService  userInfoService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length > 0){
            for(int i = 0;i < cookies.length ;i++) {
                //根据获取到的参数名
                if (cookies[i].getName().equals("userInfo")) {
                    //根据参数名查询数据库
                    String token = cookies[i].getValue();
                    ServerResponse serverResponse = userInfoService.selectUserInfoByToken(token);
                    if (serverResponse.isSuccess()) {
                        UserInfo userInfo = (UserInfo) serverResponse.getDate();
                        HttpSession session = request.getSession();
                        userInfo.setPassword("");
                        session.setAttribute(ResponseCord.CURRENTUSER, userInfo);
                        return true;
                    }
                }
            }
        }
        //重构response
        response.reset();
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        ServerResponse serverResponse = ServerResponse.responseIsError(100,"用户未登录");
        Gson gson = new Gson();
        String s = gson.toJson(serverResponse);
        writer.write(s);
        writer.close();
        return false;
    }
}
