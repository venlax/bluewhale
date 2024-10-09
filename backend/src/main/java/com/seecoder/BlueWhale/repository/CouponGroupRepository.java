package com.seecoder.BlueWhale.repository;

import com.seecoder.BlueWhale.po.CouponGroup;
import com.seecoder.BlueWhale.vo.CouponGroupVO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponGroupRepository extends JpaRepository<CouponGroup, Integer> {
    List<CouponGroup> findByStoreId(Integer storeId);
}
