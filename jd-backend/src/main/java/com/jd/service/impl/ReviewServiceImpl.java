package com.jd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jd.entity.Review;
import com.jd.mapper.ReviewMapper;
import com.jd.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review> implements ReviewService {

    @Override
    public Map<String, Object> getProductReviews(Long productId, int page, int size) {
        int offset = (page - 1) * size;
        List<Review> reviews = baseMapper.findByProductIdWithUser(productId, offset, size);
        long total = baseMapper.countByProductId(productId);

        Map<String, Object> result = new HashMap<>();
        result.put("records", reviews);
        result.put("total", total);
        result.put("current", page);
        result.put("size", size);
        return result;
    }

    @Override
    public Review addReview(Long userId, Long productId, Integer rating, String content) {
        if (baseMapper.countByProductAndUser(productId, userId) > 0) {
            throw new RuntimeException("您已评价过该商品");
        }
        if (rating < 1 || rating > 5) {
            throw new RuntimeException("评分必须在1到5之间");
        }
        Review review = new Review();
        review.setUserId(userId);
        review.setProductId(productId);
        review.setRating(rating);
        review.setContent(content);
        save(review);
        return review;
    }
}
