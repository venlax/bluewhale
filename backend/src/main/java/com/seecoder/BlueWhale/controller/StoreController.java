package com.seecoder.BlueWhale.controller;

import com.seecoder.BlueWhale.service.StoreService;
import com.seecoder.BlueWhale.vo.ResultVO;
import com.seecoder.BlueWhale.vo.StoreVO;
import com.seecoder.BlueWhale.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
public class StoreController {
    @Autowired
    StoreService storeService;

    @PostMapping("/createStore")
    public ResultVO<Boolean> createStore(@RequestBody StoreVO storeVO) {
        return ResultVO.buildSuccess(storeService.createStore(storeVO));
    }

    @GetMapping
    public ResultVO<List<StoreVO>> getAllStores() {
        return ResultVO.buildSuccess(storeService.getAllStores());
    }

    @GetMapping("/{id}")
    public ResultVO<StoreVO> getStore(@PathVariable(value = "id") Integer id) {
        return ResultVO.buildSuccess(storeService.getStore(id));
    }

    @PostMapping("/update")
    public ResultVO<Boolean> updateInformation(@RequestBody StoreVO storeVO){
        return ResultVO.buildSuccess(storeService.updateInformation(storeVO));
    }

    @PostMapping("/delete/{id}")
    public ResultVO<Boolean> deleteStore(@PathVariable(value = "id") Integer id) {
        return ResultVO.buildSuccess(storeService.deleteStore(id));
    }
}
