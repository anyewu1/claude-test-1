package com.jd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jd.dto.CreateOrderRequest;
import com.jd.entity.*;
import com.jd.mapper.OrderItemMapper;
import com.jd.mapper.OrderMapper;
import com.jd.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    private final CartService cartService;
    private final ProductService productService;
    private final AddressService addressService;
    private final OrderItemMapper orderItemMapper;

    @Override
    @Transactional
    public Order createOrder(Long userId, CreateOrderRequest req) {
        Address address = addressService.getById(req.getAddressId());
        if (address == null || !address.getUserId().equals(userId)) {
            throw new RuntimeException("收货地址不存在");
        }

        List<CartItem> cartItems = cartService.list(new LambdaQueryWrapper<CartItem>()
                .eq(CartItem::getUserId, userId)
                .in(CartItem::getId, req.getCartItemIds()));

        if (cartItems.isEmpty()) {
            throw new RuntimeException("购物车商品不存在");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            Product product = productService.getById(cartItem.getProductId());
            if (product == null || product.getStatus() != 1) {
                throw new RuntimeException("商品 " + cartItem.getProductId() + " 不可用");
            }
            if (product.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException("商品 " + product.getName() + " 库存不足");
            }

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);

            OrderItem oi = new OrderItem();
            oi.setProductId(product.getId());
            oi.setProductName(product.getName());
            oi.setProductImage(product.getCoverImage());
            oi.setPrice(product.getPrice());
            oi.setQuantity(cartItem.getQuantity());
            orderItems.add(oi);

            product.setStock(product.getStock() - cartItem.getQuantity());
            product.setSales(product.getSales() + cartItem.getQuantity());
            productService.updateById(product);
        }

        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setStatus(0);
        order.setTotalAmount(totalAmount);
        order.setActualAmount(totalAmount);
        order.setAddressId(req.getAddressId());
        order.setAddressSnapshot(address.toSnapshot());
        order.setRemark(req.getRemark());
        save(order);

        for (OrderItem oi : orderItems) {
            oi.setOrderId(order.getId());
            orderItemMapper.insert(oi);
        }

        cartService.removeByIds(req.getCartItemIds());
        order.setItems(orderItems);
        return order;
    }

    @Override
    public Page<Order> getUserOrders(Long userId, Integer status, int current, int size) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<Order>()
                .eq(Order::getUserId, userId)
                .orderByDesc(Order::getCreatedAt);
        if (status != null) {
            wrapper.eq(Order::getStatus, status);
        }
        Page<Order> page = this.page(new Page<>(current, size), wrapper);
        for (Order order : page.getRecords()) {
            order.setItems(orderItemMapper.findByOrderId(order.getId()));
        }
        return page;
    }

    @Override
    public Order getOrderDetail(Long userId, Long orderId) {
        Order order = getOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getId, orderId)
                .eq(Order::getUserId, userId));
        if (order == null) throw new RuntimeException("订单不存在");
        order.setItems(orderItemMapper.findByOrderId(orderId));
        return order;
    }

    @Override
    public void cancelOrder(Long userId, Long orderId) {
        Order order = getOrderDetail(userId, orderId);
        if (order.getStatus() != 0) throw new RuntimeException("只有待付款订单可以取消");
        order.setStatus(4);
        updateById(order);
        for (OrderItem item : order.getItems()) {
            Product product = productService.getById(item.getProductId());
            if (product != null) {
                product.setStock(product.getStock() + item.getQuantity());
                product.setSales(Math.max(0, product.getSales() - item.getQuantity()));
                productService.updateById(product);
            }
        }
    }

    @Override
    public void payOrder(Long userId, Long orderId) {
        Order order = getOrderDetail(userId, orderId);
        if (order.getStatus() != 0) throw new RuntimeException("订单状态不正确");
        order.setStatus(1);
        order.setPaidAt(LocalDateTime.now());
        updateById(order);
    }

    @Override
    public void confirmOrder(Long userId, Long orderId) {
        Order order = getOrderDetail(userId, orderId);
        if (order.getStatus() != 1 && order.getStatus() != 2) throw new RuntimeException("订单状态不正确");
        order.setStatus(3);
        order.setCompletedAt(LocalDateTime.now());
        updateById(order);
    }

    private String generateOrderNo() {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "JD" + time + String.format("%04d", new Random().nextInt(10000));
    }
}
