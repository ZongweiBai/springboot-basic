package com.baymin.springboot.service;

import com.baymin.springboot.store.entity.*;
import com.baymin.springboot.store.enumconstant.CareType;
import com.baymin.springboot.store.enumconstant.CommonDealStatus;
import com.baymin.springboot.store.enumconstant.OrderStatus;
import com.baymin.springboot.store.payload.EditOrderRequestVo;
import com.baymin.springboot.store.payload.OrderDetailVo;
import com.baymin.springboot.store.payload.UserOrderVo;
import com.baymin.springboot.store.payload.report.HospitalBizVo;
import com.baymin.springboot.store.payload.report.JSVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IOrderService {

    Order saveUserOrder(UserOrderVo request);

    List<Order> queryUserOrder(String userId, String status, String ownerType);

    OrderDetailVo queryOrderDetail(String orderId);

    Map<String, Object> getOrderBasic(String orderId, String type);

    void orderEvaluate(Evaluate evaluate);

    void saveInvoiceRequest(Invoice invoice);

    Page<Order> queryOrderForPage(Pageable pageable, OrderStatus status, String orderId, CareType careType, Date maxDate, Date minDate, String payStatus, String orderSource, String account, String address);

    Map<String, Object> getOrderDetail(String orderId);

    void assignOrderStaff(String orderId, String staffId, String adminId, String nurseId);

    void offlinePay(PayRecord payRecord, Admin sysUser);

    void staffChangeRequest(OrderStaffChange staffChange);

    void serviceStart(String orderId);

    Map<String, Object> getOrderBasicWithUserInfo(String orderId);

    Order queryOrderById(String orderId);

    List<OrderStaffChange> queryUserStaffChange(String userId);

    void orderCompleted(String orderId);

    List<Order> queryOrderForList(CareType careType, Date maxDate, Date minDate, OrderStatus status);

    void editUserOrder(EditOrderRequestVo requestVo);

    Page<Order> queryOrderStatisticForPage(PageRequest pageRequest, String careTypes, String hospitalAddress, Date maxDate, Date minDate);

    List<JSVo> queryOrderJSReport(PageRequest pageRequest, String careTypes, String hospitalAddress, Date maxDate, Date minDate);

    List<HospitalBizVo> queryHospitalBizReport(PageRequest pageRequest, String serviceStaffId, Date maxDate, Date minDate);

    List<Order> queryHospitalOrder(String orderTime, String staffId);

    List<Evaluate> queryOrderEvaluate(String orderId, CommonDealStatus dealStatus);
}
