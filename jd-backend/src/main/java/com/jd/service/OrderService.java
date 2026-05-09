package com.jd.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jd.dto.CreateOrderRequest;
import com.jd.entity.Order;

public interface OrderService extends IService<Order> {
    Order createOrder(Long userId, CreateOrderRequest req);
    Page<Order> getUserOrders(Long userId, Integer status, int current, int size);
    Order getOrderDetail(Long userId, Long orderId);
    void cancelOrder(Long userId, Long orderId);
    void payOrder(Long userId, Long orderId);
    void confirmOrder(Long userId, Long orderId);
}
