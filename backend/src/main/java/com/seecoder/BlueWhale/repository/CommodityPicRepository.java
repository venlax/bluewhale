package com.seecoder.BlueWhale.repository;

import com.seecoder.BlueWhale.po.CommodityPic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommodityPicRepository extends JpaRepository<CommodityPic, Integer> {
    List<CommodityPic> findByCommodityId(Integer commodityId);
}
