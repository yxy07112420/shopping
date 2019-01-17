package com.neuedu.service;

import com.neuedu.common.ServerResponse;

import javax.servlet.http.HttpSession;

/**
 * 购物车的service层
 */
public interface CartService {

    /**
     * 添加购物车
     * @param userId  用户id
     * @param productId 商品id
     * @param count 购物车数量
     * @return
     */
    ServerResponse add(Integer userId, Integer productId, Integer count);

    /**
     * 查询购物车列表
     */
    ServerResponse list(Integer userId);

    /**
     * 更新购物车中商品数量
     * @param userId
     * @param productId
     * @param count
     * @return
     */
    ServerResponse update(Integer userId,Integer productId,Integer count);

    /**
     * 删除某一购物信息
     * @param userId
     * @param productIds
     * @return
     */
    ServerResponse update_cart(Integer userId,String productIds);

    /**
     * 取消或选中某一件商品
     * @param userId
     * @param productId
     * @return
     */
    ServerResponse select(Integer userId,Integer productId,Integer check);

    /**
     * 统计购物车的商品数量
     * @param userId
     * @return
     */
    ServerResponse select_cart_product_count(Integer userId);
}
