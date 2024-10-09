package com.seecoder.BlueWhale.controller;

import com.seecoder.BlueWhale.po.Commodity;
import com.seecoder.BlueWhale.service.UserService;
import com.seecoder.BlueWhale.vo.CommodityVO;
import com.seecoder.BlueWhale.vo.ResultVO;
import com.seecoder.BlueWhale.vo.StoreVO;
import com.seecoder.BlueWhale.vo.UserVO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResultVO<Boolean> register(@RequestBody UserVO userVO){
        return ResultVO.buildSuccess(userService.register(userVO));
    }

    @PostMapping("/login")
    public ResultVO<String> login(@RequestParam("phone") String phone, @RequestParam("password") String password){
        return ResultVO.buildSuccess(userService.login(phone, password));
    }


    @GetMapping("/getNameById")
    public ResultVO<String> getNameById(@RequestParam("id") Integer id){
        return ResultVO.buildSuccess(userService.getNameById(id));
    }

    @GetMapping("/getInformationById")
    public ResultVO<UserVO> getInformationById(@RequestParam("id") Integer id){
        return ResultVO.buildSuccess(userService.getInformationById(id));
    }

    @GetMapping
    public ResultVO<UserVO> getInformation(){
        return ResultVO.buildSuccess(userService.getInformation());
    }

    @PostMapping("/update")
    public ResultVO<Boolean> updateInformation(@RequestBody UserVO userVO){
        return ResultVO.buildSuccess(userService.updateInformation(userVO));
    }

    @GetMapping("/generate_statement")
    public ResultVO<String> generateStatement()
    {
        return ResultVO.buildSuccess(userService.generateStatement());
    }

    @PostMapping("/addDeliveryInfo")
    public ResultVO<Boolean> addDeliveryInfo(@RequestParam("address") String address, @RequestParam("phone") String phone){
        return ResultVO.buildSuccess(userService.addDeliveryInfo(address, phone));
    }

}
