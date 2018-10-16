package com.baymin.springboot.adminserver.controller;

import com.baymin.springboot.adminserver.constant.WebConstant;
import com.baymin.springboot.common.util.ImageUtil;
import com.baymin.springboot.service.IAdminService;
import com.baymin.springboot.service.IOrganizationService;
import com.baymin.springboot.service.ISysManageService;
import com.baymin.springboot.store.entity.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("system")
public class SystemController {

    @Autowired
    private IAdminService adminService;

    @Autowired
    private ISysManageService sysManageService;

    @Autowired
    private IOrganizationService organizationService;

    @PostMapping("/login")
    @ResponseBody
    public Map<String, Object> login(String userName, String passWord,
                                     HttpServletRequest request) {
        Map<String, Object> reMap = new HashMap<>();
        Admin admin = adminService.getAdminByAccount(userName);
        if (admin != null) {
            if (StringUtils.equals(admin.getPassword(), passWord)) {
                request.getSession().setAttribute(WebConstant.ADMIN_USER_SESSION, admin);
                reMap.put(WebConstant.RESULT, WebConstant.SUCCESS);

                admin.setLastLoginTime(new Date());
                adminService.updateAdmin(admin);
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
    public String main(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
        HttpSession session = request.getSession();
        Admin sysUser = (Admin) session.getAttribute(WebConstant.ADMIN_USER_SESSION);
        if (sysUser == null) {
            sysUser = (Admin) session.getAttribute(WebConstant.SELLER_USER_SESSION);
        }

        List<SysMenu> sysMenuList = new ArrayList<>();

        if (sysUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return "login";
        }

        if (sysUser.getGrade() == 1) {
            sysMenuList.addAll(sysManageService.getAllSysMenu());
        } else if (sysUser.getGrade() == 2) {
            if (sysUser.getRoleId() != null) {
                List<RelateRoleMenu> relateRoleMenus = sysManageService.getRelateRoleMenuByRoleId(sysUser.getRoleId());
                if (CollectionUtils.isNotEmpty(relateRoleMenus)) {
                    List<String> menuIdList = relateRoleMenus.stream().map(RelateRoleMenu::getMenuId).collect(Collectors.toList());
                    sysMenuList.addAll(sysManageService.getSysMenuByIds(menuIdList));
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

            mainSysMenuList = sysManageService.getSysMenuByIds(new ArrayList<>(mainMenuIdSet));

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
                    System.out.println(sysMenu);
                    System.out.println(sysMenu.getId());
                    System.out.println(subMenuMap.get(sysMenu.getId()));
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


    @ResponseBody
    @RequestMapping(value = "/modifyPasswd", method = RequestMethod.POST)
    public Map<String, Object> modifyPasswd(HttpServletRequest request) throws Exception {
        Map<String, Object> reMap = new HashMap<>();
        HttpSession session = request.getSession();
        Admin sysUser = (Admin) session.getAttribute(WebConstant.ADMIN_USER_SESSION);
        if (sysUser == null) {
            reMap.put(WebConstant.RESULT, WebConstant.FAULT);
            reMap.put(WebConstant.MESSAGE, "请先登录");
            return reMap;
        }
        String oldPasswd = request.getParameter("oldPasswd");
        String newPasswd = request.getParameter("newPasswd");
        String rePasswd = request.getParameter("rePasswd");
        if (StringUtils.isBlank(oldPasswd) || StringUtils.isBlank(newPasswd) || StringUtils.isBlank(rePasswd)) {
            reMap.put(WebConstant.RESULT, WebConstant.FAULT);
            reMap.put(WebConstant.MESSAGE, "参数不足");
            return reMap;
        }
        if (!newPasswd.equals(rePasswd)) {
            reMap.put(WebConstant.RESULT, WebConstant.FAULT);
            reMap.put(WebConstant.MESSAGE, "两次输入密码不一致");
            return reMap;
        }
        try {
            Admin admin = adminService.getAdminByAccount(sysUser.getAccount());
            if (admin == null) {
                reMap.put(WebConstant.RESULT, WebConstant.FAULT);
                reMap.put(WebConstant.MESSAGE, "用户不存在");
                return reMap;
            }
            if (!oldPasswd.equals(admin.getPassword())) {
                reMap.put(WebConstant.RESULT, WebConstant.FAULT);
                reMap.put(WebConstant.MESSAGE, "旧密码错误");
                return reMap;
            }
            admin.setPassword(newPasswd);
            adminService.updateAdmin(admin);
            request.getSession().removeAttribute(WebConstant.ADMIN_USER_SESSION);
            reMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            reMap.put(WebConstant.RESULT, WebConstant.FAULT);
            reMap.put(WebConstant.MESSAGE, "修改密码失败");
        }
        return reMap;
    }

    @ResponseBody
    @RequestMapping(value = "/resetPsd", method = RequestMethod.POST)
    public Map<String, Object> resetPsd(String account, HttpServletRequest request) throws Exception {
        Map<String, Object> reMap = new HashMap<>();
        HttpSession session = request.getSession();
        Admin sysUser = (Admin) session.getAttribute(WebConstant.ADMIN_USER_SESSION);
        if (sysUser == null) {
            reMap.put(WebConstant.RESULT, WebConstant.FAULT);
            reMap.put(WebConstant.MESSAGE, "请先登录");
            return reMap;
        }
        String newPasswd = "888888";
        try {
            Admin admin = adminService.getAdminByAccount(account);
            if (admin == null) {
                reMap.put(WebConstant.RESULT, WebConstant.FAULT);
                reMap.put(WebConstant.MESSAGE, "用户不存在");
                return reMap;
            }
            admin.setPassword(newPasswd);
            adminService.updateAdmin(admin);
            reMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
            reMap.put(WebConstant.INFO, newPasswd);
        } catch (Exception e) {
            e.printStackTrace();
            reMap.put(WebConstant.RESULT, WebConstant.FAULT);
            reMap.put(WebConstant.MESSAGE, "重置密码失败");
        }
        return reMap;
    }

    /**
     * wap图片上传，
     *
     * @throws
     * @Title: uploadWap
     * @param: base64    图片的base64编码
     * @param: size        图片的长度
     * @param: request
     * @return: Map<String   ,   Object>
     */
    @ResponseBody
    @RequestMapping(value = "uploadWap", method = RequestMethod.POST)
    public Map<String, Object> uploadWap(String base64, String size) throws Exception {
        Map<String, Object> reMap = new HashMap<>();
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            String timePath = simpleDateFormat.format(new Date());
            //保存图片文件
            String filePath = "/var/lib/modules/APP-Images/" + timePath;
            File imageFile = new File(filePath);
            if (!imageFile.exists()) {
                imageFile.mkdirs();
            }
            String fileName = Calendar.getInstance().getTimeInMillis() + ".jpg";
            ImageUtil.generateImage(base64, filePath + "/" + fileName);
            String fileFullPath = "/images/" + timePath + "/" + fileName;
            reMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
            reMap.put(WebConstant.INFO, fileFullPath);
        } catch (Exception e) {
            e.printStackTrace();
            reMap.put(WebConstant.RESULT, WebConstant.FAULT);
            reMap.put(WebConstant.INFO, "上传失败");
        }
        return reMap;
    }

    /*********************************
     * 系统菜单
     **********************************************/

    @ResponseBody
    @PostMapping(value = "queryMenuForPage")
    public Map<String, Object> queryMenuForPage(Pageable pageable, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();

        LinkedHashMap<String, String> order = new LinkedHashMap<>();
        order.put("createTime", "desc");
        pageable.getSort().and(new Sort(Sort.Direction.DESC, "createTime"));
        Page<SysMenu> sysMenuQueryResult = sysManageService.queryMenuForPage(pageable);
        resultMap.put(WebConstant.TOTAL, sysMenuQueryResult.getTotalElements());
        resultMap.put(WebConstant.ROWS, sysMenuQueryResult.getContent());
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "getMainMenuList", method = RequestMethod.POST)
    public Map<String, Object> getMainMenuList(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        List<SysMenu> sysMenuList = sysManageService.getMainMenuList();
        resultMap.put(WebConstant.ROWS, sysMenuList);
        resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "getMenuById", method = RequestMethod.POST)
    public Map<String, Object> getMenuById(String menuId, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            SysMenu sysMenu = sysManageService.getMenuById(menuId);
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
            resultMap.put(WebConstant.INFO, sysMenu);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "saveMenu", method = RequestMethod.POST)
    public Map<String, Object> saveMenu(SysMenu sysMenu, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        Admin sysUser = (Admin) request.getSession().getAttribute(WebConstant.ADMIN_USER_SESSION);
        try {
            sysManageService.saveMenu(sysMenu, sysUser);
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "保存失败");
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "getMenuByRoleId", method = RequestMethod.GET)
    public Map<String, Object> getMenuByRoleId(String roleId, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            List<RelateRoleMenu> relateList = sysManageService.getRelateRoleMenuByRoleId(roleId);
            List<SysMenu> sysMenuList = sysManageService.getAllSysMenu();
            if (relateList != null && !relateList.isEmpty()) {
                Set<String> menuIdSet = new HashSet<>();
                for (RelateRoleMenu relateRoleMenu : relateList) {
                    menuIdSet.add(relateRoleMenu.getMenuId());
                }
                for (SysMenu sysMenu : sysMenuList) {
                    if (menuIdSet.contains(sysMenu.getId())) {
                        sysMenu.setChecked(true);
                    }
                }
            }
            List<SysMenu> mainSysMenuList = formatSysMenuList(sysMenuList);
            SysRole sysRole = sysManageService.getRoleById(roleId);
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
            resultMap.put(WebConstant.ROWS, mainSysMenuList);
            resultMap.put(WebConstant.INFO, sysRole);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "加载出错：" + e.getMessage());
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "getAllMenu", method = RequestMethod.POST)
    public Map<String, Object> getAllMenu(HttpServletRequest request) {
        List<SysMenu> sysMenuList = sysManageService.getAllSysMenu();
        List<SysMenu> mainSysMenuList = formatSysMenuList(sysMenuList);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
        resultMap.put(WebConstant.ROWS, mainSysMenuList);
        return resultMap;
    }

    /*********************************
     * 系统角色
     **********************************************/

    @ResponseBody
    @RequestMapping(value = "queryRoleForPage", method = RequestMethod.POST)
    public Map<String, Object> queryRoleForPage(Pageable pageable, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        LinkedHashMap<String, String> order = new LinkedHashMap<>();
        order.put("createTime", "desc");
        pageable.getSort().and(new Sort(Sort.Direction.DESC, "createTime"));
        Page<SysRole> sysRoleQueryResult = sysManageService.queryRoleForPage(pageable);
        resultMap.put(WebConstant.TOTAL, sysRoleQueryResult.getTotalElements());
        resultMap.put(WebConstant.ROWS, sysRoleQueryResult.getContent());
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "saveRole", method = RequestMethod.POST)
    public Map<String, Object> saveRole(SysRole sysRole, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        Admin sysUser = (Admin) request.getSession().getAttribute(WebConstant.ADMIN_USER_SESSION);
        try {
            if (StringUtils.isBlank(sysRole.getId())) {
                sysRole.setCreateTime(new Date());
            } else {
                SysRole oldSysRole = sysManageService.getRoleById(sysRole.getId());
                sysRole.setCreateTime(oldSysRole.getCreateTime());
            }
            sysManageService.saveRole(sysRole);
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "保存失败");
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "getAllRoleList", method = RequestMethod.GET)
    public Map<String, Object> getAllRoleList(Pageable pageable, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        LinkedHashMap<String, String> order = new LinkedHashMap<>();
        order.put("createTime", "desc");
        pageable.getSort().and(new Sort(Sort.Direction.DESC, "createTime"));
        Page<SysRole> sysRoles = sysManageService.queryRoleForPage(pageable);
        resultMap.put(WebConstant.ROWS, sysRoles.getContent());
        resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
        return resultMap;
    }

    /**************************************
     * 系统用户管理
     ******************************************************/
    @ResponseBody
    @RequestMapping(value = "queryAdminForPage", method = RequestMethod.POST)
    public Map<String, Object> queryAdminForPage(Pageable pageable, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();

        LinkedHashMap<String, String> order = new LinkedHashMap<>();
        order.put("createTime", "desc");
        pageable.getSort().and(new Sort(Sort.Direction.DESC, "createTime"));
        Page<Admin> sysMenuQueryResult = adminService.queryAdminForPage(pageable);
        resultMap.put(WebConstant.TOTAL, sysMenuQueryResult.getTotalElements());
        resultMap.put(WebConstant.ROWS, sysMenuQueryResult.getContent());
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "getAdminById", method = RequestMethod.GET)
    public Map<String, Object> getAdminById(String userId, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        Admin admin = adminService.getAdminById(userId);
        if (admin == null) {
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "没有该用户");
        } else {
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
            resultMap.put(WebConstant.INFO, admin);
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "saveAdmin", method = RequestMethod.POST)
    public Map<String, Object> saveAdmin(Admin admin, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        Admin sysUser = (Admin) request.getSession().getAttribute(WebConstant.ADMIN_USER_SESSION);
        try {
            if (StringUtils.isBlank(admin.getId())) {
                admin.setGrade(2);
                adminService.saveAdmin(admin, sysUser);
            } else {
                Admin oldData = adminService.getAdminById(admin.getId());
                if (Objects.isNull(oldData)) {
                    resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
                    resultMap.put(WebConstant.MESSAGE, "没有该用户");
                }
                admin.setCreateTime(oldData.getCreateTime());
                admin.setGrade(oldData.getGrade());
                admin.setLastLoginTime(oldData.getLastLoginTime());
                admin.setPassword(oldData.getPassword());
                adminService.updateAdmin(admin);
            }
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "保存失败");
        }
        return resultMap;
    }

    /**************************************
     * 系统字典管理
     ******************************************************/
    @ResponseBody
    @RequestMapping(value = "queryDictForPage", method = RequestMethod.POST)
    public Map<String, Object> queryDictForPage(String dictName, String codeValue, Pageable pageable) {
        Map<String, Object> resultMap = new HashMap<>();

        pageable.getSort().and(new Sort(Sort.Direction.DESC, "createTime"));
        Page<SysDict> queryResult = sysManageService.getDictForPage(dictName, codeValue, pageable);
        resultMap.put(WebConstant.TOTAL, queryResult.getTotalElements());
        resultMap.put(WebConstant.ROWS, queryResult.getContent());
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "getDictById", method = RequestMethod.GET)
    public Map<String, Object> geDictById(String dictId) {
        Map<String, Object> resultMap = new HashMap<>();
        SysDict sysDict = sysManageService.getSysDictById(dictId);
        if (sysDict == null) {
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "没有该字典");
        } else {
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
            resultMap.put(WebConstant.INFO, sysDict);
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "saveSysDict", method = RequestMethod.POST)
    public Map<String, Object> saveSysDict(SysDict sysDict) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            sysManageService.saveSysDict(sysDict);
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "保存失败");
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "deleteSysDict", method = RequestMethod.GET)
    public Map<String, Object> deleteSysDict(String dictId) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            sysManageService.deleteSysDict(dictId);
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "删除失败");
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "getSysDictByDictName", method = RequestMethod.GET)
    public Map<String, Object> getSysDictByDictName(String dictName) {
        Map<String, Object> resultMap = new HashMap<>();
        List<SysDict> sysDicts = sysManageService.getSysDictByDictName(dictName);
        resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
        resultMap.put(WebConstant.ROWS, sysDicts);
        return resultMap;
    }

