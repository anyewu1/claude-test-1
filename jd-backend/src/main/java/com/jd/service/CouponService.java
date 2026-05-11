package com.jd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jd.entity.Coupon;

import java.math.BigDecimal;

public interface CouponService extends IService<Coupon> {
    Coupon validate(String code, BigDecimal orderAmount);
    BigDecimal applyDiscount(Coupon coupon, BigDecimal orderAmount);
    void markUsed(Long couponId);
}
