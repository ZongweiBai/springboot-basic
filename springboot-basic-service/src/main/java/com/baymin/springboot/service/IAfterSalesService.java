package com.baymin.springboot.service;

import com.baymin.springboot.store.entity.Evaluate;
import com.baymin.springboot.store.entity.Invoice;
import com.baymin.springboot.store.entity.OrderRefund;
import com.baymin.springboot.store.entity.OrderStaffChange;
import com.baymin.springboot.store.enumconstant.CommonDealStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IAfterSalesService {
    Page<OrderStaffChange> queryOrderChangePage(Pageable pageable, CommonDealStatus dealStatus, Date maxDate, Date minDate, String orderId);

    Page<Evaluate> queryEvaluatePage(Pageable pageable, Integer grade, String orderId, CommonDealStatus auditStatus, Date maxDate, Date minDate);

    Page<OrderRefund> queryRefundPage(Pageable pageable, CommonDealStatus dealStatus, Date maxDate, Date minDate, String orderId);

    Map<String, Object> getRefundInfo(String refundId);

    void dealStaffChange(OrderStaffChange change);

    Map<String, Object> getChangeDetail(String changeId);

    Page<OrderRefund> queryRefundPageForFinance(Pageable pageable, CommonDealStatus dealStatus, Date maxDate, Date minDate, String orderId);

    Page<Invoice> queryInvoicePage(Pageable pageable, CommonDealStatus dealStatus, Date maxDate, Date minDate, String orderId);

    void updateInvoice(String invoiceId, CommonDealStatus dealStatus);

    void deleteEvaluate(String evaluateId);

    Evaluate getEvaluateInfo(String evaluateId);

    void dealEvaluate(Evaluate evaluate);

    List<Evaluate> queryEvaluateList(Integer grade, String orderId, Date maxDate, Date minDate);

    void replyEvaluate(Evaluate evaluate);
}
