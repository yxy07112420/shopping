package com.neuedu.controller.portal;

import com.neuedu.common.ResponseCord;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Shipping;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 地址模块
 */
@RestController
@RequestMapping(value="/address")
public class AddressController {

    @Autowired
    AddressService addressService;
    /**
     * 添加地址
     * @param session
     * @return
     */
    @RequestMapping(value = "/add.do")
    public ServerResponse add(HttpSession session, Shipping shipping){

        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCord.CURRENTUSER);
        if(userInfo == null){
            return ServerResponse.responseIsError("用户未登录或登录已失效");
        }
        return addressService.add(userInfo.getId(),shipping);
    }
    /**
     * 删除地址
     */
    @RequestMapping(value = "/del.do")
    public ServerResponse del(HttpSession session, Integer shippingId){

        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCord.CURRENTUSER);
        if(userInfo == null){
            return ServerResponse.responseIsError("用户未登录或登录已失效");
        }
        return addressService.del(userInfo.getId(),shippingId);
    }
    /**
     * 登录状态下更新地址
     */
    @RequestMapping(value = "/update.do")
    public ServerResponse update(HttpSession session, Shipping shipping){

        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCord.CURRENTUSER);
        if(userInfo == null){
            return ServerResponse.responseIsError("用户未登录或登录已失效");
        }
        shipping.setUserId(userInfo.getId());
        return addressService.update(shipping);
    }
    /**
     * 查看具体的地址
     */
    @RequestMapping(value = "/select.do")
    public ServerResponse select(HttpSession session, Integer shippingId){

        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCord.CURRENTUSER);
        if(userInfo == null){
            return ServerResponse.responseIsError("用户未登录或登录已失效");
        }
        return addressService.select(userInfo.getId(),shippingId);
    }
    /**
     * 查看地址列表
     */
    @RequestMapping(value = "/list.do")
    public ServerResponse list(HttpSession session,
                                 @RequestParam(required = false,defaultValue = "1") Integer pageNum,
                                 @RequestParam(required = false,defaultValue = "10") Integer pageSize){

        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCord.CURRENTUSER);
        if(userInfo == null){
            return ServerResponse.responseIsError("用户未登录或登录已失效");
        }
        return addressService.list(userInfo.getId(),pageNum,pageSize);
    }
}
