package com.seecoder.BlueWhale.po;

import com.seecoder.BlueWhale.enums.CouponTypeEnum;
import com.seecoder.BlueWhale.vo.CouponVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "coupon", indexes = {
        @Index(name = "idx_group_id", columnList = "group_id"),
        @Index(name = "idx_user_id", columnList = "user_id")
})
public class Coupon {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "group_id")
    private Integer groupId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "coupon_type")
    @Enumerated(EnumType.STRING)
    private CouponTypeEnum couponType;

    @Column(name = "expiration_time")
    private Date expirationTime;

    @Column(name = "threshold")
    private BigDecimal threshold; // 满减券的满额条件, 格式为xx.xx, 如果为蓝鲸券则设置为null
    @Column(name = "reduction")
    private BigDecimal reduction;  // 满减券的减额, 格式为0.xx, 如果为蓝鲸券则设置为null
    @Column(name = "is_used")
    private Boolean isUsed;


    public CouponVO toVO() {
        CouponVO couponVO = new CouponVO();
        couponVO.setId(this.id);
        couponVO.setGroupId(this.groupId);
        couponVO.setUserId(this.userId);
        couponVO.setCouponType(this.couponType);
        couponVO.setExpirationTime(this.expirationTime);
        couponVO.setThreshold(this.threshold);
        couponVO.setReduction(this.reduction);
        couponVO.setIsUsed(this.isUsed);
        return couponVO;
    }
}
