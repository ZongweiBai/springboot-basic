package com.baymin.springboot.adminserver.controller;

import com.baymin.springboot.adminserver.constant.WebConstant;
import com.baymin.springboot.service.IAdminService;
import com.baymin.springboot.service.IMenuService;
import com.baymin.springboot.store.entity.Admin;
import com.baymin.springboot.store.entity.RelateRoleMenu;
import com.baymin.springboot.store.entity.SysMenu;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("system")
public class SystemController {

    @Autowired
    private IAdminService adminService;

    @Autowired
    private IMenuService menuService;

    @PostMapping("/login")
    @ResponseBody
    public Map<String, Object> login(String userName, String passWord,
                                     HttpServletRequest request) {
        Map<String, Object> reMap = new HashMap<>();
        Admin admin = adminService.getAdminByAccount(userName);
        if (admin != null) {
            if (admin.getPassword().equals(passWord)) {
                request.getSession().setAttribute(WebConstant.ADMIN_USER_SESSION, admin);
                reMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
            } else {
                reMap.put(WebConstant.RESULT, WebConstant.FAULT);
                reMap.put(WebConstant.MESSAGE, "用户名或密码错误");
            }
        } else {
            reMap.put(WebConstant.RESULT, WebConstant.FAULT);
            reMap.put(WebConstant.MESSAGE, "账号不存在");
        }
        return reMap;
    }

    @GetMapping("/main")
    public String main(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Admin sysUser = (Admin) session.getAttribute(WebConstant.ADMIN_USER_SESSION);
        if (sysUser == null) {
            sysUser = (Admin) session.getAttribute(WebConstant.SELLER_USER_SESSION);
        }

        List<SysMenu> sysMenuList = new ArrayList<>();

        if (sysUser == null) {
            return "login";
        }

        if (sysUser.getGrade() == 1) {
            sysMenuList.addAll(menuService.getAllSysMenu());
        } else if (sysUser.getGrade() == 2) {
            if (sysUser.getRoleId() != null) {
                List<RelateRoleMenu> relateRoleMenus = menuService.getRelateRoleMenuByRoleId(sysUser.getRoleId());
                if (CollectionUtils.isNotEmpty(relateRoleMenus)) {
                    List<String> menuIdList = relateRoleMenus.stream().map(RelateRoleMenu::getId).collect(Collectors.toList());
                    sysMenuList.addAll(menuService.getSysMenuByIds(menuIdList));
                }
            }
        }

        List<SysMenu> mainSysMenuList = formatSysMenuList(sysMenuList);

        model.addAttribute("sysMenuList", mainSysMenuList);
        return "main";
    }

    private List<SysMenu> formatSysMenuList(List<SysMenu> sysMenuList) {
        List<SysMenu> mainSysMenuList = new ArrayList<>();
        Set<String> mainMenuIdSet = new HashSet<>();
        if (sysMenuList != null && !sysMenuList.isEmpty()) {
            for (SysMenu sysMenu : sysMenuList) {
                if (sysMenu.getLevel() == 1) {
                    mainMenuIdSet.add(sysMenu.getId());
                } else {
                    mainMenuIdSet.add(sysMenu.getParentId());
                }
            }

            mainSysMenuList = menuService.getSysMenuByIds(new ArrayList<String>(mainMenuIdSet));

            Map<String, List<SysMenu>> subMenuMap = new HashMap<>();
            for (SysMenu sysMenu : sysMenuList) {
                List<SysMenu> subMenuList = new ArrayList<>();
                if (sysMenu.getLevel() == 2) {
                    if (subMenuMap.containsKey(sysMenu.getParentId())) {
                        subMenuList = subMenuMap.get(sysMenu.getParentId());
                    }
                    subMenuList.add(sysMenu);
                    subMenuMap.put(sysMenu.getParentId(), subMenuList);
                }
            }

            if (mainSysMenuList != null && !mainSysMenuList.isEmpty()) {
                for (SysMenu sysMenu : mainSysMenuList) {
                    sysMenu.setSubMenuList(subMenuMap.get(sysMenu.getId()));
                }
            }
        }
        return mainSysMenuList;
    }

    @ResponseBody
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public Map<String, Object> LoginOut(HttpServletRequest request) {
        Map<String, Object> reMap = new HashMap<>();
        try {
            request.getSession().removeAttribute(WebConstant.ADMIN_USER_SESSION);
            reMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            reMap.put(WebConstant.RESULT, WebConstant.FAULT);
            reMap.put(WebConstant.MESSAGE, "退出登录失败");
        }
        return reMap;
    }

}
