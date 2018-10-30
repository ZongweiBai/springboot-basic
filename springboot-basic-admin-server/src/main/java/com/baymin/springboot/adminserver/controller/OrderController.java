package com.baymin.springboot.adminserver.controller;

import com.baymin.springboot.adminserver.constant.WebConstant;
import com.baymin.springboot.common.util.DateUtil;
import com.baymin.springboot.service.IOrderRefundService;
import com.baymin.springboot.service.IOrderService;
import com.baymin.springboot.service.IQuestionService;
import com.baymin.springboot.store.entity.*;
import com.baymin.springboot.store.enumconstant.CareType;
import com.baymin.springboot.store.enumconstant.CommonDealStatus;
import com.baymin.springboot.store.enumconstant.OrderStatus;
import com.baymin.springboot.store.payload.UserOrderVo;
import org.apache.commons.lang3.StringUtils;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IOrderRefundService orderRefundService;

    @Autowired
    private IQuestionService questionService;

    @ResponseBody
    @PostMapping(value = "queryOrderForPage")
    public Map<String, Object> queryOrderForPage(Pageable pageable, OrderStatus status, String orderId, CareType careType,
                                                 String datemin, String datemax, String payStatus, String orderSource,
                                                 String account, String address, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        Date maxDate = DateUtil.dayEnd(datemax);
        Date minDate = DateUtil.dayBegin(datemin);

        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), new Sort(Sort.Direction.DESC, "orderTime"));
        Page<Order> queryResult = orderService.queryOrderForPage(pageRequest, status, orderId, careType, maxDate, minDate, payStatus, orderSource, account, address);
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
    @GetMapping(value = "getOrderBasic")
    public Map<String, Object> getOrderBasic(String orderId, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            Map<String, Object> detailMap = orderService.getOrderBasic(orderId);
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
    @GetMapping(value = "getOrderBasicWithUserInfo")
    public Map<String, Object> getOrderBasicWithUserInfo(String orderId, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            Map<String, Object> detailMap = orderService.getOrderBasicWithUserInfo(orderId);
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
    public Map<String, Object> assignOrderStaff(String orderId, String staffId, String adminId, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            orderService.assignOrderStaff(orderId, staffId, adminId);
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "加载出错：" + e.getMessage());
        }
        return resultMap;
    }

    @ResponseBody
    @PostMapping(value = "staffChange")
    public Map<String, Object> staffChange(OrderStaffChange staffChange, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            staffChange.setDealTime(new Date());
            staffChange.setDealStatus(CommonDealStatus.AGREE);
            staffChange.setChangeDesc("后台替换");
            orderService.staffChangeRequest(staffChange);
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
    public Map<String, Object> offlinePay(PayRecord payRecord, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            orderService.offlinePay(payRecord);
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "加载出错：" + e.getMessage());
        }
        return resultMap;
    }

    @ResponseBody
    @PostMapping(value = "orderRefund")
    public Map<String, Object> orderRefund(OrderRefund orderRefund, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            orderRefund.setDealStatus(CommonDealStatus.AGREE);
            orderRefund.setDealDesc("后台退款");
            orderRefund.setRefundDesc("后台退款");
            orderRefund.setApplyType("SYS");
            orderRefundService.saveOrderRefund(orderRefund);
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "加载出错：" + e.getMessage());
        }
        return resultMap;
    }

    @ResponseBody
    @PostMapping(value = "saveOrder")
    public Map<String, Object> saveOrder(UserOrderVo userOrderVo, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            List<Question> questions = userOrderVo.getQuestions();
            List<String> questionIds = questions.stream().filter(question -> StringUtils.isNotBlank(question.getId()))
                    .map(Question::getId).collect(Collectors.toList());
            List<Question> questionList = questionService.getQuestionByIds(questionIds);

            userOrderVo.setQuestions(questionList);
            userOrderVo.setOrderSource("PC");
            userOrderVo.setOrderType(CareType.HOSPITAL_CARE);
            orderService.saveUserOrder(userOrderVo);
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "加载出错：" + e.getMessage());
        }
        return resultMap;
    }

}
