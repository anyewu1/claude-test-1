package com.jd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jd.entity.ProductEmbedding;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductEmbeddingMapper extends BaseMapper<ProductEmbedding> {

    @Select("SELECT * FROM product_embeddings WHERE product_id = #{productId}")
    ProductEmbedding findByProductId(@Param("productId") Long productId);
}
