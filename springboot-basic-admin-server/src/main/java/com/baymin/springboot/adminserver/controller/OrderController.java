package com.baymin.springboot.adminserver.controller;

import com.baymin.springboot.adminserver.constant.WebConstant;
import com.baymin.springboot.common.util.DateUtil;
import com.baymin.springboot.service.IHospitalService;
import com.baymin.springboot.service.IOrderRefundService;
import com.baymin.springboot.service.IOrderService;
import com.baymin.springboot.service.IQuestionService;
import com.baymin.springboot.store.entity.*;
import com.baymin.springboot.store.enumconstant.CareType;
import com.baymin.springboot.store.enumconstant.CommonDealStatus;
import com.baymin.springboot.store.enumconstant.OrderStatus;
import com.baymin.springboot.store.payload.BasicItemRequestVo;
import com.baymin.springboot.store.payload.EditOrderRequestVo;
import com.baymin.springboot.store.payload.UserOrderVo;
import org.apache.commons.collections4.CollectionUtils;
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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;
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

    @Autowired
    private IHospitalService hospitalService;

    @ResponseBody
    @PostMapping(value = "queryOrderForPage")
    public Map<String, Object> queryOrderForPage(Pageable pageable, OrderStatus status, String orderId, CareType careType,
                                                 String datemin, String datemax, String payStatus, String orderSource,
                                                 String account, String address, String hospitalName, HttpServletRequest request) {

        Admin sysUser = (Admin) request.getSession().getAttribute(WebConstant.ADMIN_USER_SESSION);

        Map<String, Object> resultMap = new HashMap<>();
        Date maxDate = DateUtil.dayEnd(datemax);
        Date minDate = DateUtil.dayBegin(datemin);

        Set<String> hospitalNameSet = new HashSet<>();
        if (StringUtils.isBlank(hospitalName)) {
            List<Hospital> hospitalList = hospitalService.getUserHospital(sysUser.getId());
            if (CollectionUtils.isNotEmpty(hospitalList)) {
                for (Hospital hospital : hospitalList) {
                    hospitalNameSet.add(hospital.getHospitalName());
                }
            }
        } else {
            hospitalNameSet.add(hospitalName);
        }

        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), new Sort(Sort.Direction.DESC, "orderTime"));
        Page<Order> queryResult = orderService.queryOrderForPage(pageRequest, status, orderId, careType, maxDate, minDate, payStatus, orderSource, account, address, hospitalNameSet);
        resultMap.put(WebConstant.TOTAL, queryResult.getTotalElements());
        resultMap.put(WebConstant.ROWS, queryResult.getContent());
        return resultMap;
    }

    @ResponseBody
    @PostMapping(value = "queryHospitalOrder")
    public Map<String, Object> queryHospitalOrder(String orderTime, String staffId) {
        Map<String, Object> resultMap = new HashMap<>();

        List<Order> queryResult = orderService.queryHospitalOrder(orderTime, staffId);
        resultMap.put(WebConstant.TOTAL, queryResult.size());
        resultMap.put(WebConstant.ROWS, queryResult);
        return resultMap;
    }

    @ResponseBody
    @PostMapping(value = "queryQuickOrder")
    public Map<String, Object> queryQuickOrder(String datemin, String datemax, String serviceScope,
                                               String paydatemin, String paydatemax, String hospitalName, String department) {
        Map<String, Object> resultMap = new HashMap<>();
        Date maxDate = DateUtil.dayEnd(datemax);
        Date minDate = DateUtil.dayBegin(datemin);
        Date paymaxDate = DateUtil.dayEnd(paydatemax);
        Date payminDate = DateUtil.dayBegin(paydatemin);

        List<Order> queryResult = orderService.queryQuickOrder(minDate, maxDate, hospitalName, paymaxDate, payminDate, "NORMAL_WITH_PAID", serviceScope, department);
        resultMap.put(WebConstant.TOTAL, queryResult.size());
        resultMap.put(WebConstant.ROWS, queryResult);
        return resultMap;
    }

    @ResponseBody
    @PostMapping(value = "queryQuickRefundOrder")
    public Map<String, Object> queryQuickRefundOrder(String datemin, String datemax,
                                               String paydatemin, String paydatemax, String hospitalName, String department) {
        Map<String, Object> resultMap = new HashMap<>();
        Date maxDate = DateUtil.dayEnd(datemax);
        Date minDate = DateUtil.dayBegin(datemin);
        Date paymaxDate = DateUtil.dayEnd(paydatemax);
        Date payminDate = DateUtil.dayBegin(paydatemin);

        List<Order> queryResult = orderService.queryQuickOrder(minDate, maxDate, hospitalName, paymaxDate, payminDate, "REFUND", null, department);
        resultMap.put(WebConstant.TOTAL, queryResult.size());
        resultMap.put(WebConstant.ROWS, queryResult);
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
    public Map<String, Object> getOrderBasic(String orderId, String type, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            Map<String, Object> detailMap = orderService.getOrderBasic(orderId, type);
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
    public Map<String, Object> assignOrderStaff(String orderId, String staffId, String adminId, String nurseId, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            orderService.assignOrderStaff(orderId, staffId, adminId, nurseId);
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
    public Map<String, Object> offlinePay(PayRecord payRecord, HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Admin sysUser = (Admin) session.getAttribute(WebConstant.ADMIN_USER_SESSION);
        if (sysUser == null) {
            sysUser = (Admin) session.getAttribute(WebConstant.SELLER_USER_SESSION);
        }

        Map<String, Object> resultMap = new HashMap<>();
        if (sysUser == null) {
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "登录已失效，请重新登录");
            return resultMap;
        }

        try {
            orderService.offlinePay(payRecord, sysUser);
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
            if (CollectionUtils.isEmpty(questions)) {
                /*resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
                resultMap.put(WebConstant.MESSAGE, "请选择用户基本情况");
                return resultMap;*/
                questions = new ArrayList<>();
            }
            List<String> questionIds = questions.stream().filter(question -> StringUtils.isNotBlank(question.getId()))
                    .map(Question::getId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(questionIds)) {
                List<Question> questionList = questionService.getQuestionByIds(questionIds);
                userOrderVo.setQuestions(questionList);
            }

            if (CollectionUtils.isNotEmpty(userOrderVo.getBasicItems())) {
                List<BasicItemRequestVo> basicItemList = userOrderVo.getBasicItems().stream().filter(itemRequestVo -> Objects.nonNull(itemRequestVo.getId())).collect(Collectors.toList());
                userOrderVo.setBasicItems(basicItemList);
            } else {
                userOrderVo.setBasicItems(null);
            }

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

    @ResponseBody
    @PostMapping(value = "editOrder")
    public Map<String, Object> editOrder(EditOrderRequestVo requestVo, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            orderService.editUserOrder(requestVo);
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "加载出错：" + e.getMessage());
        }
        return resultMap;
    }

}
