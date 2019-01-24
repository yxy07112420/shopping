package com.neuedu.controller.backend;

import com.neuedu.common.ResponseCord;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.UserInfoMapper;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.UserInfoService;
import com.neuedu.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 后台管理员接口
 */
@RestController
@RequestMapping(value = "manage/user")
public class UserManageInfoController {
    @Autowired
    UserInfoService userInfoService;
     @Autowired
    UserInfoMapper userInfoMapper;
    //登录
    @RequestMapping(value = "/login.do/{username}/{password}")
    public ServerResponse isLoginSuccess(HttpSession session, HttpServletResponse response,
                                         @PathVariable("username") String username,
                                         @PathVariable("password") String password){
        ServerResponse loginSuccess = userInfoService.isLoginSuccess(username, password);
        if(loginSuccess.isSuccess()){//登录成功
            UserInfo userInfo = (UserInfo) loginSuccess.getData();
            String token =  MD5Utils.getMD5Code(userInfo.getUsername()+userInfo.getPassword());
            Cookie cookie = new Cookie("userInfo",token);
            cookie.setMaxAge(1800);
            cookie.setPath("/");
            response.addCookie(cookie);
            //将token保存
            userInfo.setToken(token);
            int i = userInfoMapper.updateUserInfo(userInfo);
            if(i == 0){
                return ServerResponse.responseIsError("更新失败，登录错误");
            }
            userInfo.setPassword("");
            //保存用户信息
            session.setAttribute(ResponseCord.CURRENTUSER,userInfo);
        }
        return loginSuccess;
    }
}
