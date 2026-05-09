package com.jd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jd.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    @Select("""
        SELECT p.*, c.name as category_name
        FROM products p
        LEFT JOIN categories c ON p.category_id = c.id
        WHERE p.deleted = 0
          AND p.status = 1
          AND (#{categoryId} IS NULL OR p.category_id = #{categoryId})
          AND (#{keyword} IS NULL OR p.name LIKE CONCAT('%', #{keyword}, '%'))
          AND (#{minPrice} IS NULL OR p.price >= #{minPrice})
          AND (#{maxPrice} IS NULL OR p.price <= #{maxPrice})
        ORDER BY
          CASE WHEN #{sortBy} = 'price' AND #{sortOrder} = 'asc' THEN p.price END ASC,
          CASE WHEN #{sortBy} = 'price' AND #{sortOrder} = 'desc' THEN p.price END DESC,
          CASE WHEN #{sortBy} = 'sales' THEN p.sales END DESC,
          CASE WHEN #{sortBy} = 'rating' THEN p.rating END DESC,
          p.created_at DESC
        """)
    IPage<Product> searchProducts(
            Page<Product> page,
            @Param("categoryId") Long categoryId,
            @Param("keyword") String keyword,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("sortBy") String sortBy,
            @Param("sortOrder") String sortOrder
    );

    @Select("SELECT p.*, c.name as category_name FROM products p LEFT JOIN categories c ON p.category_id = c.id WHERE p.id = #{id} AND p.deleted = 0")
    Product findByIdWithCategory(@Param("id") Long id);
}
