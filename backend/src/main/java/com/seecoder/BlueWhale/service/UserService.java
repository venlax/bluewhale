package com.seecoder.BlueWhale.service;

import com.seecoder.BlueWhale.vo.CommodityVO;
import com.seecoder.BlueWhale.vo.StoreVO;
import com.seecoder.BlueWhale.vo.UserVO;

import java.util.List;

public interface UserService {
    Boolean register(UserVO userVO);

    String login(String phone,String password);

    UserVO getInformation();

    Boolean updateInformation(UserVO userVO);

    String generateStatement();

    String getNameById(Integer id);

    UserVO getInformationById(Integer id);

    Boolean addDeliveryInfo(String address, String phone);


}