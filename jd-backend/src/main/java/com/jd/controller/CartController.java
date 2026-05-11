package com.jd.controller;

import com.jd.common.Result;
import com.jd.common.UserContext;
import com.jd.dto.CartItemRequest;
import com.jd.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public Result<?> getCart() {
        return Result.success(cartService.getCart(UserContext.getUserId()));
    }

    @PostMapping("/items")
    public Result<?> addItem(@RequestBody CartItemRequest req) {
        cartService.addItem(UserContext.getUserId(), req.getProductId(), req.getQuantity() != null ? req.getQuantity() : 1);
        return Result.success("添加成功", null);
    }

    @PutMapping("/items/{id}")
    public Result<?> updateItem(@PathVariable Long id, @RequestBody CartItemRequest req) {
        cartService.updateItem(UserContext.getUserId(), id, req.getQuantity(), req.getSelected());
        return Result.success("更新成功", null);
    }

    @DeleteMapping("/items/{id}")
    public Result<?> removeItem(@PathVariable Long id) {
        cartService.removeItem(UserContext.getUserId(), id);
        return Result.success("删除成功", null);
    }

    @PutMapping("/select-all")
    public Result<?> selectAll(@RequestBody Map<String, Boolean> body) {
        cartService.selectAll(UserContext.getUserId(), Boolean.TRUE.equals(body.get("selected")));
        return Result.success("操作成功", null);
    }

    @DeleteMapping
    public Result<?> clearCart() {
        cartService.clearCart(UserContext.getUserId());
        return Result.success("已清空购物车", null);
    }
}
