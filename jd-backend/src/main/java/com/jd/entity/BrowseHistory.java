package com.jd.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("browse_history")
public class BrowseHistory {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long productId;
    private LocalDateTime viewedAt;

    @TableField(exist = false)
    private String productName;
    @TableField(exist = false)
    private String coverImage;
    @TableField(exist = false)
    private java.math.BigDecimal price;
}
