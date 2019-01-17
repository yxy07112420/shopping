package com.neuedu.controller.backend;

import com.neuedu.common.ResponseCord;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Product;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpSession;
/**
 * 商品后台控制层
 */
@RestController
@RequestMapping(value = "manage/product")
public class ProductManageInfoController {

    @Autowired
    ProductService productService;
    /**
     * 新增|更新商品信息
     */
    @RequestMapping(value = "/updateOrInsertProduct.do")
    public ServerResponse updateOrInsertProduct(HttpSession session, Product product){
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCord.CURRENTUSER);
        //验证用户是否登录
        if(userInfo == null){
            return ServerResponse.responseIsError("用户未登录");
        }
        //验证用户权限是否为管理员用户
        if(userInfo.getRole() != ResponseCord.UserEnum.USER_ADMIN.getCode()){
            return ServerResponse.responseIsError("登录用户权限错误");
        }
        return productService.updateOrInsertProduct(product);
    }
    /**
     * 商品上下架接口
     */
    @RequestMapping(value = "/set_product_status.do")
    public ServerResponse set_product_status(HttpSession session,Integer productId,Integer status){
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCord.CURRENTUSER);
        //验证用户是否登录
        if(userInfo == null){
            return ServerResponse.responseIsError("用户未登录");
        }
        //验证用户权限是否为管理员用户
        if(userInfo.getRole() != ResponseCord.UserEnum.USER_ADMIN.getCode()){
            return ServerResponse.responseIsError("登录用户权限错误");
        }
        return productService.set_product_status(productId,status);
    }
    /**
     * 查看商品的详情（根据商品id）
     */
    @RequestMapping(value = "/detail.do")
    public ServerResponse detail(HttpSession session,Integer productId){
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCord.CURRENTUSER);
        //验证用户是否登录
        if(userInfo == null){
            return ServerResponse.responseIsError("用户未登录");
        }
        //验证用户权限是否为管理员用户
        if(userInfo.getRole() != ResponseCord.UserEnum.USER_ADMIN.getCode()){
            return ServerResponse.responseIsError("登录用户权限错误");
        }
        return productService.detail(productId);
    }
    /**
     * 分页查询商品信息
     */
    @RequestMapping(value = "/list.do")
    public ServerResponse list(HttpSession session, @RequestParam(required = false,defaultValue = "1")Integer pageNum,@RequestParam(required = false,defaultValue = "10")Integer pageSize){
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCord.CURRENTUSER);
        //验证用户是否登录
        if(userInfo == null){
            return ServerResponse.responseIsError("用户未登录");
        }
        //验证用户权限是否为管理员用户
        if(userInfo.getRole() != ResponseCord.UserEnum.USER_ADMIN.getCode()){
            return ServerResponse.responseIsError("登录用户权限错误");
        }
        return productService.list(pageNum,pageSize);
    }
    /**
     * 商品搜索（根据商品id|商品名）
     */
    @RequestMapping(value = "/search.do")
    public ServerResponse search(HttpSession session,
                                 @RequestParam(required = false)Integer productId,
                                 @RequestParam(required = false)String productName,
                                 @RequestParam(required = false,defaultValue = "1")Integer pageNum,
                                 @RequestParam(required = false,defaultValue = "10")Integer pageSize){
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCord.CURRENTUSER);
        //验证用户是否登录
        if(userInfo == null){
            return ServerResponse.responseIsError("用户未登录");
        }
        //验证用户权限是否为管理员用户
        if(userInfo.getRole() != ResponseCord.UserEnum.USER_ADMIN.getCode()){
            return ServerResponse.responseIsError("登录用户权限错误");
        }
        return productService.search(productId,productName,pageNum,pageSize);
    }
}
