package com.baymin.springboot.adminserver.controller;

import com.baymin.springboot.adminserver.constant.WebConstant;
import com.baymin.springboot.common.util.DateUtil;
import com.baymin.springboot.service.IOrderService;
import com.baymin.springboot.store.entity.Order;
import com.baymin.springboot.store.entity.UserProfile;
import com.baymin.springboot.store.enumconstant.CareType;
import com.baymin.springboot.store.enumconstant.OrderStatus;
import com.baymin.springboot.store.enumconstant.PayWay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @ResponseBody
    @PostMapping(value = "queryOrderForPage")
    public Map<String, Object> queryOrderForPage(Pageable pageable, OrderStatus status, String orderId, CareType careType,
                                                 String datemin, String datemax, String payStatus, String orderSource,
                                                 HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        Date maxDate = DateUtil.dayEnd(datemax);
        Date minDate = DateUtil.dayBegin(datemin);

        pageable.getSort().and(new Sort(Sort.Direction.DESC, "orderTime"));
        Page<Order> queryResult = orderService.queryOrderForPage(pageable, status, orderId, careType, maxDate, minDate, payStatus, orderSource);
        resultMap.put(WebConstant.TOTAL, queryResult.getTotalElements());
        resultMap.put(WebConstant.ROWS, queryResult.getContent());
        return resultMap;
    }

    @ResponseBody
    @GetMapping(value = "viewOrderDetail")
    public Map<String, Object> viewOrderDetail(String orderId, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            Map<String, Object> detailMap = orderService.getOrderDetail(orderId);
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
            resultMap.put(WebConstant.INFO, detailMap);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "加载出错：" + e.getMessage());
        }
        return resultMap;
    }

    @ResponseBody
    @PostMapping(value = "assignOrderStaff")
    public Map<String, Object> assignOrderStaff(String orderId, String staffId, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            orderService.assignOrderStaff(orderId, staffId);
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "加载出错：" + e.getMessage());
        }
        return resultMap;
    }

    @ResponseBody
    @PostMapping(value = "offlinePay")
    public Map<String, Object> offlinePay(String orderId, PayWay payWay, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            orderService.offlinePay(orderId, payWay);
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "加载出错：" + e.getMessage());
        }
        return resultMap;
    }

}
