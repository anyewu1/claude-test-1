package com.jd.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jd.common.Result;
import com.jd.common.UserContext;
import com.jd.dto.ProductRequest;
import com.jd.entity.*;
import com.jd.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ProductService productService;
    private final OrderService orderService;
    private final UserService userService;
    private final CouponService couponService;
    private final EmbeddingService embeddingService;

    private void requireAdmin() {
        if (!UserContext.isAdmin()) throw new RuntimeException("需要管理员权限");
    }

    // ---- Products ----

    @GetMapping("/products")
    public Result<?> listProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String keyword
    ) {
        requireAdmin();
        return Result.success(productService.search(page, size, null, keyword, null, null, "createdAt", "desc"));
    }

    @PostMapping("/products")
    public Result<?> createProduct(@RequestBody ProductRequest req) {
        requireAdmin();
        Product product = buildProduct(req, new Product());
        productService.save(product);
        embeddingService.generateAndStore(product);
        return Result.success("创建成功", product);
    }

    @PutMapping("/products/{id}")
    public Result<?> updateProduct(@PathVariable Long id, @RequestBody ProductRequest req) {
        requireAdmin();
        Product product = productService.getById(id);
        if (product == null) return Result.error(404, "商品不存在");
        buildProduct(req, product);
        productService.updateById(product);
        embeddingService.generateAndStore(product);
        return Result.success("更新成功", product);
    }

    @DeleteMapping("/products/{id}")
    public Result<?> deleteProduct(@PathVariable Long id) {
        requireAdmin();
        productService.removeById(id);
        return Result.success("删除成功", null);
    }

    // ---- Orders ----

    @GetMapping("/orders")
    public Result<?> listAllOrders(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Integer status
    ) {
        requireAdmin();
        return Result.success(orderService.getAllOrders(status, page, size));
    }

    @PutMapping("/orders/{id}/ship")
    public Result<?> shipOrder(@PathVariable Long id) {
        requireAdmin();
        orderService.shipOrder(id);
        return Result.success("已标记发货", null);
    }

    // ---- Users ----

    @GetMapping("/users")
    public Result<?> listUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        requireAdmin();
        return Result.success(userService.page(new Page<>(page, size)));
    }

    // ---- Coupons ----

    @GetMapping("/coupons")
    public Result<?> listCoupons() {
        requireAdmin();
        return Result.success(couponService.list());
    }

    @PostMapping("/coupons")
    public Result<?> createCoupon(@RequestBody Coupon coupon) {
        requireAdmin();
        coupon.setUsedCount(0);
        couponService.save(coupon);
        return Result.success("创建成功", coupon);
    }

    @DeleteMapping("/coupons/{id}")
    public Result<?> deleteCoupon(@PathVariable Long id) {
        requireAdmin();
        couponService.removeById(id);
        return Result.success("删除成功", null);
    }

    // ---- Embeddings ----

    @PostMapping("/embeddings/rebuild")
    public Result<?> rebuildEmbeddings() {
        requireAdmin();
        if (!embeddingService.isAvailable()) return Result.error(400, "向量模型未配置，请设置 AI_BASE_URL 和 OPENAI_API_KEY");
        embeddingService.generateAndStoreAll();
        return Result.success("向量索引重建完成", null);
    }

    private Product buildProduct(ProductRequest req, Product product) {
        if (req.getName() != null) product.setName(req.getName());
        if (req.getDescription() != null) product.setDescription(req.getDescription());
        if (req.getPrice() != null) product.setPrice(req.getPrice());
        if (req.getOriginalPrice() != null) product.setOriginalPrice(req.getOriginalPrice());
        if (req.getStock() != null) product.setStock(req.getStock());
        if (req.getCategoryId() != null) product.setCategoryId(req.getCategoryId());
        if (req.getCoverImage() != null) product.setCoverImage(req.getCoverImage());
        if (req.getImages() != null) product.setImages(req.getImages());
        if (req.getIsFlashSale() != null) product.setIsFlashSale(req.getIsFlashSale());
        if (req.getStatus() != null) product.setStatus(req.getStatus());
        else if (product.getStatus() == null) product.setStatus(1);
        if (product.getRating() == null) product.setRating(java.math.BigDecimal.valueOf(5.0));
        if (product.getRatingCount() == null) product.setRatingCount(0);
        if (product.getSales() == null) product.setSales(0);
        return product;
    }
}
