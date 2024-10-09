package com.seecoder.BlueWhale.vo;

import com.seecoder.BlueWhale.enums.CouponTypeEnum;
import com.seecoder.BlueWhale.po.Coupon;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.math.BigDecimal;
import java.util.Date;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CouponVO {
    private Integer id;

    private Integer groupId;


    private Integer userId;


    private CouponTypeEnum couponType;


    private Date expirationTime;


    private BigDecimal threshold; // 满减券的满额条件, 如果为蓝鲸券则设置为null

    private BigDecimal reduction;  // 满减券的减额, 如果为蓝鲸券则设置为null

    private Boolean isUsed;

    private String storeName;

    private Boolean isExpired;

    public Coupon toPO() {
        Coupon coupon = new Coupon();
        coupon.setId(this.id);
        coupon.setGroupId(this.groupId);
        coupon.setUserId(this.userId);
        coupon.setCouponType(this.couponType);
        coupon.setExpirationTime(this.expirationTime);
        coupon.setThreshold(this.threshold);
        coupon.setReduction(this.reduction);
        coupon.setIsUsed(this.isUsed);
        return coupon;
    }
}
