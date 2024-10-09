package com.seecoder.BlueWhale.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.seecoder.BlueWhale.enums.CouponTypeEnum;
import com.seecoder.BlueWhale.enums.PaymentStateEnum;
import com.seecoder.BlueWhale.enums.RoleEnum;
import com.seecoder.BlueWhale.exception.BlueWhaleException;
import com.seecoder.BlueWhale.po.*;
import com.seecoder.BlueWhale.repository.*;
import com.seecoder.BlueWhale.service.OrderService;
import com.seecoder.BlueWhale.serviceImpl.strategy.Context;
import com.seecoder.BlueWhale.serviceImpl.strategy.FillReductionCouponCalculateStrategy;
import com.seecoder.BlueWhale.serviceImpl.strategy.SpecialCouponCalculateStrategy;
import com.seecoder.BlueWhale.util.SecurityUtil;
import com.seecoder.BlueWhale.vo.CommentVO;
import com.seecoder.BlueWhale.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {


    @Value("${alipay.app-id}")
    private String appId;
    @Value("${alipay.private-key}")
    private String privateKey;
    @Value("${alipay.alipay-public-key}")
    private String alipayPublicKey;
    @Value("${alipay.server-url}")
    private String serverUrl;
    @Value("${alipay.charset}")
    private String charset;
    @Value("${alipay.sign-type}")
    private String signType;
    @Value("${alipay.notify-url}")
    private String notifyUrl;
    @Value("${alipay.return-url}")
    private String returnUrl;
    private static final String FORMAT = "JSON";



    @Autowired
    OrderRepository orderRepository;

    @Autowired
    CommodityRepository commodityRepository;

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    CouponGroupRepository couponGroupRepository;



    @Autowired
    SecurityUtil securityUtil;

    Context context1 = new Context(new SpecialCouponCalculateStrategy());
    Context context2 = new Context(new FillReductionCouponCalculateStrategy());

    private BigDecimal getPrice(Integer commodityId, OrderVO orderVO, Integer couponId, Boolean use){
        if (couponId == null) {
            return Objects.requireNonNull(commodityRepository.findById(commodityId).orElse(null)).getPrice().multiply(BigDecimal.valueOf(orderVO.getCount()));
        }
        Coupon coupon = couponRepository.findById(couponId).orElse(null);
        if (coupon == null) {
            throw BlueWhaleException.couponNotExist();
        } else if (coupon.getIsUsed()) {
            throw BlueWhaleException.couponUsed();
        }
        Commodity commodity = commodityRepository.findById(commodityId).orElse(null);
        if (commodity == null) {
            throw BlueWhaleException.commodityNotExist();
        }
        BigDecimal res;
        if (coupon.getCouponType().equals(CouponTypeEnum.SPECIAL)) {
            res =  context1.executeStrategy(commodity.getPrice().multiply(BigDecimal.valueOf(orderVO.getCount())), coupon).setScale(2, BigDecimal.ROUND_HALF_UP);
        } else {
            res =  context2.executeStrategy(commodity.getPrice().multiply(BigDecimal.valueOf(orderVO.getCount())), coupon).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        if (use) {
            coupon.setIsUsed(true);
            couponRepository.save(coupon);
        }
        return res;
    }

    private void cancelOrder(Order order) {
        Commodity commodity = commodityRepository.findById(order.getCommodityId()).orElse(null);
        if (commodity == null) {
            throw BlueWhaleException.commodityNotExist();
        }
        commodity.setInventory(commodity.getInventory() + order.getCount());
        commodityRepository.save(commodity);
        order.setPaymentState(PaymentStateEnum.CANCELLED);
        orderRepository.save(order);
    }

    private List<OrderVO> getOrderVOS(List<Order> orders) {
        return orders.stream().map(Order::toVO).collect(Collectors.toList());
    }

    /**
     * 获取订单价格
     * @param orderId
     * @param couponId
     * @return 订单价格
     */
    @Override
    public BigDecimal getPrice(Integer orderId, Integer couponId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            throw BlueWhaleException.orderNotExist();
        }
        return getPrice(order.getCommodityId(), order.toVO(), couponId, false);
    }

    /**
     * 创建订单
     * @param orderVO
     * @return 订单id
     */
    @Override
    public Integer createOrder(OrderVO orderVO) {


        User user = securityUtil.getCurrentUser();

        if (user == null || user.getRole() != RoleEnum.CUSTOMER) {
            throw BlueWhaleException.notCustomer();
        }

        Order order = orderVO.toPO();

        Store store = storeRepository.findById(orderVO.getStoreId()).orElse(null);

        if (store == null) {
            throw BlueWhaleException.storeNotExist();
        }

        Commodity commodity = commodityRepository.findById(orderVO.getCommodityId()).orElse(null);

        if (commodity == null) {
            throw BlueWhaleException.commodityNotExist();
        }
        if (!store.getId().equals(commodity.getStoreId())) {
            throw BlueWhaleException.commodityNotInStore();
        }

        if (orderVO.getCount() > commodity.getInventory()) {
            throw BlueWhaleException.inventoryNotEnough();
        }

        commodity.setInventory(commodity.getInventory() - orderVO.getCount());
        commodityRepository.save(commodity);

        order.setCreateTime(new Date());
        order.setPrice(getPrice(orderVO.getCommodityId(), orderVO, null, false));

        orderRepository.save(order);

        return order.getId();
    }


    @Scheduled(cron = "0 * * * * *") // 每分钟运行一次
    @Transactional
    public void cancelUnpaidOrders() {
        Date fourteenDaysAgo = new Date(System.currentTimeMillis() - 14 * 24 * 60 * 60 * 1000);
        List<Order> unpaidOrders = orderRepository.findByPaymentStateAndCreateTimeBefore(PaymentStateEnum.UNPAID, fourteenDaysAgo);

        for (Order order : unpaidOrders) {
            cancelOrder(order);
        }
    }

    /**
     * 唤起支付宝沙盒,支付订单
     * @param orderId
     * @param couponId
     * @param httpServletResponse
     *
     */
    @Override
    public void pay(Integer orderId, Integer couponId, HttpServletResponse httpServletResponse) throws IOException {
        User user=securityUtil.getCurrentUser();
        Order order = orderRepository.findById(orderId).orElse(null);
        if(order == null){
            throw BlueWhaleException.orderNotExist();
        }
        if(order.getPaymentState() != PaymentStateEnum.UNPAID){
            throw BlueWhaleException.orderStatusError();
        }
        if(user == null || !user.getId().equals(order.getUserId())){
            throw BlueWhaleException.notOrderOwner();
        }
        order.setPrice(getPrice(order.getCommodityId(), order.toVO(), couponId, true));
        orderRepository.save(order);

        // 1. 创建Client，通用SDK提供的Client，负责调用支付宝的API
        AlipayClient alipayClient = new DefaultAlipayClient(serverUrl, appId,
                privateKey, FORMAT, charset, alipayPublicKey, signType);
        // 2. 创建 Request并设置Request参数
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();  // 发送请求的 Request类
        request.setNotifyUrl(notifyUrl);
        request.setReturnUrl(returnUrl);
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", orderId);  // 订单编号
        bizContent.put("total_amount", order.getPrice()); // 订单的总金额
        bizContent.put("subject", "BlueWhale订单");  // 订单的标题
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");  // 固定配置
        request.setBizContent(bizContent.toString());
        // 执行请求，拿到响应的结果，返回给浏览器
        String form = "";

        try {
            form = alipayClient.pageExecute(request).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        httpServletResponse.setContentType("text/html;charset=" + charset);
        httpServletResponse.getWriter().write(form);// 直接将完整的表单html输出到页面
        httpServletResponse.getWriter().flush();
        httpServletResponse.getWriter().close();

    }

    /**
     * 支付宝异步回调
     * @param request
     * @return
     * @throws AlipayApiException
     */

    public String payNotify(HttpServletRequest request) throws AlipayApiException {
        if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
            //System.out.println("=========支付宝异步回调========");
            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (String name : requestParams.keySet()) {
                params.put(name, request.getParameter(name));
            }
            String sign = params.get("sign");
            String content = AlipaySignature.getSignCheckContentV1(params);
            boolean checkSignature = AlipaySignature.rsa256CheckContent(content, sign, alipayPublicKey, "UTF-8"); // 验证签名
            // 支付宝验签
            if (checkSignature) {

                Order order = orderRepository.findById(Integer.parseInt(params.get("out_trade_no"))).orElse(null);
                if (order == null) {
                    throw BlueWhaleException.orderNotExist();
                }
                order.setPaymentState(PaymentStateEnum.UNSEND);
                orderRepository.save(order);
            }

        }
        return "success";

    }

    /**
     * 通过商店Id获取订单
     * @param storeId
     * @return 订单列表
     */
    @Override
    public List<OrderVO> getOrdersByStoreId(Integer storeId) {
        User user = securityUtil.getCurrentUser();
        if (user == null || !user.getStoreId().equals(storeId) || !user.getRole().equals(RoleEnum.STAFF)) {
            throw BlueWhaleException.notStoreStaff();
        }
        List<Order> orders = orderRepository.findByStoreId(storeId);
        return getOrderVOS(orders);
    }

    /**
     * 通过用户Id获取订单
     * @param userId
     * @return 订单列表
     */


    @Override
    public List<OrderVO> getOrdersByUserId(Integer userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return getOrderVOS(orders);
    }

    /**
     * 获取当前用户所有订单
     * @return 订单列表
     */

    @Override
    public List<OrderVO> getOrders() {
        User user = securityUtil.getCurrentUser();

        if (user == null || !(user.getRole().equals(RoleEnum.CEO) || user.getRole().equals(RoleEnum.MANAGER))){
            throw BlueWhaleException.insufficientPermissions();
        }
        List<Order> orders = orderRepository.findAll();
        return getOrderVOS(orders);
    }

    /**
     * 通过商品Id获取订单
     * @param commodityId
     * @return 订单列表
     */

    @Override
    public List<OrderVO> getOrdersByCommodityId(Integer commodityId) {
        User user = securityUtil.getCurrentUser();
        // 权限判断未设置
        List<Order> orders = orderRepository.findByCommodityId(commodityId);
        return getOrderVOS(orders);
    }

    /**
     * 通过订单Id获取订单
     * @param  orderId
     * @return 订单信息
     */

    @Override
    public OrderVO getOrderById(Integer orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            throw BlueWhaleException.orderNotExist();
        }
        List<Order> orderList = List.of(order);
        return getOrderVOS(orderList).get(0);
    }

    /**
     * 通过订单Id更新订单状态
     * @param orderId
     * @param state
     * @return  是否更新成功
     */
    @Override
    public Boolean updateOrderState(Integer orderId, PaymentStateEnum state) {

        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            throw BlueWhaleException.orderNotExist();
        }
        if (state == PaymentStateEnum.CANCELLED) {
            cancelOrder(order);
            return true;
        }

        order.setPaymentState(state);
        orderRepository.save(order);
        return true;
    }

    /**
     * 通过订单Id删除订单
     * @param orderId
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteOrder(Integer orderId) {

        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            throw BlueWhaleException.orderNotExist();
        }
        if (order.getPaymentState() != PaymentStateEnum.CANCELLED) {
            throw BlueWhaleException.orderStatusError();
        }

        Commodity commodity = commodityRepository.findById(order.getCommodityId()).orElse(null);
        if (commodity == null) {
            throw BlueWhaleException.commodityNotExist();
        }
        orderRepository.delete(order);
        return true;
    }


}
