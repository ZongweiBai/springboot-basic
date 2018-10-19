package com.baymin.springboot.adminserver.controller;

import com.baymin.springboot.adminserver.constant.WebConstant;
import com.baymin.springboot.common.util.DateUtil;
import com.baymin.springboot.service.IAfterSalesService;
import com.baymin.springboot.service.IOrderRefundService;
import com.baymin.springboot.service.IOrderService;
import com.baymin.springboot.store.entity.Evaluate;
import com.baymin.springboot.store.entity.OrderRefund;
import com.baymin.springboot.store.entity.OrderStaffChange;
import com.baymin.springboot.store.enumconstant.CommonDealStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("afterSales")
public class AfterSalesController {

    @Autowired
    private IAfterSalesService afterSalesService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IOrderRefundService orderRefundService;

    @ResponseBody
    @PostMapping(value = "queryStaffchangeForPage")
    public Map<String, Object> queryStaffchangeForPage(Pageable pageable, CommonDealStatus dealStatus, String orderId,
                                                    String datemin, String datemax, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        Date maxDate = DateUtil.dayEnd(datemax);
        Date minDate = DateUtil.dayBegin(datemin);

        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), new Sort(Sort.Direction.DESC, "createTime"));
        Page<OrderStaffChange> queryResult = afterSalesService.queryOrderChangePage(pageRequest, dealStatus, maxDate, minDate, orderId);
        resultMap.put(WebConstant.TOTAL, queryResult.getTotalElements());
        resultMap.put(WebConstant.ROWS, queryResult.getContent());
        return resultMap;
    }

    @ResponseBody
    @PostMapping(value = "dealStaffChange")
    public Map<String, Object> dealStaffChange(OrderStaffChange change, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            afterSalesService.dealStaffChange(change);
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
        } catch (Exception e) {
            log.error("处理换人申请出错", e);
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "加载出错：" + e.getMessage());
        }
        return resultMap;
    }

    @ResponseBody
    @PostMapping(value = "queryRefundForPage")
    public Map<String, Object> queryRefundForPage(Pageable pageable, CommonDealStatus dealStatus, String orderId,
                                                    String datemin, String datemax, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        Date maxDate = DateUtil.dayEnd(datemax);
        Date minDate = DateUtil.dayBegin(datemin);

        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), new Sort(Sort.Direction.DESC, "createTime"));
        Page<OrderRefund> queryResult = afterSalesService.queryRefundPage(pageRequest, dealStatus, maxDate, minDate, orderId);
        resultMap.put(WebConstant.TOTAL, queryResult.getTotalElements());
        resultMap.put(WebConstant.ROWS, queryResult.getContent());
        return resultMap;
    }

    @ResponseBody
    @GetMapping(value = "getRefundInfo")
    public Map<String, Object> getRefundInfo(String refundId, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            Map<String, Object> detailMap = afterSalesService.getRefundInfo(refundId);
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
            resultMap.put(WebConstant.INFO, detailMap);
        } catch (Exception e) {
            log.error("获取退款申请出错", e);
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "加载出错：" + e.getMessage());
        }
        return resultMap;
    }

    @ResponseBody
    @PostMapping(value = "dealOrderRefund")
    public Map<String, Object> dealOrderRefund(OrderRefund refund, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            orderRefundService.dealOrderRefund(refund);
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
        } catch (Exception e) {
            log.error("处理退款申请出错", e);
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "加载出错：" + e.getMessage());
        }
        return resultMap;
    }

    @ResponseBody
    @GetMapping(value = "viewChangeDetail")
    public Map<String, Object> viewChangeDetail(String changeId, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            Map<String, Object> detailMap = afterSalesService.getChangeDetail(changeId);
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
            resultMap.put(WebConstant.INFO, detailMap);
        } catch (Exception e) {
            log.error("查看换人申请出错", e);
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "加载出错：" + e.getMessage());
        }
        return resultMap;
    }

    @ResponseBody
    @PostMapping(value = "queryEvaluateForPage")
    public Map<String, Object> queryEvaluatePage(Pageable pageable, Integer grade, String orderId,
                                                    String datemin, String datemax, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        Date maxDate = DateUtil.dayEnd(datemax);
        Date minDate = DateUtil.dayBegin(datemin);

        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                new Sort(Sort.Direction.DESC, "createTime"));
        Page<Evaluate> queryResult = afterSalesService.queryEvaluatePage(pageRequest, grade, orderId, maxDate, minDate);
        resultMap.put(WebConstant.TOTAL, queryResult.getTotalElements());
        resultMap.put(WebConstant.ROWS, queryResult.getContent());
        return resultMap;
    }

}
