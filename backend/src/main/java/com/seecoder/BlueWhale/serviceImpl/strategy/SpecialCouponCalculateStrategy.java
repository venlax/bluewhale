package com.seecoder.BlueWhale.serviceImpl.strategy;

import com.seecoder.BlueWhale.po.Coupon;

import java.math.BigDecimal;

/**
 * @Author: DingXiaoyu
 * @Date: 22:35 2023/12/19
 * “蓝鲸券”使用规则：
 * 0-100元区间打九五折；
 * 100-200元区间打九折；
 * 200-300元区间打八五折；
 * 300-400元区间打八折；
 * 400-500元区间打七五折；
 * 500元以上区间打七折。
*/


public class SpecialCouponCalculateStrategy implements CalculateStrategy{
    private static final BigDecimal[] DISCOUNT = new BigDecimal[]{new BigDecimal("0.95"), new BigDecimal("0.9"), new BigDecimal("0.85"), new BigDecimal("0.8"), new BigDecimal("0.75"), new BigDecimal("0.7")};

    private int getIndex(BigDecimal num) {
        return num.divide(new BigDecimal("100")).intValue() > 5 ? 5 : num.divide(new BigDecimal("100")).intValue();
    }
    @Override
    public BigDecimal calculate(BigDecimal price, Coupon coupon) {
        if (coupon.getThreshold() == null){
            return price.multiply(DISCOUNT[getIndex(price)]);
        }
        return price;
    }
}
