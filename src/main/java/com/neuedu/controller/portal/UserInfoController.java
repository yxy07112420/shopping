package com.neuedu.controller.portal;

import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
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
 * user用户的控制层
 */
@RestController
@RequestMapping(value = "/portal/user")
public class UserInfoController {

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
    @RequestMapping(value = "/forget_user_question.do/{username}")
    public ServerResponse forget_user_question(@PathVariable("username") String username){
        ServerResponse registerSuccess = userInfoService.forget_user_question(username);
        return registerSuccess;
    }
    //校验密保问题
    @RequestMapping(value = "/forget_check_question.do/{username}/{question}/{answer}")
    public ServerResponse forget_check_question(@PathVariable("username") String username,
                                                @PathVariable("question") String question,
                                                @PathVariable("answer") String answer){
        ServerResponse registerSuccess = userInfoService.forget_check_question(username,question,answer);
        return registerSuccess;
    }
    //忘记密码修改密码
    @RequestMapping(value = "/forget_checkUser_password.do/{username}/{newPassword}/{token}")
    public ServerResponse forget_checkUser_password(@PathVariable("username") String username,
                                                    @PathVariable("newPassword") String newPassword,
                                                    @PathVariable("token") String token){
        ServerResponse registerSuccess = userInfoService.forget_checkUser_password(username,newPassword,token);
        return registerSuccess;
    }
    //检查用户名||邮箱是否有效
    @RequestMapping(value = "/check_valid.do/{str}/{type}")
    public ServerResponse check_valid(@PathVariable("str") String str,@PathVariable("type") String type){
        ServerResponse registerSuccess = userInfoService.check_valid(str,type);
        return registerSuccess;
    }
    //获取用户登录信息
    @RequestMapping(value = "/get_user_info.do")
    public ServerResponse get_user_info(HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCord.CURRENTUSER);
        UserInfo userInfo1 = userInfo;
        userInfo1.setPassword("");
        userInfo1.setAnswer("");
        userInfo1.setQuestion("");
        userInfo1.setRole(null);
        return ServerResponse.responseIsSuccess(null,userInfo1);
    }
    //登录状态下重置密码
    @RequestMapping(value = "/reset_password.do/{passwordOld}/{passwordNew}")
    public ServerResponse reset_password(HttpSession session,
                                         @PathVariable("passwordOld") String passwordOld,
                                         @PathVariable("passwordNew") String passwordNew){
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCord.CURRENTUSER);
        return userInfoService.reset_password(userInfo.getUsername(),passwordOld,passwordNew);
    }
    //更新个人信息
    @RequestMapping(value = "/update_user_info.do")
    public ServerResponse update_user_info(HttpSession session, UserInfo user){
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCord.CURRENTUSER);
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
