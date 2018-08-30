package com.baymin.springboot.store.payload;

import com.baymin.springboot.store.entity.Invoice;
import com.baymin.springboot.store.enumconstant.OrderType;
import lombok.Data;

import java.util.Map;

@Data
public class UserOrderRequest {

    private String orderUserId;

    private OrderType orderType;

    private String payway; //支付方式 ONLINE_WECHAT  OFFLINE

    private Invoice invoice;

    private Long serviceStartTime;

    private Long serviceEndDate;

    private Double serviceDuration;

    private Integer serviceNumber;

    private String contact;

    private String contactMobile;

    private String serviceAddress;

    private Map<String, Object> extension;

}
