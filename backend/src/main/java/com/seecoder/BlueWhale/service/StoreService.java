package com.seecoder.BlueWhale.service;

import com.seecoder.BlueWhale.vo.StoreVO;

import java.util.List;

public interface StoreService {
    Boolean createStore(StoreVO storeVO);

    List<StoreVO> getAllStores();

    StoreVO getStore(Integer id);

    Boolean updateInformation(StoreVO storeVO);

    Boolean deleteStore(Integer id);
}
