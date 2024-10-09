package com.seecoder.BlueWhale.po;

import com.seecoder.BlueWhale.enums.DeliveryMethodEnum;
import com.seecoder.BlueWhale.enums.PaymentStateEnum;
import com.seecoder.BlueWhale.vo.OrderVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "`order`", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_store_id", columnList = "store_id"),
        @Index(name = "idx_commodity_id", columnList = "commodity_id")
}) // order是mysql的关键字，所以要加上``
public class Order {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "payment_state")
    @Enumerated(EnumType.STRING)
    private PaymentStateEnum paymentState;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "store_id")
    private Integer storeId;

    @Column(name = "commodity_id")
    private Integer commodityId;

    @Column(name = "count")
    private Integer count; // 购买数量

    @Column(name= "delivery_method")
    @Enumerated(EnumType.STRING)
    private DeliveryMethodEnum deliveryMethod;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @Column(name = "delivery_phone")
    private String deliveryPhone;

    @Column(name = "pick_date")
    private Date pickDate;


    @Column(name = "price", scale = 2)
    private BigDecimal price;
    public OrderVO toVO() {
        OrderVO order = new OrderVO();
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
