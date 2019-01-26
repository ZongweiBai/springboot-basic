package com.baymin.springboot.adminserver.controller;

import com.baymin.springboot.adminserver.constant.WebConstant;
import com.baymin.springboot.common.util.DateUtil;
import com.baymin.springboot.common.util.ExcelUtil;
import com.baymin.springboot.service.IAfterSalesService;
import com.baymin.springboot.service.IOrderService;
import com.baymin.springboot.service.IReportService;
import com.baymin.springboot.store.entity.Evaluate;
import com.baymin.springboot.store.entity.Order;
import com.baymin.springboot.store.enumconstant.CareType;
import com.baymin.springboot.store.enumconstant.OrderStatus;
import com.baymin.springboot.store.payload.report.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Slf4j
@Controller
@RequestMapping("report")
public class ReportController {

    @Autowired
    private IReportService reportService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IAfterSalesService afterSalesService;

    @ResponseBody
    @PostMapping(value = "queryPlatformOrderReport")
    public Map<String, Object> queryPlatformOrderReport(String datemin, String datemax, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        Date maxDate = DateUtil.dayEnd(datemax);
        Date minDate = DateUtil.dayBegin(datemin);

        PlatformOrderReport report = reportService.queryPlatformOrderReport(minDate, maxDate);

        List<PlatformOrderReport> reportList = new ArrayList<>();
        reportList.add(report);
        resultMap.put(WebConstant.TOTAL, 1);
        resultMap.put(WebConstant.ROWS, reportList);
        return resultMap;
    }

    @ResponseBody
    @PostMapping(value = "queryUserInfoReport")
    public Map<String, Object> queryUserInfoReport(String datemin, String datemax, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        Date maxDate = DateUtil.dayEnd(datemax);
        Date minDate = DateUtil.dayBegin(datemin);

        UserInfoReport report = reportService.queryUserInfoReport(minDate, maxDate);

        List<UserInfoReport> reportList = new ArrayList<>();
        reportList.add(report);
        resultMap.put(WebConstant.TOTAL, 1);
        resultMap.put(WebConstant.ROWS, reportList);
        return resultMap;
    }

    @ResponseBody
    @PostMapping(value = "queryServiceStaffReport")
    public Map<String, Object> queryServiceStaffReport(String datemin, String datemax, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        Date maxDate = DateUtil.dayEnd(datemax);
        Date minDate = DateUtil.dayBegin(datemin);

        ServiceStaffReport report = reportService.queryServiceStaffReport(minDate, maxDate);

        List<ServiceStaffReport> reportList = new ArrayList<>();
        reportList.add(report);
        resultMap.put(WebConstant.TOTAL, 1);
        resultMap.put(WebConstant.ROWS, reportList);
        return resultMap;
    }

