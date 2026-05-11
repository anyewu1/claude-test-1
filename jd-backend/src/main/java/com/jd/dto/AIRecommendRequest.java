package com.jd.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class AIRecommendRequest {
    private String query;
    private BigDecimal budget;
    private Integer limit = 6;
}
