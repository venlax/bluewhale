package com.seecoder.BlueWhale.service;

import com.seecoder.BlueWhale.enums.CommodityTypeEnum;
import com.seecoder.BlueWhale.vo.CommodityVO;

import java.math.BigDecimal;
import java.util.List;

public interface CommodityService {
    Boolean createCommodity(CommodityVO commodityVO);

    List<CommodityVO> getCommodityList(Integer storeId);

    Boolean updateInventory(Integer storeId, Integer id,  Integer newInventory);


    CommodityVO getCommodity(Integer id);

    Boolean updateInformation(CommodityVO commodityVO);

    Boolean deleteCommodity( Integer commodityId);

    List<CommodityVO> searchCommodity(String keyword, CommodityTypeEnum type, CommodityTypeEnum.CommoditySubTypeEnum subType, BigDecimal low, BigDecimal high);

}
