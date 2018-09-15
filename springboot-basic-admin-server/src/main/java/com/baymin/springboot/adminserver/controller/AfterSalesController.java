package com.baymin.springboot.adminserver.controller;

import com.baymin.springboot.adminserver.constant.WebConstant;
import com.baymin.springboot.common.util.DateUtil;
import com.baymin.springboot.service.IAfterSalesService;
import com.baymin.springboot.store.entity.Evaluate;
import com.baymin.springboot.store.entity.OrderRefund;
import com.baymin.springboot.store.entity.OrderStaffChange;
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
@RequestMapping("afterSales")
public class AfterSalesController {

    @Autowired
    private IAfterSalesService afterSalesService;

    @ResponseBody
    @PostMapping(value = "queryStaffchangeForPage")
    public Map<String, Object> queryStaffchangeForPage(Pageable pageable, CommonDealStatus dealStatus, String orderId,
                                                    String datemin, String datemax, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        Date maxDate = DateUtil.dayEnd(datemax);
        Date minDate = DateUtil.dayBegin(datemin);

        pageable.getSort().and(new Sort(Sort.Direction.DESC, "createTime"));
        Page<OrderStaffChange> queryResult = afterSalesService.queryOrderChangePage(pageable, dealStatus, maxDate, minDate, orderId);
        resultMap.put(WebConstant.TOTAL, queryResult.getTotalElements());
        resultMap.put(WebConstant.ROWS, queryResult.getContent());
        return resultMap;
    }

    @ResponseBody
    @PostMapping(value = "queryRefundForPage")
    public Map<String, Object> queryRefundForPage(Pageable pageable, CommonDealStatus dealStatus, String orderId,
                                                    String datemin, String datemax, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        Date maxDate = DateUtil.dayEnd(datemax);
        Date minDate = DateUtil.dayBegin(datemin);

        pageable.getSort().and(new Sort(Sort.Direction.DESC, "createTime"));
        Page<OrderRefund> queryResult = afterSalesService.queryRefundPage(pageable, dealStatus, maxDate, minDate, orderId);
        resultMap.put(WebConstant.TOTAL, queryResult.getTotalElements());
        resultMap.put(WebConstant.ROWS, queryResult.getContent());
        return resultMap;
    }

    @ResponseBody
    @PostMapping(value = "queryEvaluateForPage")
    public Map<String, Object> queryEvaluatePage(Pageable pageable, Integer grade, String orderId,
                                                    String datemin, String datemax, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        Date maxDate = DateUtil.dayEnd(datemax);
        Date minDate = DateUtil.dayBegin(datemin);

        pageable.getSort().and(new Sort(Sort.Direction.DESC, "createTime"));
        Page<Evaluate> queryResult = afterSalesService.queryEvaluatePage(pageable, grade, orderId, maxDate, minDate);
        resultMap.put(WebConstant.TOTAL, queryResult.getTotalElements());
        resultMap.put(WebConstant.ROWS, queryResult.getContent());
        return resultMap;
    }

}
