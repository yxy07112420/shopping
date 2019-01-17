package com.neuedu.service;

import com.neuedu.common.ServerResponse;

import javax.servlet.http.HttpSession;
import java.util.Map;

public interface OrderService {
    /**
     * 创建订单接口
     * @param userId
     * @param shippingId
     * @return
     */
    ServerResponse create(Integer userId,Integer shippingId);

    /**
     * 取消订单
     * @param userId
     * @param orderNo
     * @return
     */
    ServerResponse cancel(Integer userId,Long orderNo);

    /**
     * 获取订单的商品信息
     * @param userId
     * @return
     */
    ServerResponse get_order_cart_product(Integer userId);

    /**
     * 订单列表
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse list(Integer userId,Integer pageNum,Integer pageSize);

    /**
     * 订单详情
     * @param userId
     * @param orderNo
     * @return
     */
    ServerResponse detail(Integer userId,Long orderNo);

    /**
     * 支付接口
     * @param userId
     * @param orderNo
     * @return
     */
    ServerResponse pay(Integer userId,Long orderNo);

    /**
     * 支付宝回调
     */
    ServerResponse alipay_callback(Map<String,String> stringMap);

    /**
     * 查看支付信息
     * @param orderNo
     * @return
     */
    ServerResponse query_order_pay_status(Long orderNo);
}
