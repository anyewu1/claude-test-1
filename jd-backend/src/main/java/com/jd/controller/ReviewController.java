package com.jd.controller;

import com.jd.common.Result;
import com.jd.common.UserContext;
import com.jd.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/product/{productId}")
    public Result<?> listByProduct(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return Result.success(reviewService.getProductReviews(productId, page, size));
    }

    @PostMapping("/product/{productId}")
    public Result<?> addReview(
            @PathVariable Long productId,
            @RequestBody Map<String, Object> body
    ) {
        Long userId = UserContext.getUserId();
        if (userId == null) return Result.error(401, "未登录，请先登录");
        Integer rating = (Integer) body.get("rating");
        String content = (String) body.get("content");
        if (rating == null) return Result.error(400, "评分不能为空");
        return Result.success("评价成功", reviewService.addReview(userId, productId, rating, content));
    }
}
