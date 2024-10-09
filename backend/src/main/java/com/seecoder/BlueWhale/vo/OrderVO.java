package com.seecoder.BlueWhale.vo;

import com.seecoder.BlueWhale.enums.DeliveryMethodEnum;
import com.seecoder.BlueWhale.enums.PaymentStateEnum;
import com.seecoder.BlueWhale.po.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

import java.util.Date;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderVO {

    private Integer id;


    private PaymentStateEnum paymentState;

    private Date createTime;


    private Integer userId;


    private Integer storeId;


    private Integer commodityId;


    private Integer count;


    private DeliveryMethodEnum deliveryMethod;

    private BigDecimal price;

    private String deliveryAddress;

    private String deliveryPhone;

    private Date pickDate;

    private Integer couponId;


    public Order toPO() {
        Order order = new Order();
        order.setId(this.id);
        order.setCount(this.count);
        order.setStoreId(this.storeId);
        order.setCommodityId(this.commodityId);
        order.setDeliveryMethod(this.deliveryMethod);
        order.setUserId(this.userId);
        order.setPaymentState(this.paymentState);
        order.setCreateTime(this.createTime);
        order.setDeliveryAddress(this.deliveryAddress);
        order.setDeliveryPhone(this.deliveryPhone);
        order.setPickDate(this.pickDate);
        order.setPrice(this.price);
        return order;
    }

}
