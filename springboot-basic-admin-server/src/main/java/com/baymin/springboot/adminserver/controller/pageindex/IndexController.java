package com.baymin.springboot.adminserver.controller.pageindex;

import com.baymin.springboot.adminserver.constant.WebConstant;
import com.baymin.springboot.store.entity.Admin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Objects;

@Controller
@RequestMapping
@Slf4j
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

    /**
     * ===========================菜单入口================================
     */
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

    /**
     * ===========================角色入口================================
     */
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

    /**
     * ===========================系统用户入口================================
     */
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

    /**
     * ===========================机构管理入口================================
     */
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

    /**
     * ===========================收费项目库入口================================
     */
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

    @GetMapping("/index/type/basic/manage")
    public String manageServiceType() {
        return "service/type/serviceManage";
    }

    @GetMapping("/index/type/basic/add")
    public String addServiceType(String typeId, Model model) {
        if (Objects.nonNull(typeId)) {
            model.addAttribute("typeId", typeId);
        }
        return "service/type/serviceAdd";
    }

    @GetMapping("/index/product/basic/manage")
    public String manageServiceProduct() {
        return "service/product/serviceManage";
    }

    @GetMapping("/index/product/basic/add")
    public String addServiceProduct(String productId, Model model) {
        if (Objects.nonNull(productId)) {
            model.addAttribute("productId", productId);
        }
        return "service/product/serviceAdd";
    }

    /**
     * ===========================普通用户入口================================
     */
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

    /**
     * ===========================护工/护士管理入口================================
     */
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

    /**
     * ===========================订单管理入口================================
     */
    @GetMapping("/index/order/manage")
    public String manageOrder() {
        return "order/orderManage";
    }

    @GetMapping("/index/order/assign")
    public String orderAssign(String orderId, Model model) {
        if (Objects.nonNull(orderId)) {
            model.addAttribute("orderId", orderId);
        }
        return "order/orderAssign";
    }

    @GetMapping("/index/order/refund")
    public String orderRefund(String orderId, Model model) {
        if (Objects.nonNull(orderId)) {
            model.addAttribute("orderId", orderId);
        }
        return "order/orderRefund";
    }

    @GetMapping("/index/order/staffchange")
    public String orderStaffChange(String orderId, Model model) {
        if (Objects.nonNull(orderId)) {
            model.addAttribute("orderId", orderId);
        }
        return "order/staffChange";
    }

    @GetMapping("/index/order/offlinePay")
    public String orderOfflinePay(String orderId, Model model) {
        if (Objects.nonNull(orderId)) {
            model.addAttribute("orderId", orderId);
        }
        return "order/offlinePay";
    }

    @GetMapping("/index/order/detail")
    public String orderDetail(String orderId, Model model) {
        if (Objects.nonNull(orderId)) {
            model.addAttribute("orderId", orderId);
        }
        return "order/viewOrderDetail";
    }

    @GetMapping("/index/order/add")
    public String orderAdd(String orderId, Model model) {
        return "order/orderAdd";
    }

    @GetMapping("/index/order/editOrder")
    public String orderEdit(String orderId, Model model) {
        if (Objects.nonNull(orderId)) {
            model.addAttribute("orderId", orderId);
        }
        return "order/orderEdit";
    }

    /**
     * ===========================售后管理入口================================
     */
    @GetMapping("/index/aftersales/refund/manage")
    public String manageRefund() {
        return "aftersales/refund/refundManage";
    }

    @GetMapping("/index/aftersales/refund/detail")
    public String refundDetail(String refundId, Model model) {
        if (Objects.nonNull(refundId)) {
            model.addAttribute("refundId", refundId);
        }
        return "aftersales/refund/viewRefundDetail";
    }

    @GetMapping("/index/aftersales/staffchange/manage")
    public String manageStaffChange() {
        return "aftersales/staffchange/staffchangeManage";
    }

    @GetMapping("/index/aftersales/staffchange/detail")
    public String staffchangeDetail(String changeId, Model model) {
        if (Objects.nonNull(changeId)) {
            model.addAttribute("changeId", changeId);
        }
        return "aftersales/staffchange/viewStaffchangeDetail";
    }

    @GetMapping("/index/aftersales/evaluate/manage")
    public String manageEvaluate() {
        return "aftersales/evaluate/evaluateManage";
    }

    @GetMapping("/index/aftersales/evaluate/detail")
    public String evaluateDetail(String evaluateId, Model model) {
        if (Objects.nonNull(evaluateId)) {
            model.addAttribute("evaluateId", evaluateId);
        }
        return "aftersales/evaluate/viewEvaluateDetail";
    }

    @GetMapping("/index/aftersales/evaluate/deal")
    public String evaluateDeal(String evaluateId, Model model) {
        if (Objects.nonNull(evaluateId)) {
            model.addAttribute("evaluateId", evaluateId);
        }
        return "aftersales/evaluate/dealEvaluate";
    }

    @GetMapping("/index/aftersales/evaluate/reply")
    public String evaluateReply(String evaluateId, Model model) {
        if (Objects.nonNull(evaluateId)) {
            model.addAttribute("evaluateId", evaluateId);
        }
        return "aftersales/evaluate/replyEvaluate";
    }

    /**
     * ===========================财务管理入口================================
     */
    @GetMapping("/index/finance/refund/manage")
    public String manageFinanceRefund() {
        return "finance/refundManage";
    }

    @GetMapping("/index/finance/invoice/manage")
    public String manageFinanceInvoice() {
        return "finance/invoiceManage";
    }

    @GetMapping("/index/finance/withdraw/manage")
    public String manageFinanceWithdraw() {
        return "finance/withdrawManage";
    }

    @GetMapping("/index/finance/withdraw/dealWithdraw")
    public String dealFinanceWithdraw(String withdrawId, Model model) {
        if (Objects.nonNull(withdrawId)) {
            model.addAttribute("withdrawId", withdrawId);
        }
        return "finance/dealWithdraw";
    }

    /**
     * ===========================照护计划入口================================
     */
    @GetMapping("/index/careplan/manage")
    public String manageCarePlan() {
        return "careplan/careplanManage";
    }

    @GetMapping("/index/careplan/add")
    public String addCareplan(String planId, Model model) {
        if (Objects.nonNull(planId)) {
            model.addAttribute("planId", planId);
        }
        return "careplan/addCareplan";
    }

    @GetMapping("/index/careplan/dict/manage")
    public String manageCarePlanDict(String dictName, Model model) {
        if (Objects.nonNull(dictName)) {
            model.addAttribute("dictName", dictName);
        }
        return "careplan/dictManage";
    }

    @GetMapping("/index/careplan/dict/add")
    public String addCareplanDict(String dictId, String dictName, Model model) {
        if (Objects.nonNull(dictId)) {
            model.addAttribute("dictId", dictId);
        }
        if (Objects.nonNull(dictName)) {
            model.addAttribute("dictName", dictName);
        }
        return "careplan/addDict";
    }

    /**
     * ===========================问题管理入口================================
     */
    @GetMapping("/index/question/manage")
    public String manageQuestion() {
        return "question/questionManage";
    }

    @GetMapping("/index/question/add")
    public String addQuestion(String questionId, Model model) {
        if (Objects.nonNull(questionId)) {
            model.addAttribute("questionId", questionId);
        }
        return "question/addQuestion";
    }

    /**
     * ===========================医院管理入口================================
     */
    @GetMapping("/index/hospital/manage")
    public String manageHospital() {
        return "hospital/hospitalManage";
    }

    @GetMapping("/index/hospital/add")
    public String addHospital(String hospitalId, Model model) {
        if (Objects.nonNull(hospitalId)) {
            model.addAttribute("hospitalId", hospitalId);
        }
        return "hospital/addHospital";
    }

    /**
     * ===========================报表中心入口================================
     */
    @GetMapping("/index/report/platform/manage")
    public String managePlatformReport() {
        return "report/platformManage";
    }

    @GetMapping("/index/report/order/manage")
    public String manageOrderReport() {
        return "report/orderManage";
    }

    @GetMapping("/index/report/orderstatistic/manage")
    public String manageOrderStatisticReport() {
        return "report/orderStatisticManage";
    }

    @GetMapping("/index/report/quickorder/manage")
    public String manageQuickOrderReport() {
        return "report/quickOrderManage";
    }

    @GetMapping("/index/report/hospitalbiz/manage")
    public String manageHospitalBizReport() {
        return "report/hospitalBizManage";
    }

    @GetMapping("/index/report/orderlist/manage")
    public String manageOrderListForReport(String orderTime, String staffId, Model model) {
        model.addAttribute("staffId", staffId);
        model.addAttribute("orderTime", orderTime);
        return "report/orderListManage";
    }

    @GetMapping("/index/report/quickOrderlist/manage")
    public String manageQuickOrderListForReport(String hospitalName, String datemin, String datemax, String department,
                                                String paydatemin, String paydatemax, String serviceScope, Model model) {
        log.error("获取到的hospitalName是{}", hospitalName);
        model.addAttribute("datemin", datemin);
        model.addAttribute("datemax", datemax);
        model.addAttribute("paydatemin", paydatemin);
        model.addAttribute("paydatemax", paydatemax);
        model.addAttribute("serviceScope", serviceScope);
        try {
            model.addAttribute("hospitalName", URLDecoder.decode(hospitalName, "utf-8"));
            model.addAttribute("department", URLDecoder.decode(department, "utf-8"));
        } catch (Exception e) {
            log.warn("hospitalName解码失败:{}", e.getMessage());
            model.addAttribute("hospitalName", hospitalName);
            model.addAttribute("department", department);
        }
        return "report/quickOrderListManage";
    }

    @GetMapping("/index/report/quickRefundOrderlist/manage")
    public String manageQuickRefundOrderListForReport(String hospitalName, String datemin, String datemax, String department,
                                                String paydatemin, String paydatemax, Model model) {
        model.addAttribute("datemin", datemin);
        model.addAttribute("datemax", datemax);
        model.addAttribute("paydatemin", paydatemin);
        model.addAttribute("paydatemax", paydatemax);
        try {
            model.addAttribute("hospitalName", URLDecoder.decode(hospitalName, "utf-8"));
            model.addAttribute("department", URLDecoder.decode(department, "utf-8"));
        } catch (Exception e) {
            log.warn("hospitalName解码失败:{}", e.getMessage());
            model.addAttribute("hospitalName", hospitalName);
            model.addAttribute("department", department);
        }
        return "report/quickRefundOrderListManage";
    }

    @GetMapping("/index/report/evaluate/manage")
    public String manageEvaluateReport() {
        return "report/evaluateManage";
    }

}
