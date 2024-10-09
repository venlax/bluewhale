package com.seecoder.BlueWhale.controller;

import com.seecoder.BlueWhale.po.CouponGroup;
import com.seecoder.BlueWhale.service.CouponService;
import com.seecoder.BlueWhale.vo.CouponGroupVO;
import com.seecoder.BlueWhale.vo.CouponVO;
import com.seecoder.BlueWhale.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {

    @Autowired
    CouponService couponService;


    @PostMapping("/distributeCouponGroup")
    public ResultVO<Boolean> distributeCouponGroup(@RequestParam(required = false) Integer storeId,@RequestParam Integer amount,@RequestBody CouponVO couponVO) {
        return ResultVO.buildSuccess(couponService.distributeCouponGroup(storeId, amount, couponVO));
    }

    @GetMapping("/getCouponGroup")
    public ResultVO<List<CouponGroupVO>> getCouponGroups() {
        return ResultVO.buildSuccess(couponService.getCouponGroups());
    }

    @GetMapping("/getCouponByGroupId/{groupId}")
    public ResultVO<List<CouponVO>> getCouponGroupsByGroupId(@PathVariable(value = "groupId") Integer groupId) {
        return ResultVO.buildSuccess(couponService.getCouponsByGroupId(groupId));
    }

    @GetMapping("/getCouponByUserId/{userId}")
    public ResultVO<List<CouponVO>> getCouponsByUserId(@PathVariable(value = "userId") Integer userId) {
        return ResultVO.buildSuccess(couponService.getCouponsByUserId(userId));
    }

    // 领取优惠券的接口
    @GetMapping("/redeemCoupon/{groupId}")
    public ResultVO<Boolean> redeemCoupon(@PathVariable(value = "groupId") Integer groupId) {

        return ResultVO.buildSuccess(couponService.redeemCoupon(groupId));


    }

//    @GetMapping("/getCouponByStoreId")
//    public ResultVO<List<CouponVO>> getCouponStoreByStoreId(Integer storeId) {
//        return ResultVO.buildSuccess(couponService.getCouponByStoreId(storeId));
//    }
}
