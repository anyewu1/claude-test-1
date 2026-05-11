package com.jd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jd.entity.Review;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ReviewMapper extends BaseMapper<Review> {

    @Select("SELECT r.*, u.username, u.avatar FROM reviews r JOIN users u ON r.user_id = u.id WHERE r.product_id = #{productId} ORDER BY r.created_at DESC LIMIT #{limit} OFFSET #{offset}")
    List<Review> findByProductIdWithUser(@Param("productId") Long productId, @Param("offset") int offset, @Param("limit") int limit);

    @Select("SELECT COUNT(*) FROM reviews WHERE product_id = #{productId}")
    long countByProductId(@Param("productId") Long productId);

    @Select("SELECT COUNT(*) FROM reviews WHERE product_id = #{productId} AND user_id = #{userId}")
    long countByProductAndUser(@Param("productId") Long productId, @Param("userId") Long userId);
}
