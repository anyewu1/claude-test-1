package com.jd.controller;

import com.jd.common.Result;
import com.jd.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public Result<?> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder
    ) {
        return Result.success(productService.search(page, size, categoryId, keyword, minPrice, maxPrice, sortBy, sortOrder));
    }

    @GetMapping("/search")
    public Result<?> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder
    ) {
        return Result.success(productService.search(page, size, null, keyword, null, null, sortBy, sortOrder));
    }

    @GetMapping("/flash-sale")
    public Result<?> flashSale() {
        return Result.success(productService.getFlashSale());
    }

    @GetMapping("/recommend")
    public Result<?> recommend(
            @RequestParam(required = false) Long productId,
            @RequestParam(defaultValue = "8") int limit
    ) {
        return Result.success(productService.getRecommend(productId, limit));
    }

    @GetMapping("/{id}")
    public Result<?> detail(@PathVariable Long id) {
        var product = productService.getDetail(id);
        if (product == null) return Result.error(404, "商品不存在");
        return Result.success(product);
    }
}
