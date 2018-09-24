package com.baymin.springboot.store.payload;

import com.baymin.springboot.store.entity.BasicItem;
import com.baymin.springboot.store.entity.Invoice;
import com.baymin.springboot.store.entity.Question;
import com.baymin.springboot.store.enumconstant.CareType;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class UserOrderVo {

    private String orderUserId;

    private CareType orderType;

    private String payway; //支付方式 ONLINE_WECHAT  OFFLINE

    private Invoice invoice;

    private Long serviceStartDate;

    private Long serviceEndDate;

    private Double serviceDuration;

    private Integer serviceNumber;

    private String contact;

    private String contactMobile;

    private String serviceAddress;

    private String productId;

    private List<BasicItem> basicItems;

    private List<Question> questions;

    private Map<String, Object> extension;

}
