package com.jd.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreateOrderRequest {
    private Long addressId;
    private List<Long> cartItemIds;
    private String remark;
    private String couponCode;
}
