package com.jd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jd.entity.CartItem;

import java.util.Map;

public interface CartService extends IService<CartItem> {
    Map<String, Object> getCart(Long userId);
    void addItem(Long userId, Long productId, int quantity);
    void updateItem(Long userId, Long itemId, Integer quantity, Boolean selected);
    void removeItem(Long userId, Long itemId);
    void selectAll(Long userId, boolean selected);
    void clearCart(Long userId);
    void clearSelectedItems(Long userId);
}
