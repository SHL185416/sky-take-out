package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 自定义订单定时任务类
 */
@Component
@Slf4j
public class OrderTask {

    @Autowired
    OrderMapper orderMapper;

    /**
     * 每分钟执行一次
     */
    @Scheduled(cron = "0 * * * * ?")
    public void processTimeoutOrder() {
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(-15);
        List<Orders> ordersList = orderMapper.getOrderByStatusAndTime(Orders.PENDING_PAYMENT, localDateTime);
        if (ordersList != null && ordersList.size() > 0) {
            for (Orders order : ordersList) {
                log.info("定时处理超时订单：{}", LocalDateTime.now());
                order.setStatus(Orders.CANCELLED);
                order.setCancelReason("超时未支付，订单自动取消");
                order.setCancelTime(LocalDateTime.now());
                orderMapper.update(order);
                // 执行订单超时处理逻辑
            }
        }
    }

    /**
     * 每天凌晨1点执行一次
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryOrder() {
        LocalDateTime localDateTime = LocalDateTime.now().plusHours(-1);
        List<Orders> ordersList = orderMapper.getOrderByStatusAndTime(Orders.DELIVERY_IN_PROGRESS, localDateTime);
        if (ordersList != null && ordersList.size() > 0) {
            for (Orders order : ordersList) {
                log.info("定时处理派送中订单：{}", LocalDateTime.now());
                order.setStatus(Orders.COMPLETED);
                orderMapper.update(order);
                // 执行订单派送中处理逻辑
            }
        }
    }
}
