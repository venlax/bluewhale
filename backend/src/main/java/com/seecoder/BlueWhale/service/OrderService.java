package com.seecoder.BlueWhale.service;

import com.alipay.api.AlipayApiException;
import com.seecoder.BlueWhale.enums.PaymentStateEnum;
import com.seecoder.BlueWhale.vo.CommentVO;
import com.seecoder.BlueWhale.vo.OrderVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface OrderService {
    Integer createOrder(OrderVO orderVO);

    List<OrderVO> getOrdersByStoreId(Integer storeId);

    List<OrderVO> getOrdersByUserId(Integer userId);

    List<OrderVO> getOrdersByCommodityId(Integer commodityId);

    List<OrderVO> getOrders();

    OrderVO getOrderById(Integer orderId);

    Boolean updateOrderState(Integer orderId, PaymentStateEnum state);

//    Boolean deliverOrder(Integer orderId);

    void pay(Integer orderId, Integer couponId, HttpServletResponse httpServletResponse) throws IOException;

    BigDecimal getPrice(Integer orderId, Integer couponId);

    Boolean deleteOrder(Integer orderId);

    String payNotify(HttpServletRequest request) throws AlipayApiException;
}
