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
import com.baymin.springboot.store.payload.report.QuickOrderReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IOrderService {

    Order saveUserOrder(UserOrderVo request);

    List<Order> queryUserOrder(String userId, String status, String ownerType, Date minDate, Date maxDate);

    OrderDetailVo queryOrderDetail(String orderId);

    Map<String, Object> getOrderBasic(String orderId, String type);

    void orderEvaluate(Evaluate evaluate);

    void saveInvoiceRequest(Invoice invoice);

    Page<Order> queryOrderForPage(Pageable pageable, OrderStatus status, String orderId, CareType careType, Date maxDate, Date minDate, String payStatus, String orderSource, String account, String address, Set<String> hospitalNameSet);

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

    List<HospitalBizVo> queryHospitalBizReport(PageRequest pageRequest, String serviceStaffId, Date maxDate, Date minDate, Set<String> hospitalNameSet);

    List<Order> queryHospitalOrder(String orderTime, String staffId);

    List<Evaluate> queryOrderEvaluate(String orderId, CommonDealStatus dealStatus);

    List<QuickOrderReport> queryQuickOrderReport(Set<String> hospitalNameSet, Date maxDate, Date minDate, Date paymaxDate, Date payminDate, String hospitalDepartment, String groupType);

    List<Order> queryQuickOrder(Date minDate, Date maxDate, String hospitalName, Date paymaxDate, Date payminDate, String queryType, String serviceScope, String department);

    List<Evaluate> queryEvaluate(String orderId, String userId);

    Page<Order> queryQuickOrderForPage(PageRequest pageRequest, Date minDate, Date maxDate, String hospitalName, Date paymaxDate, Date payminDate, String queryType, String serviceScope, String department);
}
