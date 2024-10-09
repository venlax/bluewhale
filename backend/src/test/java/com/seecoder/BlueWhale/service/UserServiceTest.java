package com.seecoder.BlueWhale.service;
import com.seecoder.BlueWhale.enums.RoleEnum;
import com.seecoder.BlueWhale.po.User;
import com.seecoder.BlueWhale.repository.*;
import com.seecoder.BlueWhale.util.CommonUtil;
import com.seecoder.BlueWhale.util.SecurityUtil;
import com.seecoder.BlueWhale.util.TokenUtil;
import com.seecoder.BlueWhale.vo.UserVO;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceTest {
    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DeliveryInfoRepository deliveryInfoRepository;

    @Autowired
    TokenUtil tokenUtil;

    @Autowired
    SecurityUtil securityUtil;

    @Autowired
    CommonUtil commonUtil;

    public UserVO userVO;

    @BeforeEach
    void setUp() {
        userVO = new UserVO();
        userVO.setName("testUser");
    }

    //对注册、登录的集成测试：
    //@Transactional和@Rollback注解使测试后数组库无脏数据
    @Test
    @Transactional
    @Rollback
    void register() {
        userVO.setPassword("123456");
        userVO.setRole(RoleEnum.CUSTOMER);
        userVO.setPhone("18045302907");
        Assert.assertEquals(true, userService.register(userVO));
    }

    @Test
    @Transactional
    @Rollback
    void login() {
        userVO.setPassword("123456");
        userVO.setRole(RoleEnum.CUSTOMER);
        userVO.setPhone("18045302907");
        userService.register(userVO);
        Assert.assertEquals(tokenUtil.getToken(userRepository.findByPhoneAndPassword("18045302907","123456")),userService.login("18045302907","123456"));
    }

    @Test
    void getNameById() {
        Assert.assertEquals(userRepository.findById(1).map(User::getName).orElse(null),userService.getNameById(1));
    }
}