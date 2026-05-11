package com.jd.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jd.entity.Coupon;
import com.jd.mapper.CouponMapper;
import com.jd.service.CouponService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements CouponService {

    @Override
    public Coupon validate(String code, BigDecimal orderAmount) {
        Coupon coupon = baseMapper.findByCode(code);
        if (coupon == null) throw new RuntimeException("优惠券不存在");
        if (coupon.getUsedCount() >= coupon.getMaxUses()) throw new RuntimeException("优惠券已使用完毕");
        if (coupon.getValidFrom() != null && LocalDateTime.now().isBefore(coupon.getValidFrom()))
            throw new RuntimeException("优惠券未到使用时间");
        if (coupon.getValidTo() != null && LocalDateTime.now().isAfter(coupon.getValidTo()))
            throw new RuntimeException("优惠券已过期");
        if (orderAmount.compareTo(coupon.getMinOrder()) < 0)
            throw new RuntimeException("订单金额未达到优惠券最低使用门槛 ¥" + coupon.getMinOrder());
        return coupon;
    }

    @Override
    public BigDecimal applyDiscount(Coupon coupon, BigDecimal orderAmount) {
        if ("FIXED".equals(coupon.getType())) {
            BigDecimal discounted = orderAmount.subtract(coupon.getValue());
            return discounted.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : discounted;
        } else if ("PERCENT".equals(coupon.getType())) {
            BigDecimal discount = orderAmount.multiply(coupon.getValue()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            return orderAmount.subtract(discount);
        }
        return orderAmount;
    }

    @Override
    public void markUsed(Long couponId) {
        update(new LambdaUpdateWrapper<Coupon>()
                .eq(Coupon::getId, couponId)
                .setSql("used_count = used_count + 1"));
    }
}
