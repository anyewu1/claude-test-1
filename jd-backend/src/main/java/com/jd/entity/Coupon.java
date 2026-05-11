package com.jd.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("coupons")
public class Coupon {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String code;
    /** FIXED (满减) or PERCENT (折扣, value = discount %) */
    private String type;
    private BigDecimal value;
    private BigDecimal minOrder;
    private Integer maxUses;
    private Integer usedCount;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
