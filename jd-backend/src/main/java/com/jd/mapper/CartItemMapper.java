package com.jd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jd.entity.CartItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CartItemMapper extends BaseMapper<CartItem> {

    @Select("""
        SELECT ci.*, p.name as product_name, p.cover_image as product_image,
               p.price, p.stock
        FROM cart_items ci
        JOIN products p ON ci.product_id = p.id
        WHERE ci.user_id = #{userId}
        ORDER BY ci.created_at DESC
        """)
    List<CartItem> findCartWithProducts(@Param("userId") Long userId);
}
