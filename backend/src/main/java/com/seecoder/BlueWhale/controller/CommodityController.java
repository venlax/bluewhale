package com.seecoder.BlueWhale.controller;

import com.seecoder.BlueWhale.enums.CommodityTypeEnum;
import com.seecoder.BlueWhale.service.CommodityService;
import com.seecoder.BlueWhale.vo.CommodityVO;
import com.seecoder.BlueWhale.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/commodities")
public class CommodityController {
    @Autowired
    CommodityService commodityService;

    @GetMapping
    public ResultVO<List<CommodityVO>> getCommoditiesList(@RequestParam Integer storeId) {
        return ResultVO.buildSuccess(commodityService.getCommodityList(storeId));
    }

    @PostMapping("/createCommodity")
    public ResultVO<Boolean> createCommodity(@RequestBody CommodityVO commodityVO) {
        return ResultVO.buildSuccess(commodityService.createCommodity(commodityVO));
    }



    @PostMapping("/updateInventory")
    public ResultVO<Boolean> updateInventory(@RequestParam Integer storeId, @RequestParam Integer id, @RequestParam Integer newInventory) {
        return ResultVO.buildSuccess(commodityService.updateInventory(storeId, id, newInventory));
    }

    @GetMapping("/{id}")
    public ResultVO<CommodityVO> getCommodity(@PathVariable(value = "id") Integer id) {
        return ResultVO.buildSuccess(commodityService.getCommodity(id));
    }

    @PostMapping("/update")
    public ResultVO<Boolean> updateInformation(@RequestBody CommodityVO commodityVO){
        return ResultVO.buildSuccess(commodityService.updateInformation(commodityVO));
    }

    @PostMapping("/search")
    public ResultVO<List<CommodityVO>> searchCommodity(@RequestParam(required = false) String keyword, @RequestParam(required = false) CommodityTypeEnum type, @RequestParam(required = false) CommodityTypeEnum.CommoditySubTypeEnum subType, @RequestParam(required = false) BigDecimal low, @RequestParam(required = false) BigDecimal high) {
        return ResultVO.buildSuccess(commodityService.searchCommodity(keyword, type, subType, low, high));
    }
    @PostMapping("/delete")
    public ResultVO<Boolean> deleteCommodity( @RequestParam Integer commodityId) {
        return ResultVO.buildSuccess(commodityService.deleteCommodity(commodityId));
    }
}
