package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import org.springframework.stereotype.Service;


/**
 * user用户的业务层
 */
@Service
public interface UserInfoService {

    //根据用户名和密码验证用户登录
    ServerResponse isLoginSuccess(String username,String password);
    //注册
    ServerResponse isRegisterSuccess(UserInfo userInfo);
    //根据用户名查询密保问题
    ServerResponse forget_user_question(String username);
    //根据用户名、密保问题和答案进行校验
    ServerResponse forget_check_question(String username,String question,String answer);
    //忘记密码，修改密码
    ServerResponse forget_checkUser_password(String username,String newPassword,String token);
    //验证用户名或邮箱是否有效
    ServerResponse check_valid(String str,String type);
    //登录状态下重置密码
    ServerResponse reset_password(String username,String passwordOld, String passwordNew);
    //更新用户个人信息
    ServerResponse update_user_info(UserInfo user);
    //根据哦用户id查询用户信息
    UserInfo selectUserInfoByUserId(Integer userId);
}
