package com.seecoder.BlueWhale.repository;

import com.seecoder.BlueWhale.po.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {

    List<Coupon> findByGroupId(Integer groupId);

    List<Coupon> findByUserId(Integer userId);

    List<Coupon> findByUserIdAndGroupId(Integer userId, Integer groupId);
}
