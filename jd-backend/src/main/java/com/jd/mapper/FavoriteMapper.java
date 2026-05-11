package com.jd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jd.entity.Favorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FavoriteMapper extends BaseMapper<Favorite> {

    @Select("SELECT f.*, p.name as product_name, p.cover_image, p.price, p.original_price, p.rating, p.sales, p.is_flash_sale FROM favorites f JOIN products p ON f.product_id = p.id WHERE f.user_id = #{userId} ORDER BY f.created_at DESC")
    List<Favorite> findFavoritesWithProduct(@Param("userId") Long userId);

    @Select("SELECT COUNT(*) FROM favorites WHERE user_id = #{userId} AND product_id = #{productId}")
    long countByUserAndProduct(@Param("userId") Long userId, @Param("productId") Long productId);
}
