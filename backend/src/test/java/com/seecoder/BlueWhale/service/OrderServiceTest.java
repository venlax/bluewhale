package com.seecoder.BlueWhale.service;

import com.seecoder.BlueWhale.enums.PaymentStateEnum;
import com.seecoder.BlueWhale.po.Order;
import com.seecoder.BlueWhale.repository.*;
import com.seecoder.BlueWhale.util.SecurityUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class OrderServiceTest {
    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    CommodityRepository commodityRepository;

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    CouponGroupRepository couponGroupRepository;

    @Autowired
    SecurityUtil securityUtil;

    public Order testOrder;
    @Before
    public void setUp() {
        testOrder = new Order();
        testOrder.setUserId(1);
        testOrder.setCommodityId(1);
        testOrder.setCount(10);
        orderRepository.save(testOrder);
    }

    @Test
    void getOrdersByUserId() {
        Assert.assertEquals(orderRepository.findByUserId(1).get(0).getCommodityId(),orderService.getOrdersByUserId(1).get(0).getCommodityId());
    }

    @Test
    void updateOrderState() {
        Assert.assertEquals(true,orderService.updateOrderState(6, PaymentStateEnum.UNCOMMENT));
    }
}