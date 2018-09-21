package com.baymin.springboot.service;

import com.baymin.springboot.store.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ISysManageService {
    List<SysMenu> getAllSysMenu();

    List<RelateRoleMenu> getRelateRoleMenuByRoleId(String roleId);

    List<SysMenu> getSysMenuByIds(List<String> menuIdList);

    Page<SysMenu> queryMenuForPage(Pageable pageable);

    SysMenu getMenuById(String menuId);

    void saveMenu(SysMenu sysMenu, Admin sysUser);

    SysRole getRoleById(String roleId);

    Page<SysRole> queryRoleForPage(Pageable pageable);

    void saveRole(SysRole sysRole);

    List<SysMenu> getMainMenuList();

    SysDict getSysDictById(String dictId);

    void saveSysDict(SysDict sysDict);

    Page<SysDict> getDictForPage(String dictName, String codeValue, Pageable pageable);

    void deleteSysDict(String dictId);
}
