package com.baymin.springboot.adminserver.controller.pageindex;

import com.baymin.springboot.adminserver.constant.WebConstant;
import com.baymin.springboot.store.entity.Admin;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

@Controller
@RequestMapping
public class IndexController {

    @GetMapping("/")
    public void index(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws IOException {
        HttpSession session = servletRequest.getSession();
        // 从session里取账号
        Admin user = (Admin) session.getAttribute(WebConstant.ADMIN_USER_SESSION);
        if (user == null) {
            user = (Admin) session.getAttribute(WebConstant.SELLER_USER_SESSION);
        }

        // 判断如果没有取到登录信息,就跳转到登陆页面
        if (user == null) {
            servletResponse.sendRedirect(servletRequest.getContextPath() + "/login");
        } else {
            servletResponse.sendRedirect(servletRequest.getContextPath() + "/system/main");
        }
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "welcome";
    }

    @GetMapping("/nologin")
    public String noLogin() {
        return "nologin";
    }

    @GetMapping("/index/sysmenu/add")
    public String addSysMenu(String menuId, Model model) {
        if (Objects.nonNull(menuId)) {
            model.addAttribute("menuId", menuId);
        }
        return "system/sysMenu/sysMenuAdd";
    }

    @GetMapping("/index/sysmenu/manage")
    public String manageSysMenu() {
        return "system/sysMenu/sysMenuManage";
    }

    @GetMapping("/index/sysmenu/add")
    public String addSysRole(String roleId, Model model) {
        if (Objects.nonNull(roleId)) {
            model.addAttribute("roleId", roleId);
        }
        return "system/sysRole/sysRoleAdd";
    }

    @GetMapping("/index/sysrole/manage")
    public String manageSysRole() {
        return "system/sysRole/sysRoleManage";
    }

    @GetMapping("/index/sysuser/add")
    public String addSysUser(String userId, Model model) {
        if (Objects.nonNull(userId)) {
            model.addAttribute("userId", userId);
        }
        return "system/sysUser/sysUserAdd";
    }

    @GetMapping("/index/sysuser/manage")
    public String manageSysUser() {
        return "system/sysUser/sysUserManage";
    }

}
