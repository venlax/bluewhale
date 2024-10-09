package com.seecoder.BlueWhale.vo;

import com.seecoder.BlueWhale.po.CouponGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CouponGroupVO {


    private Integer id;

    private Integer storeId;

    private Integer amount;

    private Integer restAmount;

    public CouponGroup toPO(){
        CouponGroup couponGroup = new CouponGroup();
        couponGroup.setId(this.id);
        couponGroup.setStoreId(this.storeId);
        couponGroup.setAmount(this.amount);
        couponGroup.setRestAmount(this.restAmount);
        return couponGroup;
    }
}
