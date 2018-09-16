package com.baymin.springboot.adminserver.controller;

import com.baymin.springboot.adminserver.constant.WebConstant;
import com.baymin.springboot.service.IBasicItemService;
import com.baymin.springboot.store.entity.Admin;
import com.baymin.springboot.store.entity.BasicItem;
import com.baymin.springboot.store.entity.ServiceType;
import com.baymin.springboot.store.enumconstant.BasicItemType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("item")
public class BasicItemController {

    @Autowired
    private IBasicItemService basicItemService;

    @ResponseBody
    @PostMapping(value = "queryItemForPage")
    public Map<String, Object> queryItemForPage(Pageable pageable, BasicItemType basicItemType, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();

        pageable.getSort().and(new Sort(Sort.Direction.DESC, "createTime"));
        Page<BasicItem> queryResult = basicItemService.queryItemForPage(pageable, basicItemType);
        resultMap.put(WebConstant.TOTAL, queryResult.getTotalElements());
        resultMap.put(WebConstant.ROWS, queryResult.getContent());
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "saveItem", method = RequestMethod.POST)
    public Map<String, Object> saveItem(BasicItem basicItem, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        Admin sysUser = (Admin) request.getSession().getAttribute(WebConstant.ADMIN_USER_SESSION);
        try {
            basicItemService.saveItem(basicItem);
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "保存失败");
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "getItemById", method = RequestMethod.GET)
    public Map<String, Object> getItemById(String itemId, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            BasicItem basicItem = basicItemService.getItemById(itemId);
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
            resultMap.put(WebConstant.INFO, basicItem);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "加载出错：" + e.getMessage());
        }
        return resultMap;
    }

    @ResponseBody
    @PostMapping(value = "queryServiceTypeForPage")
    public Map<String, Object> queryServiceTypeForPage(Pageable pageable, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();

        pageable.getSort().and(new Sort(Sort.Direction.DESC, "createTime"));
        Page<ServiceType> queryResult = basicItemService.queryServiceTypeForPage(pageable);
        resultMap.put(WebConstant.TOTAL, queryResult.getTotalElements());
        resultMap.put(WebConstant.ROWS, queryResult.getContent());
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "saveServiceType", method = RequestMethod.POST)
    public Map<String, Object> saveServiceType(ServiceType serviceType, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        Admin sysUser = (Admin) request.getSession().getAttribute(WebConstant.ADMIN_USER_SESSION);
        try {
            basicItemService.saveServiceType(serviceType);
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "保存失败");
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "getServiceTypeById", method = RequestMethod.GET)
    public Map<String, Object> getServiceTypeById(String typeId, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            ServiceType serviceType = basicItemService.getServiceTypeById(typeId);
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
            resultMap.put(WebConstant.INFO, serviceType);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "加载出错：" + e.getMessage());
        }
        return resultMap;
    }

}
