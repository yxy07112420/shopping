package com.neuedu.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.ShippingMapper;
import com.neuedu.pojo.Shipping;
import com.neuedu.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    ShippingMapper shippingMapper;
    //添加地址
    @Override
    public ServerResponse add(Integer userId,Shipping shipping) {
        //参数非空校验
        if(shipping == null){
            return ServerResponse.responseIsError("参数错误");
        }
        //添加地址
        shipping.setUserId(userId);
        int result = shippingMapper.insert(shipping);
        if(result == 0){
            return ServerResponse.responseIsError("添加地址失败");
        }
        Map<String,Integer> map = Maps.newHashMap();
        map.put("shippingId",shipping.getId());
        return ServerResponse.responseIsSuccess("添加地址成功",map);
    }
    @Override
    public ServerResponse del(Integer userId, Integer shippingId) {
        //参数非空校验
        if(shippingId == null||userId == null){
            return ServerResponse.responseIsError("参数错误");
        }
        //根据用户id和地址id删除
        int result = shippingMapper.deleteShippingByUserIdAndShippingId(userId, shippingId);
        if(result == 0){
            return ServerResponse.responseIsError("删除地址失败");
        }
        return ServerResponse.responseIsSuccess("删除地址成功");
    }

    @Override
    public ServerResponse update(Shipping shipping) {
        //参数非空校验
        if(shipping == null || shipping.getId() == null){
            return ServerResponse.responseIsError("参数错误");
        }
        //跟新地址
        int result = shippingMapper.updateShippingByShipping(shipping);
        if(result == 0){
            ServerResponse.responseIsError("更新地址失败");
        }
        return ServerResponse.responseIsSuccess("更新地址成功");
    }

    @Override
    public ServerResponse select(Integer userId, Integer shippingId) {
        //参数非空校验
        if(shippingId == null||userId == null){
            return ServerResponse.responseIsError("参数错误");
        }
        //根据用户名和地址id查看具体的地址
        Shipping shipping = shippingMapper.selectShippingByUserIdAndShippingId(userId, shippingId);
        return ServerResponse.responseIsSuccess(null,shipping);
    }

    @Override
    public ServerResponse list(Integer userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        //根据用户id查询地址信息
        List<Shipping> shippings = shippingMapper.selectShippingByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippings);
        return ServerResponse.responseIsSuccess(null,pageInfo);
    }
}