    /**************************************
     * 系统机构管理
     ******************************************************/
    @ResponseBody
    @RequestMapping(value = "queryOrgForPage", method = RequestMethod.POST)
    public Map<String, Object> queryOrgForPage(Pageable pageable, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();

        LinkedHashMap<String, String> order = new LinkedHashMap<>();
        order.put("createTime", "desc");
        pageable.getSort().and(new Sort(Sort.Direction.DESC, "createTime"));
        Page<Organization> queryResult = organizationService.queryOrgForPage(pageable);
        resultMap.put(WebConstant.TOTAL, queryResult.getTotalElements());
        resultMap.put(WebConstant.ROWS, queryResult.getContent());
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "getOrgById", method = RequestMethod.GET)
    public Map<String, Object> getOrgById(String orgId, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        Organization org = organizationService.getOrgById(orgId);
        if (org == null) {
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "没有该信息");
        } else {
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
            resultMap.put(WebConstant.INFO, org);
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "getAllOrg", method = RequestMethod.GET)
    public Map<String, Object> getAllOrg(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        List<Organization> org = organizationService.getAllOrg();
        if (org == null) {
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "没有该信息");
        } else {
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
            resultMap.put(WebConstant.ROWS, org);
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "saveOrg", method = RequestMethod.POST)
    public Map<String, Object> saveOrg(Organization organization, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        Admin sysUser = (Admin) request.getSession().getAttribute(WebConstant.ADMIN_USER_SESSION);
        try {
            organizationService.saveOrg(organization);
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "保存失败");
        }
        return resultMap;
    }

}
