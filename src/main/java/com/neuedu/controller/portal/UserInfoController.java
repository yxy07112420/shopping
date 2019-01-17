package com.neuedu.controller.portal;

import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import com.neuedu.common.ResponseCord;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * user用户的控制层
 */
@RestController
@RequestMapping(value = "/user")
public class UserInfoController {

    @Autowired
    UserInfoService userInfoService;
    //登录
    @RequestMapping(value = "/login.do")
    public ServerResponse isLoginSuccess(HttpSession session,String username, String password){
        ServerResponse loginSuccess = userInfoService.isLoginSuccess(username, password);
        if(loginSuccess.isSuccess()){//登录成功
            UserInfo userInfo = (UserInfo) loginSuccess.getDate();
            //保存用户信息
            session.setAttribute(ResponseCord.CURRENTUSER,userInfo);
        }
        return loginSuccess;
    }
    //注册
    @RequestMapping(value = "/register.do")
    public ServerResponse isRegisterSuccess(HttpSession session,UserInfo userInfo){
        ServerResponse registerSuccess = userInfoService.isRegisterSuccess(userInfo);
        return registerSuccess;
    }
    //根据用户名获取密码问题
    @RequestMapping(value = "/forget_user_question.do")
    public ServerResponse forget_user_question(String username){
        ServerResponse registerSuccess = userInfoService.forget_user_question(username);
        return registerSuccess;
    }
    //校验密保问题
    @RequestMapping(value = "/forget_check_question.do")
    public ServerResponse forget_check_question(String username,String question,String answer){
        ServerResponse registerSuccess = userInfoService.forget_check_question(username,question,answer);
        return registerSuccess;
    }
    //忘记密码修改密码
    @RequestMapping(value = "/forget_checkUser_password.do")
    public ServerResponse forget_checkUser_password(String username,String newPassword,String token){
        ServerResponse registerSuccess = userInfoService.forget_checkUser_password(username,newPassword,token);
        return registerSuccess;
    }
    //检查用户名||邮箱是否有效
    @RequestMapping(value = "/check_valid.do")
    public ServerResponse check_valid(String str,String type){
        ServerResponse registerSuccess = userInfoService.check_valid(str,type);
        return registerSuccess;
    }
    //获取用户登录信息
    @RequestMapping(value = "/get_user_info.do")
    public ServerResponse get_user_info(HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCord.CURRENTUSER);
        if(userInfo == null){
            return ServerResponse.responseIsError("用户未登录");
        }
        UserInfo userInfo1 = userInfo;
        userInfo1.setPassword("");
        userInfo1.setAnswer("");
        userInfo1.setQuestion("");
        userInfo1.setRole(null);
        return ServerResponse.responseIsSuccess(null,userInfo1);
    }
    //登录状态下重置密码
    @RequestMapping(value = "/reset_password.do")
    public ServerResponse reset_password(HttpSession session, String passwordOld,String passwordNew){
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCord.CURRENTUSER);
        if(userInfo == null){
            return ServerResponse.responseIsError("用户未登录");
        }
        return userInfoService.reset_password(userInfo.getUsername(),passwordOld,passwordNew);
    }
    //更新个人信息
    @RequestMapping(value = "/update_user_info.do")
    public ServerResponse update_user_info(HttpSession session, UserInfo user){
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCord.CURRENTUSER);
        if(userInfo == null){
            return ServerResponse.responseIsError("用户未登录");
        }
        user.setId(userInfo.getId());
        ServerResponse serverResponse = userInfoService.update_user_info(user);
        if(serverResponse.isSuccess()){
            //根据userid查询用户信息
            //更新session中的用户信息
            UserInfo userInfo1 = userInfoService.selectUserInfoByUserId(userInfo.getId());
            session.setAttribute(ResponseCord.CURRENTUSER,userInfo1);
        }
        return serverResponse;
    }
    //获取用户详细信息
    @RequestMapping(value = "/get_user_allInfos.do")
    public ServerResponse get_user_allInfos(HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCord.CURRENTUSER);
        if(userInfo == null){
            return ServerResponse.responseIsError("用户未登录");
        }
        UserInfo userInfo1 = userInfo;
        userInfo1.setPassword("");
        return ServerResponse.responseIsSuccess(null,userInfo1);
    }
    //退出登录
    @RequestMapping(value = "/logout.do")
    public ServerResponse logout(HttpSession session){
        session.removeAttribute(ResponseCord.CURRENTUSER);
        return ServerResponse.responseIsSuccess();
    }
}
