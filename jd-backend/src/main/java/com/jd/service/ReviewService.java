package com.jd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jd.entity.Review;

import java.util.Map;

public interface ReviewService extends IService<Review> {
    Map<String, Object> getProductReviews(Long productId, int page, int size);
    Review addReview(Long userId, Long productId, Integer rating, String content);
}
