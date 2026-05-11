package com.jd.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer stock;
    private Long categoryId;
    private String coverImage;
    private String images;
    private Boolean isFlashSale;
    private Integer status;
}
