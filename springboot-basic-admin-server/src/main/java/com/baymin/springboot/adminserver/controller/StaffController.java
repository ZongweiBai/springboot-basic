package com.baymin.springboot.adminserver.controller;

import com.baymin.springboot.adminserver.constant.WebConstant;
import com.baymin.springboot.service.IStaffService;
import com.baymin.springboot.store.entity.Admin;
import com.baymin.springboot.store.entity.ServiceStaff;
import com.baymin.springboot.store.enumconstant.CommonStatus;
import com.baymin.springboot.store.enumconstant.ServiceStaffType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("staff")
public class StaffController {

    @Autowired
    private IStaffService staffService;

    @ResponseBody
    @PostMapping(value = "queryStaffForPage")
    public Map<String, Object> queryStaffForPage(Pageable pageable, String userName, String mobile, String sex,
                                                HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();

        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), new Sort(Sort.Direction.DESC, "createTime"));
        Page<ServiceStaff> queryResult = staffService.queryStaffForPage(pageRequest, userName, mobile, sex);
        resultMap.put(WebConstant.TOTAL, queryResult.getTotalElements());
        resultMap.put(WebConstant.ROWS, queryResult.getContent());
        return resultMap;
    }

    @ResponseBody
    @GetMapping(value = "queryStaffByType")
    public Map<String, Object> queryStaffByType(String serviceStaffType,
                                                HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();

        List<ServiceStaff> staffList = staffService.queryStaffByType(ServiceStaffType.valueOf(serviceStaffType));
        resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
        resultMap.put(WebConstant.ROWS, staffList);
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "saveStaff", method = RequestMethod.POST)
    public Map<String, Object> saveStaff(ServiceStaff serviceStaff, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        Admin sysUser = (Admin) request.getSession().getAttribute(WebConstant.ADMIN_USER_SESSION);
        try {
            staffService.saveStaff(serviceStaff);
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "保存失败");
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "getStaffById", method = RequestMethod.GET)
    public Map<String, Object> getStaffById(String staffId, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            ServiceStaff staff = staffService.findById(staffId);
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
            resultMap.put(WebConstant.INFO, staff);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "加载出错：" + e.getMessage());
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "viewStaffDetail", method = RequestMethod.GET)
    public Map<String, Object> viewStaffDetail(String staffId, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            Map<String, Object> detailMap = staffService.getStaffDetail(staffId);
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
    @RequestMapping(value = "deleteStaff", method = RequestMethod.GET)
    public Map<String, Object> deleteStaff(String staffId, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            staffService.updateStaffStatus(staffId, CommonStatus.DELETE);
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "加载出错：" + e.getMessage());
        }
        return resultMap;
    }

}
