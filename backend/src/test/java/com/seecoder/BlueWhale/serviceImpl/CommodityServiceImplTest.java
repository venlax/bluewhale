package com.seecoder.BlueWhale.serviceImpl;

import com.seecoder.BlueWhale.enums.CommodityTypeEnum;
import com.seecoder.BlueWhale.po.Commodity;
import com.seecoder.BlueWhale.repository.*;
import com.seecoder.BlueWhale.util.CommonUtil;
import com.seecoder.BlueWhale.util.SecurityUtil;
import com.seecoder.BlueWhale.util.TokenUtil;
import com.seecoder.BlueWhale.vo.CommodityVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommodityServiceImplTest {
    @Autowired
    CommodityServiceImpl commodityServiceImpl;

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
        List<Commodity> commodityList = new ArrayList<>();
        Commodity cat = new Commodity();
        cat.setName("cat dress");
        cat.setType(CommodityTypeEnum.CLOTHES);
        commodityList.add(cat);
        Mockito.when(commodityRepository.findByCriteria("cat", CommodityTypeEnum.CLOTHES,null,null,null)).thenReturn(commodityList);
    }

    // 正常搜索商品的情况
    @Test
    void searchCommodity() {
        List<CommodityVO> commodityVOList = commodityServiceImpl.searchCommodity("cat", CommodityTypeEnum.CLOTHES,null,null, null);
        assertEquals(commodityVOList.get(0).getName(), "cat dress");
        assertEquals(commodityVOList.get(0).getType(), CommodityTypeEnum.CLOTHES);
    }
}