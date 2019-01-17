package com.neuedu.controller.backend;


import com.neuedu.common.ResponseCord;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 类别的控制层
 */
@RestController
@RequestMapping(value = "manage/category")
public class CategoryManageInfoController {

    @Autowired
    CategoryService categoryService;

    /**
     * 查询某一类别的子类别（同级，不含有后代）
     */
    @RequestMapping(value = "/get_category.do")
    public ServerResponse get_category(HttpSession session,Integer categoryId){
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCord.CURRENTUSER);
        //验证用户是否登录
        if(userInfo == null){
            return ServerResponse.responseIsError("用户未登录");
        }
        //验证用户权限是否为管理员用户
        if(userInfo.getRole() != ResponseCord.UserEnum.USER_ADMIN.getCode()){
            return ServerResponse.responseIsError("登录用户权限错误");
        }
        //查询子节点
        ServerResponse category = categoryService.get_category(categoryId);
        return category;
    }

    /**
     * 增加节点
     */
    @RequestMapping(value = "/add_category.do")
    public ServerResponse add_category(HttpSession session, @RequestParam(required = false,defaultValue = "0") Integer parentId, String categoryName){
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCord.CURRENTUSER);
        //验证用户是否登录
        if(userInfo == null){
            return ServerResponse.responseIsError("用户未登录");
        }
        //验证用户权限是否为管理员用户
        if(userInfo.getRole() != ResponseCord.UserEnum.USER_ADMIN.getCode()){
            return ServerResponse.responseIsError("登录用户权限错误");
        }
        //添加节点
        ServerResponse category = categoryService.add_category(parentId,categoryName);
        return category;
    }
    /**
     * 修改节点
     */
    @RequestMapping(value = "/set_category_name.do")
    public ServerResponse set_category_name(HttpSession session,Integer categoryId, String categoryName){
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCord.CURRENTUSER);
        //验证用户是否登录
        if(userInfo == null){
            return ServerResponse.responseIsError("用户未登录");
        }
        //验证用户权限是否为管理员用户
        if(userInfo.getRole() != ResponseCord.UserEnum.USER_ADMIN.getCode()){
            return ServerResponse.responseIsError("登录用户权限错误");
        }
        //修改节点信息
        ServerResponse category = categoryService.set_category_name(categoryId,categoryName);
        return category;
    }
    /**
     *获取当前类和他的后台类别
     */
    @RequestMapping(value = "/get_deep_category.do")
    public ServerResponse get_deep_category(HttpSession session,Integer categoryId){
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCord.CURRENTUSER);
        //验证用户是否登录
        if(userInfo == null){
            return ServerResponse.responseIsError("用户未登录");
        }
        //验证用户权限是否为管理员用户
        if(userInfo.getRole() != ResponseCord.UserEnum.USER_ADMIN.getCode()){
            return ServerResponse.responseIsError("登录用户权限错误");
        }
        //修改节点信息
        ServerResponse category = categoryService.get_deep_category(categoryId);
        return category;
    }
}
