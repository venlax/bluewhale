package com.seecoder.BlueWhale.serviceImpl;

import com.seecoder.BlueWhale.enums.RoleEnum;
import com.seecoder.BlueWhale.exception.BlueWhaleException;
import com.seecoder.BlueWhale.po.DeliveryInfo;
import com.seecoder.BlueWhale.po.User;
import com.seecoder.BlueWhale.repository.*;
import com.seecoder.BlueWhale.service.UserService;
import com.seecoder.BlueWhale.util.CommonUtil;
import com.seecoder.BlueWhale.util.SecurityUtil;
import com.seecoder.BlueWhale.util.TokenUtil;
import com.seecoder.BlueWhale.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: GaoZhaolong
 * @Date: 14:46 2023/11/26
 *
 * 注册登录功能实现
*/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    CommodityRepository commodityRepository;

    @Autowired
    CommodityPicRepository commodityPicRepository;

    @Autowired
    DeliveryInfoRepository deliveryInfoRepository;


    
    @Autowired
    TokenUtil tokenUtil;

    @Autowired
    SecurityUtil securityUtil;

    @Autowired
    CommonUtil commonUtil;

    private UserVO getUserVO(User user) {
        UserVO userVO = user.toVO();
        if (userVO.getStoreName() == null) {
            userVO.setStoreName(commonUtil.getStoreNameById(userVO.getStoreId()));
        }

        List<DeliveryInfo> deliveryInfos = deliveryInfoRepository.findByUserId(user.getId());
        userVO.setDeliveryInfos(deliveryInfos);
        return userVO;
    }



    /**
     * 注册
     * @param userVO
     * @return 是否注册成功
     */
    @Override
    public Boolean register(UserVO userVO) {
        User user = userRepository.findByPhone(userVO.getPhone());
        if (user != null) {
            throw BlueWhaleException.phoneAlreadyExists();
        }
        Integer id = userVO.getStoreId();



        if ((!(id == null && (userVO.getRole() == RoleEnum.MANAGER || userVO.getRole() == RoleEnum.CEO || userVO.getRole() == RoleEnum.CUSTOMER))) && (id != null && !storeRepository.existsById(id))) {

            throw BlueWhaleException.storeNotExist();
        }

        User newUser = userVO.toPO();
        newUser.setCreateTime(new Date());
        DeliveryInfo deliveryInfo = new DeliveryInfo();
        deliveryInfo.setAddress(newUser.getAddress());
        deliveryInfo.setPhone(newUser.getPhone());
        userRepository.save(newUser);
        deliveryInfo.setUserId(newUser.getId());
        deliveryInfoRepository.save(deliveryInfo);
        return true;
    }

    /**
     * 登录
     * @param phone
     * @param password
     * @return token
     */
    @Override
    public String login(String phone, String password) {
        User user = userRepository.findByPhoneAndPassword(phone, password);
        if (user == null) {
            throw BlueWhaleException.phoneOrPasswordError();
        }
        return tokenUtil.getToken(user);
    }

    /**
     *  添加配送信息
     * @param address
     * @param phone
     * @return 是否添加成功
     */
    @Override
    public Boolean addDeliveryInfo(String address, String phone) {
        User user = securityUtil.getCurrentUser();
        DeliveryInfo deliveryInfo = new DeliveryInfo();

        if (deliveryInfoRepository.existsByUserIdAndAddressAndPhone(user.getId(), address, phone)) {
            throw BlueWhaleException.deliveryInfoAlreadyExists();
        }

        deliveryInfo.setUserId(user.getId());
        deliveryInfo.setAddress(address);
        deliveryInfo.setPhone(phone);
        userRepository.save(user);
        deliveryInfoRepository.save(deliveryInfo);
        return true;
    }

    /**
     * 获取用户信息
     * @return 用户信息
     */
    @Override
    public UserVO getInformation() {

        return getUserVO(securityUtil.getCurrentUser());
    }

    /**
     * 更新用户信息
     * @param userVO
     * @return 是否更新成功
     */
    @Override
    public Boolean updateInformation(UserVO userVO) {
        User user=securityUtil.getCurrentUser();
        if (userVO.getPassword()!=null){
            user.setPassword(userVO.getPassword());
        }
        if (userVO.getName()!=null){
            user.setName(userVO.getName());
        }
        if (userVO.getAddress()!=null){
            user.setAddress(userVO.getAddress());
        }
        userRepository.save(user);
        return true;
    }

    /**
     * 生成报表
     * @return 报表
     */
    @Override
    public  String generateStatement() {
        return commonUtil.generateStatement();
    }

    /**
     * 获取用户姓名
     * @param id
     * @return 用户姓名
     */
    @Override
    @Cacheable(value = "userName", key = "#id")
    public String getNameById(Integer id) {
        return userRepository.findById(id).map(User::getName).orElse(null);
    }

    /**
     * 获取用户信息
     * @param id
     * @return 用户信息
     */
    @Override
    public UserVO getInformationById(Integer id) {
        return userRepository.findById(id).map(this::getUserVO).orElse(null);
    }


}
