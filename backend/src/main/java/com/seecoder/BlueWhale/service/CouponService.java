package com.seecoder.BlueWhale.service;

import com.seecoder.BlueWhale.vo.CouponGroupVO;
import com.seecoder.BlueWhale.vo.CouponVO;

import java.util.List;

public interface CouponService {
    Boolean distributeCouponGroup(Integer storeId, Integer amount, CouponVO couponVO);

    List<CouponGroupVO> getCouponGroups();

    List<CouponVO> getCouponsByGroupId(Integer groupId);


    List<CouponVO> getCouponsByUserId(Integer userId);

    Boolean redeemCoupon(Integer groupId);



   // List<CouponVO> getCouponByStoreId(Integer storeId);
}
