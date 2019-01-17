package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Shipping;

import javax.servlet.http.HttpSession;

public interface AddressService {
    /**
     * 添加地址
     * @param userId
     * @param shipping
     * @return
     */
    ServerResponse add(Integer userId,Shipping shipping);

    /**
     * 删除地址
     * @param userId
     * @param shippingId
     * @return
     */
    ServerResponse del(Integer userId,Integer shippingId);

    /**
     * 登录状态更新地址
     * @param shipping
     * @return
     */
    ServerResponse update(Shipping shipping);

    /**
     * 选中查看具体的地址
     * @param userId
     * @param shippingId
     * @return
     */
    ServerResponse select(Integer userId,Integer shippingId);

    /**
     * 查看地址列表
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse list(Integer userId,Integer pageNum,Integer pageSize);
}
