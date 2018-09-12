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

    /**===========================菜单入口================================*/
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

    /**===========================角色入口================================*/
    @GetMapping("/index/sysrole/add")
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

    /**===========================系统用户入口================================*/
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

    /**===========================机构管理入口================================*/
    @GetMapping("/index/org/add")
    public String addSysOrg(String orgId, Model model) {
        if (Objects.nonNull(orgId)) {
            model.addAttribute("orgId", orgId);
        }
        return "system/org/orgAdd";
    }

    @GetMapping("/index/org/manage")
    public String manageOrg() {
        return "system/org/orgManage";
    }

    /**===========================收费项目库入口================================*/
    @GetMapping("/index/item/basic/manage")
    public String manageBasicService() {
        return "service/basic/serviceManage";
    }

    @GetMapping("/index/item/basic/add")
    public String addBasicService(String itemId, Model model) {
        if (Objects.nonNull(itemId)) {
            model.addAttribute("itemId", itemId);
        }
        return "service/basic/serviceAdd";
    }

    /**===========================普通用户入口================================*/
    @GetMapping("/index/userprofile/manage")
    public String manageUserProfile() {
        return "user/user/userManage";
    }

    @GetMapping("/index/userprofile/add")
    public String addUser(String userId, Model model) {
        if (Objects.nonNull(userId)) {
            model.addAttribute("userId", userId);
        }
        return "user/user/addUser";
    }

    @GetMapping("/index/userprofile/detail")
    public String userDetail(String userId, Model model) {
        if (Objects.nonNull(userId)) {
            model.addAttribute("userId", userId);
        }
        return "user/user/viewUserDetail";
    }

    /**===========================护工/护士管理入口================================*/
    @GetMapping("/index/staff/manage")
    public String manageStaff() {
        return "user/staff/staffManage";
    }

    @GetMapping("/index/staff/add")
    public String addStaff(String staffId, Model model) {
        if (Objects.nonNull(staffId)) {
            model.addAttribute("staffId", staffId);
        }
        return "user/staff/addStaff";
    }

    @GetMapping("/index/staff/detail")
    public String staffDetail(String staffId, Model model) {
        if (Objects.nonNull(staffId)) {
            model.addAttribute("staffId", staffId);
        }
        return "user/staff/viewStaffDetail";
    }


}
