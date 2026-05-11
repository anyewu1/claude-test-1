package com.jd.controller;

import com.jd.common.Result;
import com.jd.common.UserContext;
import com.jd.dto.CreateOrderRequest;
import com.jd.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public Result<?> createOrder(@RequestBody CreateOrderRequest req) {
        return Result.success("下单成功", orderService.createOrder(UserContext.getUserId(), req));
    }

    @GetMapping
    public Result<?> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer status
    ) {
        return Result.success(orderService.getUserOrders(UserContext.getUserId(), status, page, size));
    }

    @GetMapping("/{id}")
    public Result<?> detail(@PathVariable Long id) {
        return Result.success(orderService.getOrderDetail(UserContext.getUserId(), id));
    }

    @PutMapping("/{id}/cancel")
    public Result<?> cancel(@PathVariable Long id) {
        orderService.cancelOrder(UserContext.getUserId(), id);
        return Result.success("取消成功", null);
    }

    @PutMapping("/{id}/pay")
    public Result<?> pay(@PathVariable Long id) {
        orderService.payOrder(UserContext.getUserId(), id);
        return Result.success("支付成功", null);
    }

    @PutMapping("/{id}/confirm")
    public Result<?> confirm(@PathVariable Long id) {
        orderService.confirmOrder(UserContext.getUserId(), id);
        return Result.success("确认收货成功", null);
    }

    @PutMapping("/{id}/refund")
    public Result<?> refund(@PathVariable Long id) {
        orderService.refundOrder(UserContext.getUserId(), id);
        return Result.success("退款申请已提交", null);
    }
}
