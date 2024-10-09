package com.seecoder.BlueWhale.service;

import com.seecoder.BlueWhale.enums.RoleEnum;
import com.seecoder.BlueWhale.po.Store;
import com.seecoder.BlueWhale.po.User;
import com.seecoder.BlueWhale.repository.*;
import com.seecoder.BlueWhale.serviceImpl.StoreServiceImpl;
import com.seecoder.BlueWhale.util.CommonUtil;
import com.seecoder.BlueWhale.util.SecurityUtil;
import com.seecoder.BlueWhale.util.TokenUtil;
import com.seecoder.BlueWhale.vo.StoreVO;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
class StoreServiceTest {

    @Autowired
    StoreService storeService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    CommodityRepository commodityRepository;

    @Autowired
    CommodityPicRepository commodityPicRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    CouponGroupRepository couponGroupRepository;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    TokenUtil tokenUtil;

    @Autowired
    SecurityUtil securityUtil;

    @Autowired
    CommonUtil commonUtil;

    @Autowired
    CommodityService commodityService;

    public static StoreVO storeVO;
    @BeforeEach
    void setUp() {
        storeVO = new StoreVO();
        storeVO.setName("testStore");
        storeVO.setIntroduction("very good");
    }

    @Test
    void getAllStores() {
        Assert.assertEquals(storeRepository.findAll().stream()
                .map(Store::toVO)
                .collect(Collectors.toList()).get(0).getName(),storeService.getAllStores().get(0).getName());
    }

    @Test
    void getStore() {
        Assert.assertEquals(storeRepository.findById(1).map(Store::toVO).orElse(null).getName(),storeService.getStore(1).getName());
    }

}