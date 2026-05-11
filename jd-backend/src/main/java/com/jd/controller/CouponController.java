package com.jd.controller;

import com.jd.common.Result;
import com.jd.entity.Coupon;
import com.jd.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @GetMapping("/validate")
    public Result<?> validate(
            @RequestParam String code,
            @RequestParam(defaultValue = "0") BigDecimal orderAmount
    ) {
        Coupon coupon = couponService.validate(code, orderAmount);
        BigDecimal discounted = couponService.applyDiscount(coupon, orderAmount);
        return Result.success(Map.of(
                "coupon", coupon,
                "discount", orderAmount.subtract(discounted),
                "finalAmount", discounted
        ));
    }
}
