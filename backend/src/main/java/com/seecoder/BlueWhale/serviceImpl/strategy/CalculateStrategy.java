package com.seecoder.BlueWhale.serviceImpl.strategy;

import com.seecoder.BlueWhale.po.Coupon;

import java.math.BigDecimal;

public interface CalculateStrategy {
    BigDecimal calculate(BigDecimal price, Coupon coupon);
}
