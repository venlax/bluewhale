package com.seecoder.BlueWhale.serviceImpl;

import com.seecoder.BlueWhale.enums.*;
import com.seecoder.BlueWhale.po.*;
import com.seecoder.BlueWhale.repository.*;
import com.seecoder.BlueWhale.vo.*;
import org.junit.jupiter.api.*;
import com.seecoder.BlueWhale.util.SecurityUtil;
import com.seecoder.BlueWhale.exception.BlueWhaleException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CouponServiceImplTest {
    //模拟测试对象
    public SecurityUtil securityUtil = mock(SecurityUtil.class);
    public CouponRepository couponRepository = mock(CouponRepository.class);
    public CouponGroupRepository couponGroupRepository = mock(CouponGroupRepository.class);
    public User user = new User();
    public CouponVO coupon = new CouponVO();
    public Coupon coupon2 = new Coupon();
    //测试实例
    CouponServiceImpl test  =new CouponServiceImpl(securityUtil,couponRepository,couponGroupRepository);

    // 发放优惠券的测试 5’：
    // 非CEO/Manager发放全局优惠券；非该商店工作人员发放局部优惠券；非工作人员发放局部优惠券；
    // 优惠券数量为负数；正常发放局部优惠券
    @Test
    void distributeCouponGroupFullFail() {
        user.setRole(RoleEnum.STAFF);
        when(securityUtil.getCurrentUser()).thenReturn(user);
        assertThrows(BlueWhaleException.class,
                () -> test.distributeCouponGroup(null,10, coupon));
    }
    @Test
    void distributeCouponGroupSpecialFail1() {
        //不是该商店员工发放局部优惠券
        user.setRole(RoleEnum.STAFF);
        user.setStoreId(1);
        when(securityUtil.getCurrentUser()).thenReturn(user);
        assertThrows(BlueWhaleException.class,
                () -> test.distributeCouponGroup(2,10, coupon));
    }
    @Test
    void distributeCouponGroupSpecialFail2() {
        user.setRole(RoleEnum.CUSTOMER);
        when(securityUtil.getCurrentUser()).thenReturn(user);
        assertThrows(BlueWhaleException.class,
                () -> test.distributeCouponGroup(1,10, coupon));
    }
    @Test
    void distributeCouponGroupNumIllegal() {
        //设置优惠券数量为负时应该抛出异常
        user.setRole(RoleEnum.CEO);
        when(securityUtil.getCurrentUser()).thenReturn(user);
        assertThrows(BlueWhaleException.class,
                () -> test.distributeCouponGroup(null,-1, coupon));
    }

    @Test
    void distributeCouponGroupNormal() {
        user.setRole(RoleEnum.STAFF);
        user.setStoreId(1);
        when(securityUtil.getCurrentUser()).thenReturn(user);

        CouponGroup couponGroup =  new CouponGroup();
        couponGroup.setId(1);
        when(couponGroupRepository.save(any())).thenAnswer(invocation -> {
            CouponGroup savedCouponGroup = invocation.getArgument(0);
            savedCouponGroup.setId(1); // 假设设置一个有效的 id
            return savedCouponGroup;
        });
        Coupon coupon2 = new Coupon();
        when(couponRepository.save(coupon.toPO())).thenReturn(coupon2);

        assertTrue(test.distributeCouponGroup(1,10, coupon));
    }

    // 用户领取优惠券的测试 2'：
    // 优惠券id不存在；优惠券剩余数量为0
    @Test
    void redeemCouponNotExist() {
        when(couponGroupRepository.findById(any())).thenReturn(Optional.empty());
        when(couponRepository.findByGroupId(any())).thenReturn(null);
        assertThrows(BlueWhaleException.couponNotExist().getClass(),() -> test.redeemCoupon(1));
    }

    @Test
    void redeemCouponAlreadyZero() {
        //优惠券剩余数量为0
        coupon2.setId(1);
        coupon2.setUserId(null);

        when(securityUtil.getCurrentUser()).thenReturn(user);
        when(couponRepository.findById(1)).thenReturn(Optional.of(coupon2));

        CouponGroup couponGroup = new CouponGroup();
        couponGroup.setRestAmount(0);
        couponGroup.setId(1);
        when(couponGroupRepository.findById(any())).thenReturn(Optional.of(couponGroup));

        assertThrows(BlueWhaleException.couponGroupEmpty().getClass(),() -> test.redeemCoupon(1));
    }

    @Test
//    @RepeatedTest() // 执行10次重复测试，模拟多个并发请求
    public void testRedeemCouponConcurrency() throws InterruptedException {

        CouponGroup couponGroup = new CouponGroup();
        couponGroup.setRestAmount(10);
        couponGroup.setId(1);
        when(couponGroupRepository.findById(any())).thenReturn(Optional.of(couponGroup));

        Runnable task = () -> {
            try {
                test.redeemCoupon(1);
            } catch (Exception e) {
                // 处理异常或记录日志
                e.printStackTrace();
            }
        };

        Thread thread1 = new Thread(task);
        Thread thread2 = new Thread(task);
        Thread thread3 = new Thread(task);

        thread1.start();
        thread2.start();
        thread3.start();


        thread1.join();
        thread2.join();
        thread3.join();
    }
}