    @ResponseBody
    @PostMapping(value = "queryOrderReport")
    public Map<String, Object> queryOrderReport(Pageable pageable, CareType careType, OrderStatus status,
                                                String datemin, String datemax, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        Date maxDate = DateUtil.dayEnd(datemax);
        Date minDate = DateUtil.dayBegin(datemin);

        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), new Sort(Sort.Direction.DESC, "orderTime"));
        Page<Order> queryResult = orderService.queryOrderForPage(pageRequest, status, null, careType, maxDate, minDate, null, null, null, null);
        resultMap.put(WebConstant.TOTAL, queryResult.getTotalElements());
        resultMap.put(WebConstant.ROWS, queryResult.getContent());
        return resultMap;
    }

    @ResponseBody
    @PostMapping(value = "queryOrderStatisticReport")
    public Map<String, Object> queryOrderStatisticReport(Pageable pageable, String careTypes, String hospitalAddress,
                                                         String datemin, String datemax, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        Date maxDate = DateUtil.dayEnd(datemax);
        Date minDate = DateUtil.dayBegin(datemin);

        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), new Sort(Sort.Direction.DESC, "orderTime"));
        Page<Order> queryResult = orderService.queryOrderStatisticForPage(pageRequest, careTypes, hospitalAddress, maxDate, minDate);
        resultMap.put(WebConstant.TOTAL, queryResult.getTotalElements());
        resultMap.put(WebConstant.ROWS, queryResult.getContent());
        return resultMap;
    }

    @ResponseBody
    @PostMapping(value = "queryOrderJSReport")
    public Map<String, Object> queryOrderJSReport(Pageable pageable, String careTypes, String hospitalAddress,
                                                  String datemin, String datemax, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        Date maxDate = DateUtil.dayEnd(datemax);
        Date minDate = DateUtil.dayBegin(datemin);

        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), new Sort(Sort.Direction.DESC, "orderTime"));
        List<JSVo> queryResult = orderService.queryOrderJSReport(pageRequest, careTypes, hospitalAddress, maxDate, minDate);
        resultMap.put(WebConstant.TOTAL, queryResult.size());
        resultMap.put(WebConstant.ROWS, queryResult);
        return resultMap;
    }

    @ResponseBody
    @PostMapping(value = "queryQuickOrderReport")
    public Map<String, Object> queryQuickOrderReport(Pageable pageable, String hospitalAddress,
                                                     String datemin, String datemax,
                                                     String paydatemin, String paydatemax, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        Date maxDate = DateUtil.dayEnd(datemax);
        Date minDate = DateUtil.dayBegin(datemin);
        Date paymaxDate = DateUtil.dayEnd(paydatemax);
        Date payminDate = DateUtil.dayBegin(paydatemin);

        List<QuickOrderReport> queryResult = orderService.queryQuickOrderReport(hospitalAddress, maxDate, minDate, paymaxDate, payminDate);
        resultMap.put(WebConstant.TOTAL, queryResult.size());
        resultMap.put(WebConstant.ROWS, queryResult);
        return resultMap;
    }


    @ResponseBody
    @PostMapping(value = "queryHospitalBizReport")
    public Map<String, Object> queryHospitalBizReport(Pageable pageable, String serviceStaffId,
                                                      String datemin, String datemax, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        Date maxDate = DateUtil.dayEnd(datemax);
        Date minDate = DateUtil.dayBegin(datemin);

        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), new Sort(Sort.Direction.DESC, "orderTime"));
        List<HospitalBizVo> queryResult = orderService.queryHospitalBizReport(pageRequest, serviceStaffId, maxDate, minDate);
        resultMap.put(WebConstant.TOTAL, queryResult.size());
        resultMap.put(WebConstant.ROWS, queryResult);
        return resultMap;
    }

    /**
     * 导出评价统计信息
     */
    @RequestMapping(value = "downloadEvaluate", method = RequestMethod.GET)
    public void downloadDriverOrder(String datemin, String datemax, String orderId, Integer grade,
                                    HttpServletRequest request, HttpServletResponse response) throws Exception {
        Date minDate = DateUtil.dayBegin(datemin);
        Date maxDate = DateUtil.dayEnd(datemax);

        List<Evaluate> queryResult = afterSalesService.queryEvaluateList(grade, orderId, maxDate, minDate);

        List<String> titleList = new ArrayList<>();
        titleList.add("订单号");
        titleList.add("产品名称");
        titleList.add("用户账号");
        titleList.add("评价时间");
        titleList.add("评价内容");
        titleList.add("评价星级");

        List<List<String>> lls = new ArrayList<>();
        for (Evaluate data : queryResult) {
            List<String> ls = new ArrayList<>();
            ls.add(data.getOrderId());
            ls.add(Objects.nonNull(data.getCareType()) ? data.getCareType().getName() : "");
            ls.add(Objects.nonNull(data.getUserProfile()) ? data.getUserProfile().getAccount() : "");
            ls.add(DateUtil.formatDate(data.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            ls.add(data.getDescription());
            ls.add(data.getGrade() + "星");
            lls.add(ls);
        }

        String fileName = "评价统计报表";
        String outPath = "/tmp/" + fileName + ".xlsx";
        // 写入文件
        ExcelUtil.writeExcel(titleList, lls, outPath);
        // 下载文件
        ExcelUtil.downLoadFile(outPath, response);
    }

    /**
     * 导出订单统计信息
     */
    @RequestMapping(value = "downloadOrder", method = RequestMethod.GET)
    public void downloadOrder(String datemin, String datemax, CareType careType, OrderStatus status,
                              HttpServletRequest request, HttpServletResponse response) throws Exception {
        Date minDate = DateUtil.dayBegin(datemin);
        Date maxDate = DateUtil.dayEnd(datemax);

        List<Order> queryResult = orderService.queryOrderForList(careType, maxDate, minDate, status);

        List<String> titleList = new ArrayList<>();
        titleList.add("订单号");
        titleList.add("日期");
        titleList.add("服务项目");
        titleList.add("订单状态");
        titleList.add("订单金额");
        titleList.add("服务人员");

        List<List<String>> lls = new ArrayList<>();
        for (Order data : queryResult) {
            List<String> ls = new ArrayList<>();
            ls.add(data.getId());
            ls.add(DateUtil.formatDate(data.getOrderTime(), "yyyy-MM-dd HH:mm:ss"));
            ls.add(Objects.nonNull(data.getCareType()) ? data.getCareType().getName() : "");
            ls.add(Objects.nonNull(data.getStatus()) ? data.getStatus().getName() : "");
            ls.add("￥ " + data.getTotalFee());
            ls.add(Objects.nonNull(data.getServiceStaff()) ? data.getServiceStaff().getUserName() : "-");
            lls.add(ls);
        }

        String fileName = "订单统计报表";
        String outPath = "/tmp/" + fileName + ".xlsx";
        // 写入文件
        ExcelUtil.writeExcel(titleList, lls, outPath);
        // 下载文件
        ExcelUtil.downLoadFile(outPath, response);
    }

    /**
     * 导出微信快捷订单统计信息
     */
    @RequestMapping(value = "downloadQuickRefundOrder", method = RequestMethod.GET)
    public void downloadQuickRefundOrder(String hospitalAddress,
                                    String datemin, String datemax,
                                    String paydatemin, String paydatemax, HttpServletResponse response) throws Exception {
        Date maxDate = DateUtil.dayEnd(datemax);
        Date minDate = DateUtil.dayBegin(datemin);
        Date paymaxDate = DateUtil.dayEnd(paydatemax);
        Date payminDate = DateUtil.dayBegin(paydatemin);

        List<QuickOrderReport> queryResult = orderService.queryQuickOrderReport(hospitalAddress, maxDate, minDate, paymaxDate, payminDate);

        // 声明String数组，并初始化元素（表头名称）
        // 第一行表头字段，合并单元格时字段跨几列就将该字段重复几次
        String[] excelHeader0 = { "医院", "总收入", "退款", "实收",
                "院内", "院内", "院内", "院内",
                "院外", "院外", "院外"};
        // “0,2,0,0” ===> “起始行，截止行，起始列，截止列”
        String[] headnum0 = { "0,1,0,0", "0,1,1,1", "0,1,2,2", "0,1,3,3", "0,0,4,7", "0,0,8,10" };
        //第二行表头字段，其中的空的双引号是为了补全表格边框
        String[] excelHeader1 = { "合计", "一对一", "一对多", "多对一",
                "合计", "一对一", "多对一"};

        List<List<String>> lls = new ArrayList<>();
        for (QuickOrderReport data : queryResult) {
            List<String> ls = new ArrayList<>();
            ls.add(data.getHospitalName());
            ls.add("￥ " + data.getTotalFee());
            ls.add("￥ " + data.getRefundFee());
            ls.add("￥ " + data.getActualIncome());
            ls.add("￥ " + data.getActualIncome());
            ls.add("￥ " + data.getInOneToOne());
            ls.add("￥ " + data.getInOneToMany());
            ls.add("￥ " + data.getInManyToOne());
            ls.add("￥ " + data.getTotalOutFee());
            ls.add("￥ " + data.getOutOneToOne());
            ls.add("￥ " + data.getOutManyToOne());
            lls.add(ls);
        }

        String fileName = "护工费报表";
        String outPath = "D:\\" + fileName + ".xlsx";
        // 写入文件
        ExcelUtil.writeComplicatedHeaderExcel(excelHeader0, headnum0, excelHeader1, lls, outPath);
        // 下载文件
        ExcelUtil.downLoadFile(outPath, response);
    }
}
