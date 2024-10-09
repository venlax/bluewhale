package com.seecoder.BlueWhale.repository;

import com.seecoder.BlueWhale.po.DeliveryInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface DeliveryInfoRepository extends JpaRepository<DeliveryInfo, Integer> {
    List<DeliveryInfo> findByUserId(Integer userId);

    Boolean existsByUserIdAndAddressAndPhone(Integer userId, String address, String phone);
    DeliveryInfo findByUserIdAndAddressAndPhone(Integer userId, String address, String phone);
}
