package com.seecoder.BlueWhale.serviceImpl.strategy;

import com.seecoder.BlueWhale.po.Coupon;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
public class Context {
    private CalculateStrategy calculateStrategy;

    public BigDecimal executeStrategy(BigDecimal price, Coupon coupon) {
        return calculateStrategy.calculate(price, coupon);
    }
}
