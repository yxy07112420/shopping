package com.neuedu.controller.portal;

import com.neuedu.common.ResponseCord;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 购物车控制层
 */
@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    CartService cartService;

    /**
     * 添加购物车
     * @param session
     * @param productId
     * @param count
     * @return
     */
    @RequestMapping(value = "/add.do")
    public ServerResponse add(HttpSession session,Integer productId,Integer count){
        //验证是否有用户登录

        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCord.CURRENTUSER);
        if(userInfo == null){
            return ServerResponse.responseIsError("当前无用户登录或用户登录已超时");
        }


        return cartService.add(userInfo.getId(),productId,count);

    }
    /**
     * 查询购物车列表
     * @param session
     * @return
     */
    @RequestMapping(value = "/list.do")
    public ServerResponse list(HttpSession session){
        //验证是否有用户登录
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCord.CURRENTUSER);
        if(userInfo == null){
            return ServerResponse.responseIsError("当前无用户登录或用户登录已超时");
        }
        return cartService.list(userInfo.getId());
    }

    /**
     * 更新购物车中商品数量
     * @param session
     * @return
     */
    @RequestMapping(value = "/update.do")
    public ServerResponse update(HttpSession session,Integer productId,Integer count){
        //验证是否有用户登录
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCord.CURRENTUSER);
        if(userInfo == null){
            return ServerResponse.responseIsError("当前无用户登录或用户登录已超时");
        }
        return cartService.update(userInfo.getId(),productId,count);
    }
    /**
     * 删除某一购物信息
     * @param session
     * @return
     */
    @RequestMapping(value = "/update_cart.do")
    public ServerResponse update_cart(HttpSession session,String productIds){
        //验证是否有用户登录
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCord.CURRENTUSER);
        if(userInfo == null){
            return ServerResponse.responseIsError("当前无用户登录或用户登录已超时");
        }
        return cartService.update_cart(userInfo.getId(),productIds);
    }

    /**
     * 选中某件商品
     * @param session
     * @return
     */
    @RequestMapping(value = "/select.do")
    public ServerResponse select(HttpSession session,Integer productId){
        //验证是否有用户登录
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCord.CURRENTUSER);
        if(userInfo == null){
            return ServerResponse.responseIsError("当前无用户登录或用户登录已超时");
        }
        return cartService.select(userInfo.getId(),productId, ResponseCord.CartProductEnum.PRODUCT_ISCHECKED.getCode());
    }
    /**
     * 取消选中某件商品
     * @param session
     * @return
     */
    @RequestMapping(value = "/un_select.do")
    public ServerResponse un_select(HttpSession session,Integer productId){
        //验证是否有用户登录
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCord.CURRENTUSER);
        if(userInfo == null){
            return ServerResponse.responseIsError("当前无用户登录或用户登录已超时");
        }
        return cartService.select(userInfo.getId(),productId, ResponseCord.CartProductEnum.PRODUCT_NOCHECKED.getCode());
    }
    /**
     *全选
     * @param session
     * @return
     */
    @RequestMapping(value = "/selectAll.do")
    public ServerResponse selectAll(HttpSession session,Integer productId){
        //验证是否有用户登录
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCord.CURRENTUSER);
        if(userInfo == null){
            return ServerResponse.responseIsError("当前无用户登录或用户登录已超时");
        }
        return cartService.select(userInfo.getId(),null, ResponseCord.CartProductEnum.PRODUCT_ISCHECKED.getCode());
    }
    /**
     * 取消全选
     * @param session
     * @return
     */
    @RequestMapping(value = "/un_selectAll.do")
    public ServerResponse un_selectAll(HttpSession session,Integer productId){
        //验证是否有用户登录
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCord.CURRENTUSER);
        if(userInfo == null){
            return ServerResponse.responseIsError("当前无用户登录或用户登录已超时");
        }
        return cartService.select(userInfo.getId(),null, ResponseCord.CartProductEnum.PRODUCT_NOCHECKED.getCode());
    }
    /**
     * 统计购物车中产品的数量
     */
    @RequestMapping(value = "/select_cart_product_count.do")
    public ServerResponse select_cart_product_count(HttpSession session){
        //验证是否有用户登录
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCord.CURRENTUSER);
        if(userInfo == null){
            return ServerResponse.responseIsError("当前无用户登录或用户登录已超时");
        }
        return cartService.select_cart_product_count(userInfo.getId());
    }

}
