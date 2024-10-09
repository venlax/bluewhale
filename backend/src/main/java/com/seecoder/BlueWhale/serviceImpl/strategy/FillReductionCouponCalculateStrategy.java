package com.seecoder.BlueWhale.serviceImpl.strategy;

import com.seecoder.BlueWhale.po.Coupon;

import java.math.BigDecimal;

public class FillReductionCouponCalculateStrategy implements CalculateStrategy{

    @Override
    public BigDecimal calculate(BigDecimal price, Coupon coupon) {
        if (price.compareTo(coupon.getThreshold()) >= 0){
            return price.subtract(coupon.getReduction());
        }
        return price;
    }
}
