package com.seecoder.BlueWhale.repository;

import com.seecoder.BlueWhale.enums.PaymentStateEnum;
import com.seecoder.BlueWhale.po.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Integer> {
    List<Order> findByStoreId(Integer storeId);

    List<Order> findByUserId(Integer userId);

    List<Order> findByCommodityId(Integer commodityId);

    List<Order> findByPaymentStateAndCreateTimeBefore(PaymentStateEnum status, Date createTime);

}
