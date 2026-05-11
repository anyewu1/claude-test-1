package com.jd.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("favorites")
public class Favorite {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long productId;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(exist = false)
    private String productName;
    @TableField(exist = false)
    private String coverImage;
    @TableField(exist = false)
    private BigDecimal price;
    @TableField(exist = false)
    private BigDecimal originalPrice;
    @TableField(exist = false)
    private BigDecimal rating;
    @TableField(exist = false)
    private Integer sales;
    @TableField(exist = false)
    private Boolean isFlashSale;
}
