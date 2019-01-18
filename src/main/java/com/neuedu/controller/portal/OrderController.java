package com.neuedu.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.neuedu.common.ResponseCord;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.OrderService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *订单模块
 */
@RestController
@RequestMapping(value = "/portal/order")
public class OrderController {
    @Autowired
    OrderService orderService;
    /**
     * 创建订单
     */
    @RequestMapping(value = "/create.do")
    public ServerResponse create(HttpSession session,Integer shippingId){
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCord.CURRENTUSER);
        return orderService.create(userInfo.getId(),shippingId);
    }
    /**
     * 取消订单
     */
    @RequestMapping(value = "/cancel.do")
    public ServerResponse cancel(HttpSession session,Long orderNo){
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCord.CURRENTUSER);
        return orderService.cancel(userInfo.getId(),orderNo);
    }
    /**
     *获取订单的商品信息
     */
    @RequestMapping(value = "/get_order_cart_product.do")
    public ServerResponse get_order_cart_product(HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCord.CURRENTUSER);
        return orderService.get_order_cart_product(userInfo.getId());
    }
    /**
     * 查询订单，分页查询
     */
    @RequestMapping(value = "/list.do")
    public ServerResponse list(HttpSession session,
                               @RequestParam(required = false,defaultValue = "1")Integer pageNum,
                               @RequestParam(required = false,defaultValue = "10")Integer pageSize){
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCord.CURRENTUSER);
        return orderService.list(userInfo.getId(),pageNum,pageSize);
    }
    /**
     * 获取订单详情
     */
    @RequestMapping(value = "/detail.do")
    public ServerResponse detail(HttpSession session,Long orderNo){
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCord.CURRENTUSER);
        return orderService.detail(userInfo.getId(),orderNo);
    }
    /**
     * 支付接口
     */
    @RequestMapping(value = "/pay.do")
    public ServerResponse pay(HttpSession session,Long orderNo){
        //验证是否有用户登录
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCord.CURRENTUSER);
        return orderService.pay(userInfo.getId(),orderNo);
    }
    /**
     * 支付宝回调参数
     */
    @RequestMapping(value = "/alipay_callback.do")
        public  ServerResponse alipay_callback(HttpServletRequest request){
        //拿到支付宝的回调参数
        Map<String, String[]> parameterMap = request.getParameterMap();
        //遍历map集合，value用“，”隔开
        Iterator<String> iterator = parameterMap.keySet().iterator();
        Map<String,String> paraValues= Maps.newHashMap();
        while (iterator.hasNext()){
            String key = iterator.next();
            String[] strings = parameterMap.get(key);
            String value = "";
            for (int i = 0;i < strings.length;i++){
                value = (i == strings.length - 1)?value+strings[i]:value + strings[i]+",";
                System.out.println("value:"+value);
            }
            paraValues.put(key,value);
        }
        //进行支付宝的验签
        try {
            paraValues.remove("sign_type");//使验签可以通过
            System.out.println("验证通过");
            boolean result = AlipaySignature.rsaCheckV2(paraValues, Configs.getAlipayPublicKey(),"UTF-8",Configs.getSignType());
            if(!result){
                return ServerResponse.responseIsError("非法请求，验证不通过");
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        System.out.println("===================");
        return orderService.alipay_callback(paraValues);
    }
    /**
     * 查询支付信息
     */
    @RequestMapping(value = "/query_order_pay_status.do")
    public ServerResponse query_order_pay_status(HttpSession session,Long orderNo){
        //验证是否有用户登录
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCord.CURRENTUSER);
        return orderService.query_order_pay_status(orderNo);
    }
}
