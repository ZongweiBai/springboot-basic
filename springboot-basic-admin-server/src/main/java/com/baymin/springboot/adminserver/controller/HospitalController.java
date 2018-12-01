package com.baymin.springboot.adminserver.controller;

import com.baymin.springboot.adminserver.constant.WebConstant;
import com.baymin.springboot.service.IHospitalService;
import com.baymin.springboot.store.entity.Admin;
import com.baymin.springboot.store.entity.Hospital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.Collator;
import java.util.*;

@Controller
@RequestMapping("hospital")
public class HospitalController {

    @Autowired
    private IHospitalService hospitalService;

    @ResponseBody
    @PostMapping(value = "queryHospitalForPage")
    public Map<String, Object> queryQuestionForPage(String hospitalName,
                                                    Pageable pageable, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();

        Page<Hospital> queryResult = hospitalService.queryQuestionForPage(hospitalName, pageable);

        resultMap.put(WebConstant.TOTAL, queryResult.getTotalElements());
        resultMap.put(WebConstant.ROWS, queryResult.getContent());
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "saveHospital", method = RequestMethod.POST)
    public Map<String, Object> saveHospital(Hospital hospital, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        Admin sysUser = (Admin) request.getSession().getAttribute(WebConstant.ADMIN_USER_SESSION);
        try {
            hospitalService.saveHospital(hospital);
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "保存失败");
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "deleteHospital", method = RequestMethod.POST)
    public Map<String, Object> deleteHospital(String hospitalId, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        Admin sysUser = (Admin) request.getSession().getAttribute(WebConstant.ADMIN_USER_SESSION);
        try {
            hospitalService.deleteHospital(hospitalId);
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "删除失败");
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "getHospitalById", method = RequestMethod.GET)
    public Map<String, Object> getHospitalById(String hospitalId, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            Hospital hospital = hospitalService.getHospitalById(hospitalId);
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
            resultMap.put(WebConstant.INFO, hospital);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "加载出错：" + e.getMessage());
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "getAllHospital", method = RequestMethod.GET)
    public Map<String, Object> getAllHospital(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            List<Hospital> hospitalList = hospitalService.getAllHospital();
            Collections.sort(hospitalList, (Hospital o1, Hospital o2) -> Collator.getInstance(Locale.CHINESE).compare(o1.getHospitalName(), o2.getHospitalName()));
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
            resultMap.put(WebConstant.ROWS, hospitalList);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "加载出错：" + e.getMessage());
        }
        return resultMap;
    }

}
