package com.baymin.springboot.adminserver.controller;

import com.baymin.springboot.adminserver.constant.WebConstant;
import com.baymin.springboot.service.ICarePlanService;
import com.baymin.springboot.service.ISysManageService;
import com.baymin.springboot.store.entity.Admin;
import com.baymin.springboot.store.entity.CarePlan;
import com.baymin.springboot.store.entity.SysDict;
import com.baymin.springboot.store.enumconstant.CommonStatus;
import org.apache.commons.collections4.CollectionUtils;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequestMapping("careplan")
public class CarePlanController {

    @Autowired
    private ICarePlanService carePlanService;

    @Autowired
    private ISysManageService sysManageService;

    @ResponseBody
    @PostMapping(value = "queryCarePlanForPage")
    public Map<String, Object> queryCarePlanForPage(String typeId, String caseId, String planDesc,
                                                    Pageable pageable, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();

        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), new Sort(Sort.Direction.DESC, "createTime"));
        Page<CarePlan> queryResult = carePlanService.queryCarePlanForPage(typeId, caseId, planDesc, pageRequest);
        List<CarePlan> carePlanList = queryResult.getContent();
        if (CollectionUtils.isNotEmpty(carePlanList)) {
            List<SysDict> typeList = sysManageService.getSysDictByDictName("CARE_PLAN_TYPE");
            List<SysDict> caseList = sysManageService.getSysDictByDictName("CARE_PLAN_CASE");
            Map<String, SysDict> typeMap = new HashMap<>();
            Map<String, SysDict> caseMap = new HashMap<>();

            if (CollectionUtils.isNotEmpty(typeList)) {
                typeMap = typeList.stream().collect(Collectors.toMap(SysDict::getId, Function.identity()));
            }
            if (CollectionUtils.isNotEmpty(caseList)) {
                caseMap = caseList.stream().collect(Collectors.toMap(SysDict::getId, Function.identity()));
            }

            Map<String, SysDict> finalTypeMap = typeMap;
            Map<String, SysDict> finalCaseMap = caseMap;
            carePlanList.stream().forEach(carePlan -> {
                carePlan.setTypeDict(finalTypeMap.get(carePlan.getTypeId()));
                carePlan.setCaseDict(finalCaseMap.get(carePlan.getCaseId()));
            });
        }

        resultMap.put(WebConstant.TOTAL, queryResult.getTotalElements());
        resultMap.put(WebConstant.ROWS, carePlanList);
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "saveCarePlan", method = RequestMethod.POST)
    public Map<String, Object> saveCarePlan(CarePlan carePlan, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        Admin sysUser = (Admin) request.getSession().getAttribute(WebConstant.ADMIN_USER_SESSION);
        try {
            carePlanService.saveCarePlan(carePlan);
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "保存失败");
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "deleteCarePlan", method = RequestMethod.POST)
    public Map<String, Object> deleteCarePlan(String planId, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        Admin sysUser = (Admin) request.getSession().getAttribute(WebConstant.ADMIN_USER_SESSION);
        try {
            carePlanService.changeCarePlanStatus(planId, CommonStatus.DELETE);
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "保存失败");
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "getCarePlanById", method = RequestMethod.GET)
    public Map<String, Object> getCarePlanById(String planId, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            CarePlan carePlan = carePlanService.getCarePlanById(planId);
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
            resultMap.put(WebConstant.INFO, carePlan);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "加载出错：" + e.getMessage());
        }
        return resultMap;
    }

}
