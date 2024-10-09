package com.seecoder.BlueWhale.serviceImpl;

import com.seecoder.BlueWhale.enums.PaymentStateEnum;
import com.seecoder.BlueWhale.exception.BlueWhaleException;
import com.seecoder.BlueWhale.po.Order;
import com.seecoder.BlueWhale.po.User;
import com.seecoder.BlueWhale.repository.*;
import com.seecoder.BlueWhale.util.CommonUtil;
import com.seecoder.BlueWhale.util.SecurityUtil;
import com.seecoder.BlueWhale.util.TokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class OrderServiceImplTest {
    @Autowired
    OrderServiceImpl orderServiceImpl;

    @MockBean
    UserRepository userRepository;
    @MockBean
    StoreRepository storeRepository;
    @MockBean
    CommentRepository commentRepository;
    @MockBean
    CommodityRepository commodityRepository;
    @MockBean
    CommodityPicRepository commodityPicRepository;
    @MockBean
    OrderRepository orderRepository;
    @MockBean
    CouponRepository couponRepository;
    @MockBean
    CouponGroupRepository couponGroupRepository;
    @MockBean
    TokenUtil tokenUtil;
    @MockBean
    SecurityUtil securityUtil;
    @MockBean
    CommonUtil commonUtil;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setId(1);
        userRepository.save(user);
        Mockito.when(securityUtil.getCurrentUser()).thenReturn(user);
    }
    //使用支付宝沙箱_测试_3'
    //订单不存在
    @Test
    void pay_orderNotExist() {
        Order order = null;
        when(orderRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(BlueWhaleException.orderNotExist().getClass(),
                () -> orderServiceImpl.pay(1,null,null));
    }
    //订单状态不是未支付
    @Test
    void pay_OrderStateWrong(){
        Order order = new Order();
        order.setId(1);
        order.setPaymentState(PaymentStateEnum.UNCOMMENT);
        order.setUserId(1);
        orderRepository.save(order);
        Mockito.when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        assertThrows(BlueWhaleException.orderStatusError().getClass(),
                () -> orderServiceImpl.pay(1,null,null));
    }
}