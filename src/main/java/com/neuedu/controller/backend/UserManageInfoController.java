package com.neuedu.controller.backend;

import com.neuedu.common.ResponseCord;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 后台管理员接口
 */
@RestController
@RequestMapping(value = "manage/user")
public class UserManageInfoController {

    @Autowired
    UserInfoService userInfoService;
    //登录
    @RequestMapping(value = "/login.do")
    public ServerResponse isLoginSuccess(HttpSession session, String username, String password){
        ServerResponse loginSuccess = userInfoService.isLoginSuccess(username, password);
        if(loginSuccess.isSuccess()){//登录成功
            UserInfo userInfo = (UserInfo) loginSuccess.getDate();
            //判断用户角色
            if(userInfo.getRole() != ResponseCord.UserEnum.USER_ADMIN.getCode()){
                return ServerResponse.responseIsError("当前用户没有权限登录");
            }
            //保存用户信息
            session.setAttribute(ResponseCord.CURRENTUSER,userInfo);
        }
        return loginSuccess;
    }
}
