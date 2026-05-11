package com.jd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jd.entity.CartItem;
import com.jd.entity.Product;
import com.jd.mapper.CartItemMapper;
import com.jd.service.CartService;
import com.jd.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CartServiceImpl extends ServiceImpl<CartItemMapper, CartItem> implements CartService {

    private final ProductService productService;

    @Override
    public Map<String, Object> getCart(Long userId) {
        List<CartItem> items = baseMapper.findCartWithProducts(userId);
        int totalCount = items.stream().mapToInt(CartItem::getQuantity).sum();
        int selectedCount = items.stream().filter(CartItem::getSelected).mapToInt(CartItem::getQuantity).sum();
        BigDecimal totalAmount = items.stream()
                .filter(CartItem::getSelected)
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> result = new HashMap<>();
        result.put("items", items);
        result.put("totalCount", totalCount);
        result.put("selectedCount", selectedCount);
        result.put("totalAmount", totalAmount);
        return result;
    }

    @Override
    public void addItem(Long userId, Long productId, int quantity) {
        Product product = productService.getById(productId);
        if (product == null || product.getStatus() != 1) {
            throw new RuntimeException("商品不存在或已下架");
        }
        CartItem existing = getOne(new LambdaQueryWrapper<CartItem>()
                .eq(CartItem::getUserId, userId)
                .eq(CartItem::getProductId, productId));
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
            updateById(existing);
        } else {
            CartItem item = new CartItem();
            item.setUserId(userId);
            item.setProductId(productId);
            item.setQuantity(quantity);
            item.setSelected(true);
            save(item);
        }
    }

    @Override
    public void updateItem(Long userId, Long itemId, Integer quantity, Boolean selected) {
        CartItem item = getById(itemId);
        if (item == null || !item.getUserId().equals(userId)) {
            throw new RuntimeException("购物车商品不存在");
        }
        if (quantity != null) item.setQuantity(quantity);
        if (selected != null) item.setSelected(selected);
        updateById(item);
    }

    @Override
    public void removeItem(Long userId, Long itemId) {
        remove(new LambdaQueryWrapper<CartItem>()
                .eq(CartItem::getId, itemId)
                .eq(CartItem::getUserId, userId));
    }

    @Override
    public void selectAll(Long userId, boolean selected) {
        update(new LambdaUpdateWrapper<CartItem>()
                .eq(CartItem::getUserId, userId)
                .set(CartItem::getSelected, selected));
    }

    @Override
    public void clearCart(Long userId) {
        remove(new LambdaQueryWrapper<CartItem>().eq(CartItem::getUserId, userId));
    }

    @Override
    public void clearSelectedItems(Long userId) {
        remove(new LambdaQueryWrapper<CartItem>()
                .eq(CartItem::getUserId, userId)
                .eq(CartItem::getSelected, true));
    }
}
