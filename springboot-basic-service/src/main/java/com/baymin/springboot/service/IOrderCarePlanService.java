package com.baymin.springboot.service;

import com.baymin.springboot.store.entity.OrderCarePlan;
import com.baymin.springboot.store.entity.OrderCarePlanRecord;

import java.util.List;

public interface IOrderCarePlanService {
    OrderCarePlan findByOrderId(String orderId);

    void saveOrderCarePlan(OrderCarePlan orderCarePlan);

    void saveOrderCarePlanRecord(String careplanId, String careplanSubId, String operate);

    List<OrderCarePlanRecord> findRecordByCarePlanId(String carePlanId);
}
