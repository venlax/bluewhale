package com.seecoder.BlueWhale.controller;

import com.seecoder.BlueWhale.enums.PaymentStateEnum;
import com.seecoder.BlueWhale.po.Order;
import com.seecoder.BlueWhale.service.OrderService;
import com.seecoder.BlueWhale.vo.CommentVO;
import com.seecoder.BlueWhale.vo.OrderVO;
import com.seecoder.BlueWhale.vo.ResultVO;
import com.seecoder.BlueWhale.vo.StoreVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping("/createOrder")
    public ResultVO<Integer> createOrder(@RequestBody  OrderVO orderVO) {
        return ResultVO.buildSuccess(orderService.createOrder(orderVO));
    }
    @ResponseBody
    @PostMapping("/pay") // 前端接收到的res需要把对象渲染出来
    public void pay(@RequestParam Integer orderId, @RequestParam(required = false) Integer couponId, HttpServletResponse httpServletResponse) throws IOException {
//        System.out.println("pay");

       orderService.pay(orderId, couponId, httpServletResponse);

    }



    @PostMapping("/notify")  // 这里是由pay成功之后的回调函数,不能直接由前端调用, 注意在开启内网穿透后修改application.yml中的notify_url
    public String payNotify(HttpServletRequest request) throws Exception {
        return orderService.payNotify(request);
    }
    @GetMapping("/getPrice")
    public ResultVO<BigDecimal> getPrice(@RequestParam Integer orderId, @RequestParam(required = false) Integer couponId) {
        return ResultVO.buildSuccess(orderService.getPrice(orderId, couponId));
    }

    @GetMapping("/getOrdersByStoreId/{storeId}")
    public ResultVO<List<OrderVO>> getOrdersByStoreId(@PathVariable(value = "storeId") Integer storeId) {
        return ResultVO.buildSuccess(orderService.getOrdersByStoreId(storeId));
    }

    @GetMapping("/getOrdersByUserId/{userId}")
    public ResultVO<List<OrderVO>> getOrdersByUserId(@PathVariable(value = "userId") Integer userId) {
        return ResultVO.buildSuccess(orderService.getOrdersByUserId(userId));
    }

    @GetMapping("/getOrdersByCommodityId/{commodityId}")
    public ResultVO<List<OrderVO>> getOrdersByCommodityId(@PathVariable(value = "commodityId")  Integer commodityId) {
        return ResultVO.buildSuccess(orderService.getOrdersByCommodityId(commodityId));
    }

    @GetMapping("/getOrderById/{orderId}")
    public ResultVO<OrderVO> getOrderById(@PathVariable(value = "orderId") Integer orderId) {
        return ResultVO.buildSuccess(orderService.getOrderById(orderId));
    }


    @GetMapping("/getOrders")
    public ResultVO<List<OrderVO>> getOrders() {
        return ResultVO.buildSuccess(orderService.getOrders());
    }

    @PostMapping("/updateOrderState")
    public ResultVO<Boolean> updateOrderState(@RequestParam Integer orderId, @RequestParam PaymentStateEnum state) {
        return ResultVO.buildSuccess(orderService.updateOrderState(orderId, state));
    }

    @PostMapping("/deleteOrder")
    public ResultVO<Boolean> deleteOrder(@RequestParam Integer orderId) {
        return ResultVO.buildSuccess(orderService.deleteOrder(orderId));
    }



}
