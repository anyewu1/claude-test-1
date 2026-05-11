package com.jd.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("product_embeddings")
public class ProductEmbedding {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long productId;
    private String embedding;
    private LocalDateTime updatedAt;
}
