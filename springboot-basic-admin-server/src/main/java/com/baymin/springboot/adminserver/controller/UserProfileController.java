package com.baymin.springboot.adminserver.controller;

import com.baymin.springboot.adminserver.constant.WebConstant;
import com.baymin.springboot.common.util.DateUtil;
import com.baymin.springboot.service.IUserProfileService;
import com.baymin.springboot.store.entity.Admin;
import com.baymin.springboot.store.entity.UserProfile;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserProfileController {

    @Autowired
    private IUserProfileService userProfileService;

    @ResponseBody
    @PostMapping(value = "queryUserForPage")
    public Map<String, Object> queryUserForPage(Pageable pageable, String nickName, String account, String sex,
                                                String datemin, String datemax, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        Date maxDate = DateUtil.dayEnd(datemax);
        Date minDate = DateUtil.dayBegin(datemin);

        pageable.getSort().and(new Sort(Sort.Direction.DESC, "createTime"));
        Page<UserProfile> queryResult = userProfileService.queryUserForPage(pageable, nickName, account, sex, maxDate, minDate);
        resultMap.put(WebConstant.TOTAL, queryResult.getTotalElements());
        resultMap.put(WebConstant.ROWS, queryResult.getContent());
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "saveUser", method = RequestMethod.POST)
    public Map<String, Object> saveUser(UserProfile userProfile, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        Admin sysUser = (Admin) request.getSession().getAttribute(WebConstant.ADMIN_USER_SESSION);
        try {
            userProfileService.saveUserProfile(userProfile);
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "保存失败");
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "getUserById", method = RequestMethod.GET)
    public Map<String, Object> getUserById(String userId, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            UserProfile userProfile = userProfileService.findById(userId);
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
            resultMap.put(WebConstant.INFO, userProfile);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "加载出错：" + e.getMessage());
        }
        return resultMap;
    }

}
