package com.seecoder.BlueWhale.po;

import com.seecoder.BlueWhale.vo.CouponGroupVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class CouponGroup {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;


    @Column(name = "store_id")
    private Integer storeId; // 如果为全局优惠券,该字段为null

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "rest_amount")
    private Integer restAmount;


    public CouponGroupVO toVO(){
        CouponGroupVO couponGroupVO = new CouponGroupVO();
        couponGroupVO.setId(this.id);
        couponGroupVO.setStoreId(this.storeId);
        couponGroupVO.setAmount(this.amount);
        couponGroupVO.setRestAmount(this.restAmount);
        return couponGroupVO;
    }
}
