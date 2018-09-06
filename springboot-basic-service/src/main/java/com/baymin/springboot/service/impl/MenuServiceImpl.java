package com.baymin.springboot.service.impl;

import com.baymin.springboot.service.IMenuService;
import com.baymin.springboot.store.entity.Admin;
import com.baymin.springboot.store.entity.RelateRoleMenu;
import com.baymin.springboot.store.entity.SysMenu;
import com.baymin.springboot.store.entity.SysRole;
import com.baymin.springboot.store.repository.IRelateRoleMenuRepository;
import com.baymin.springboot.store.repository.ISysMenuRepository;
import com.baymin.springboot.store.repository.ISysRoleRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class MenuServiceImpl implements IMenuService {

    @Autowired
    private ISysMenuRepository sysMenuRepository;

    @Autowired
    private IRelateRoleMenuRepository relateRoleMenuRepository;

    @Autowired
    private ISysRoleRepository sysRoleRepository;

    @Override
    public List<SysMenu> getAllSysMenu() {
        Iterable<SysMenu> sysMenuIterable = sysMenuRepository.findAll();
        List<SysMenu> list = Lists.newArrayList();
        sysMenuIterable.forEach(list::add);
        return list;
    }

    @Override
    public List<RelateRoleMenu> getRelateRoleMenuByRoleId(String roleId) {
        return relateRoleMenuRepository.findByRoleId(roleId);
    }

    @Override
    public List<SysMenu> getSysMenuByIds(List<String> menuIdList) {
        return sysMenuRepository.findByMenuIds(menuIdList);
    }

    @Override
    public Page<SysMenu> queryMenuForPage(Pageable pageable) {
        return sysMenuRepository.findAll(pageable);
    }

    @Override
    public SysMenu getMenuById(String menuId) {
        return sysMenuRepository.findById(menuId).orElse(null);
    }

    @Override
    public void saveMenu(SysMenu sysMenu, Admin sysUser) {
        if (sysMenu.getParentId() != null) {
            SysMenu mainSysMenu = sysMenuRepository.findById(sysMenu.getParentId()).orElse(null);
            if (mainSysMenu != null) {
                sysMenu.setParentName(mainSysMenu.getMenuName());
            }
        }
        if (sysMenu.getId() == null) {
            sysMenu.setCreateTime(new Date());
            sysMenu.setStatus(1);
            sysMenuRepository.save(sysMenu);
        } else {
            SysMenu oldSysMenu = sysMenuRepository.findById(sysMenu.getId()).orElse(null);
            oldSysMenu.setLevel(sysMenu.getLevel());
            oldSysMenu.setMenuIcon(sysMenu.getMenuIcon());
            oldSysMenu.setMenuName(sysMenu.getMenuName());
            oldSysMenu.setMenuUrl(sysMenu.getMenuUrl());
            oldSysMenu.setParentId(sysMenu.getParentId());
            oldSysMenu.setParentName(sysMenu.getParentName());
            sysMenuRepository.save(oldSysMenu);
        }
    }

    @Override
    public SysRole getRoleById(String roleId) {
        return sysRoleRepository.findById(roleId).orElse(null);
    }

    @Override
    public Page<SysRole> queryRoleForPage(Pageable pageable) {
        return sysRoleRepository.findAll(pageable);
    }

    @Override
    public void saveRole(SysRole sysRole) {
        sysRoleRepository.save(sysRole);
    }

    @Override
    public List<SysMenu> getMainMenuList() {
        return sysMenuRepository.findByLevel(1);
    }
}
