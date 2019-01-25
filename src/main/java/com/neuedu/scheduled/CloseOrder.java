package com.neuedu.scheduled;


import com.neuedu.service.OrderService;
import com.neuedu.utils.DateChangeUtils;
import com.neuedu.utils.PropertisUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
 * 作业调度
 */
@Component
public class CloseOrder {

    @Autowired
    OrderService orderService;
    /**
     * 定时关闭订单
     */
    @Scheduled(cron = "0 */1 * * * *")
    public void closeOrder(){
        System.out.println("====关闭订单执行中===");
       //获取关闭订单的时效
        Integer timeOut = Integer.parseInt(PropertisUtils.readByKey("close.order.time"));
        Date closeTime = DateUtils.addSeconds(new Date(),-timeOut);
        //关闭订单
        orderService.closeOrder(DateChangeUtils.dateToString(closeTime));
    }
}
