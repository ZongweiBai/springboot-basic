package com.baymin.springboot.adminserver.controller;

import com.baymin.springboot.adminserver.constant.WebConstant;
import com.baymin.springboot.common.util.DateUtil;
import com.baymin.springboot.service.IAfterSalesService;
import com.baymin.springboot.service.IOrderRefundService;
import com.baymin.springboot.store.entity.Invoice;
import com.baymin.springboot.store.entity.OrderRefund;
import com.baymin.springboot.store.enumconstant.CommonDealStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("finance")
public class FinanceController {

    @Autowired
    private IOrderRefundService orderRefundService;

    @Autowired
    private IAfterSalesService afterSalesService;

    @ResponseBody
    @PostMapping(value = "queryRefundForPage")
    public Map<String, Object> queryRefundForPage(Pageable pageable, CommonDealStatus dealStatus, String orderId,
                                                  String datemin, String datemax, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        Date maxDate = DateUtil.dayEnd(datemax);
        Date minDate = DateUtil.dayBegin(datemin);

        pageable.getSort().and(new Sort(Sort.Direction.DESC, "createTime"));
        Page<OrderRefund> queryResult = afterSalesService.queryRefundPageForFinance(pageable, dealStatus, maxDate, minDate, orderId);
        resultMap.put(WebConstant.TOTAL, queryResult.getTotalElements());
        resultMap.put(WebConstant.ROWS, queryResult.getContent());
        return resultMap;
    }

    @ResponseBody
    @PostMapping(value = "finishRefund")
    public Map<String, Object> finishRefund(String refundId, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            orderRefundService.updateOrderRefund(refundId, CommonDealStatus.COMPLETED, "财务已确认退款");
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "加载出错：" + e.getMessage());
        }
        return resultMap;
    }


    @ResponseBody
    @PostMapping(value = "queryInvoiceForPage")
    public Map<String, Object> queryInvoiceForPage(Pageable pageable, CommonDealStatus dealStatus, String orderId,
                                                  String datemin, String datemax, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        Date maxDate = DateUtil.dayEnd(datemax);
        Date minDate = DateUtil.dayBegin(datemin);

        pageable.getSort().and(new Sort(Sort.Direction.DESC, "createTime"));
        Page<Invoice> queryResult = afterSalesService.queryInvoicePage(pageable, dealStatus, maxDate, minDate, orderId);
        resultMap.put(WebConstant.TOTAL, queryResult.getTotalElements());
        resultMap.put(WebConstant.ROWS, queryResult.getContent());
        return resultMap;
    }

    @ResponseBody
    @PostMapping(value = "finishInvoice")
    public Map<String, Object> finishInvoice(String invoiceId, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            afterSalesService.updateInvoice(invoiceId, CommonDealStatus.AGREE);
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "加载出错：" + e.getMessage());
        }
        return resultMap;
    }
}
