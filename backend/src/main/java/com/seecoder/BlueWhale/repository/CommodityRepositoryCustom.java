package com.seecoder.BlueWhale.repository;

import com.seecoder.BlueWhale.enums.CommodityTypeEnum;
import com.seecoder.BlueWhale.po.Commodity;

import java.math.BigDecimal;
import java.util.List;

public interface CommodityRepositoryCustom {
    List<Commodity> findByCriteria(
            String keyword,
            CommodityTypeEnum type,
            CommodityTypeEnum.CommoditySubTypeEnum subType,
            BigDecimal minPrice,
            BigDecimal maxPrice
    );
}
