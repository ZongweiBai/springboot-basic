package com.baymin.springboot.service;

import com.baymin.springboot.store.entity.RelateRoleMenu;
import com.baymin.springboot.store.entity.SysMenu;

import java.util.List;

public interface IMenuService {
    List<SysMenu> getAllSysMenu();

    List<RelateRoleMenu> getRelateRoleMenuByRoleId(String roleId);

    List<SysMenu> getSysMenuByIds(List<String> menuIdList);
}
