package com.baymin.springboot.adminserver.controller;

import com.baymin.springboot.adminserver.constant.WebConstant;
import com.baymin.springboot.common.util.DateUtil;
import com.baymin.springboot.service.IReportService;
import com.baymin.springboot.store.payload.report.PlatformOrderReport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@Controller
@RequestMapping("report")
public class ReportController {

    @Autowired
    private IReportService reportService;

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

}
