package com.neuedu.service.serviceImpl;

import com.neuedu.common.ResponseCord;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.UserInfoMapper;
import com.neuedu.pojo.UserInfo;
import com.neuedu.redis.RedisApi;
import com.neuedu.service.UserInfoService;
import com.neuedu.utils.MD5Utils;
import com.neuedu.utils.TokenCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.util.UUID;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    RedisApi redisApi;

    @Override
    public ServerResponse isLoginSuccess(String username, String password) {
        /**
         * 判断输入的用户名、密码是否为空
         */
        if(username == null || username.equals("")){
            return ServerResponse.responseIsError("用户名不能为空");
        }
        if(password == null || password.equals("")){
            return ServerResponse.responseIsError("密码不能为空");
        }
        /**
         * 判断用户名是否存在
         */
        int isExist = userInfoMapper.checkUsernameIsExist(username);
        //用户名不存在
        if(isExist == 0){
            return ServerResponse.responseIsError("该用户名不存在");
        }
        /**
         * 根据用户名和密码查询用户信息
         */
        UserInfo userInfo = userInfoMapper.loginByUsernameAndPassword(username, MD5Utils.getMD5Code(password));
        if(userInfo == null){
            return ServerResponse.responseIsError("用户名或密码错误");
        }
        /**
         * 返回结果
         */
//        userInfo.setPassword("");
        //保存用户信息
//        System.out.println(10/0);
        return ServerResponse.responseIsSuccess(null,userInfo);
    }

    @Override
    public ServerResponse isRegisterSuccess(UserInfo userInfo) {
        //验证参数是否存在
        if(userInfo == null){
            return ServerResponse.responseIsError("参数不能为空");
        }
        //验证用户名是否存在
        int isExist = userInfoMapper.checkUsernameIsExist(userInfo.getUsername());
        //用户名不存在
        if(isExist > 0){
            return ServerResponse.responseIsError("该用户名已存在");
        }
        //验证邮箱是否存在
        int emailIsExist = userInfoMapper.checkEmailIsExist(userInfo.getEmail());
        if(emailIsExist > 0){
            return ServerResponse.responseIsError("该邮箱已存在");
        }
        //验证注册是否成功
        userInfo.setRole(ResponseCord.UserEnum.USER_COMMON.getCode());
        //对密码进行md5盐值加密
        userInfo.setPassword(MD5Utils.getMD5Code(userInfo.getPassword()));
        int insert = userInfoMapper.insert(userInfo);
        if(insert == 0){
            return ServerResponse.responseIsError("注册失败");
        }
        return ServerResponse.responseIsSuccess("注册成功");
    }

    @Override
    public ServerResponse forget_user_question(String username) {
        //判断参数是否存在
        if(username==null || username.equals("")){
            return ServerResponse.responseIsError("用户名不能为空，请重新输入");
        }
        //判断用户名是否存在
        int usernameIsExist = userInfoMapper.checkUsernameIsExist(username);
        if(usernameIsExist == 0){
            return ServerResponse.responseIsError("该用户名不存在");
        }
        //根据用户名查询密保问题

        String question = userInfoMapper.forget_user_question(username);
        if(question == null || question.equals("")){
            return ServerResponse.responseIsError("密码问题为空");
        }
        return ServerResponse.responseIsSuccess(question);
    }

    @Override
    public ServerResponse forget_check_question(String username, String question, String answer) {
        //验证参数
        if(username==null || username.equals("")){
            return ServerResponse.responseIsError("用户名不能为空，请重新输入");
        }
        if(question==null || question.equals("")){
            return ServerResponse.responseIsError("密保问题不能为空，请重新输入");
        }
        if(answer==null || answer.equals("")){
            return ServerResponse.responseIsError("答案不能为空，请重新输入");
        }
        //校验
        int checkQuestion = userInfoMapper.forget_check_question(username, question, answer);
        if(checkQuestion == 0){
            //答案错误
            return ServerResponse.responseIsError("答案错误");
        }
        //在服务端产生一个token，并将token返回给客户端
        String token = UUID.randomUUID().toString();
        redisApi.setex(username,1800,token);
//        TokenCache.set(username,token);
        return ServerResponse.responseIsSuccess("",token);
    }
    @Override
    public ServerResponse forget_checkUser_password(String username, String newPassword, String token) {
        //校验参数
        if(username==null || username.equals("")){
            return ServerResponse.responseIsError("用户名不能为空，请重新输入");
        }
        if(newPassword==null || newPassword.equals("")){
            return ServerResponse.responseIsError("密保问题不能为空，请重新输入");
        }
        if(token==null || token.equals("")){
            return ServerResponse.responseIsError("答案不能为空，请重新输入");
        }
        //判读用户是否横向越权校验
//        String forgenToken = TokenCache.get(username);
        String forgenToken = redisApi.get(username);
        if(forgenToken == null){
            return ServerResponse.responseIsError("token已失效");
        }
        if(!forgenToken.equals(token)){
            return ServerResponse.responseIsError("无效token");
        }
        //修改密码
        //对密码进行加密设置
        String password = MD5Utils.getMD5Code(newPassword);
        int updateCount = userInfoMapper.updateUserPasswordByUsername(username, password);
        if (updateCount == 0){
            return ServerResponse.responseIsError("修改失败");
        }
        return ServerResponse.responseIsSuccess("修改成功");
    }

    @Override
    public ServerResponse check_valid(String str, String type) {
        //参数非空校验
        if(str==null || str.equals("")){
            return ServerResponse.responseIsError("用户名或邮箱不能为空");
        }
        if(type==null || type.equals("")){
            return ServerResponse.responseIsError("校验参数类型不能为空");
        }
        //校验参数（type==>uaername||type==>email）
        if(type.equals("username")){
            //参数类型为用户
            int result = userInfoMapper.checkUsernameIsExist(str);
            if(result == 0){
                return ServerResponse.responseIsSuccess();
            }
            if(result > 0){
                return ServerResponse.responseIsError("该用户名已存在");
            }
        }else if (type.equals("email")){
            //参数类型为email
            int result = userInfoMapper.checkEmailIsExist(str);
            if(result == 0){
                return ServerResponse.responseIsSuccess();
            }
            if(result > 0){
                return ServerResponse.responseIsError("该邮箱已存在");
            }
        }else {
            return ServerResponse.responseIsError("参数类型错误");
        }
        return null;
    }

    @Override
    public ServerResponse reset_password(String username, String passwordOld, String passwordNew) {
        //参数的验证
        if(passwordOld==null || passwordOld.equals("")){
            return ServerResponse.responseIsError("用户的旧密码不能为空");
        }
        if(passwordNew==null || passwordNew.equals("")){
            return ServerResponse.responseIsError("用户的新密码不能为空");
        }
        if(username==null || username.equals("")){
            return ServerResponse.responseIsError("用户名不能为空");
        }
        //验证用户名+旧密码
        UserInfo userInfo = userInfoMapper.loginByUsernameAndPassword(username,MD5Utils.getMD5Code(passwordOld));
        if(userInfo == null){
            return ServerResponse.responseIsError("旧密码错误");
        }
        //修改密码
        //修改旧密码
        userInfo.setPassword(MD5Utils.getMD5Code(passwordNew));
        int updatePass = userInfoMapper.updateByPrimaryKey(userInfo);
        if (updatePass == 0){
            return ServerResponse.responseIsError("修改密码失败");
        }
        return ServerResponse.responseIsSuccess("修改密码成功",userInfo);
    }

    @Override
    public ServerResponse update_user_info(UserInfo user) {
        //校验参数
        if(user == null){
            return ServerResponse.responseIsError("用户个人信息不能为空");
        }
        //更新用户个人信息
        int updateUserInfo = userInfoMapper.updateUserInfo(user);
        if(updateUserInfo == 0){
            return ServerResponse.responseIsError("更新用户信息失败");
        }
        return ServerResponse.responseIsSuccess("更新用户信息成功");
    }

    @Override
    public UserInfo selectUserInfoByUserId(Integer userId) {
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);
        return userInfo;
    }

    @Override
    public ServerResponse selectUserInfoByToken(String token) {
        UserInfo userInfo = userInfoMapper.selectUserInfoByToken(token);
        System.out.println(userInfo);
        if(userInfo == null){
            return ServerResponse.responseIsError("登录信息错误，请重新登录");
        }
        return ServerResponse.responseIsSuccess(null,userInfo);
    }
}
