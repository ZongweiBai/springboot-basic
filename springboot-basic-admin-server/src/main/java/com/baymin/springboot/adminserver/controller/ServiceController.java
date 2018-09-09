package com.baymin.springboot.adminserver.controller;

import com.baymin.springboot.adminserver.constant.WebConstant;
import com.baymin.springboot.service.IBasicServiceService;
import com.baymin.springboot.store.entity.Admin;
import com.baymin.springboot.store.entity.BasicServiceFee;
import com.baymin.springboot.store.enumconstant.BasicServiceType;
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
@RequestMapping("service")
public class ServiceController {

    @Autowired
    private IBasicServiceService basicServiceService;

    @ResponseBody
    @PostMapping(value = "queryServiceForPage")
    public Map<String, Object> queryServiceForPage(Pageable pageable, BasicServiceType serviceType, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();

        pageable.getSort().and(new Sort(Sort.Direction.DESC, "createTime"));
        Page<BasicServiceFee> sysMenuQueryResult = basicServiceService.queryServiceForPage(pageable, serviceType);
        resultMap.put(WebConstant.TOTAL, sysMenuQueryResult.getTotalElements());
        resultMap.put(WebConstant.ROWS, sysMenuQueryResult.getContent());
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "saveService", method = RequestMethod.POST)
    public Map<String, Object> saveService(BasicServiceFee basicServiceFee, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        Admin sysUser = (Admin) request.getSession().getAttribute(WebConstant.ADMIN_USER_SESSION);
        try {
            basicServiceService.saveService(basicServiceFee);
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "保存失败");
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "getServiceById", method = RequestMethod.GET)
    public Map<String, Object> getMenuByRoleId(String serviceId, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            BasicServiceFee basicServiceFee = basicServiceService.getServiceFeeById(serviceId);
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
            resultMap.put(WebConstant.INFO, basicServiceFee);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "加载出错：" + e.getMessage());
        }
        return resultMap;
    }

}
