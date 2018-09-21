package com.baymin.springboot.service.impl;

import com.baymin.springboot.service.ISysManageService;
import com.baymin.springboot.store.entity.*;
import com.baymin.springboot.store.repository.IRelateRoleMenuRepository;
import com.baymin.springboot.store.repository.ISysDictRepository;
import com.baymin.springboot.store.repository.ISysMenuRepository;
import com.baymin.springboot.store.repository.ISysRoleRepository;
import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class SysManageServiceImpl implements ISysManageService {

    @Autowired
    private ISysMenuRepository sysMenuRepository;

    @Autowired
    private IRelateRoleMenuRepository relateRoleMenuRepository;

    @Autowired
    private ISysRoleRepository sysRoleRepository;

    @Autowired
    private ISysDictRepository sysDictRepository;

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
        if (StringUtils.isBlank(sysMenu.getId())) {
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
        if (StringUtils.isNotBlank(sysRole.getId())) {
            List<RelateRoleMenu> relateRoleMenus = relateRoleMenuRepository.findByRoleId(sysRole.getId());
            if (relateRoleMenus != null && !relateRoleMenus.isEmpty()) {
                for (RelateRoleMenu relateRoleMenu : relateRoleMenus) {
                    relateRoleMenuRepository.delete(relateRoleMenu);
                }
            }
        }
        sysRoleRepository.save(sysRole);
        if (sysRole.getMenuList() != null && !sysRole.getMenuList().isEmpty()) {
            RelateRoleMenu relateRoleMenu;
            for (SysMenu sysMenu : sysRole.getMenuList()) {
                if (StringUtils.isNotBlank(sysMenu.getId())) {
                    relateRoleMenu = new RelateRoleMenu(sysMenu.getId(), sysRole.getId());
                    relateRoleMenuRepository.save(relateRoleMenu);
                }
            }
        }

    }

    @Override
    public List<SysMenu> getMainMenuList() {
        return sysMenuRepository.findByLevel(1);
    }

    @Override
    public SysDict getSysDictById(String dictId) {
        return sysDictRepository.findById(dictId).orElse(null);
    }

    @Override
    public void saveSysDict(SysDict sysDict) {
        if (StringUtils.isBlank(sysDict.getId())) {
            sysDict.setCreateTime(new Date());
        } else {
            SysDict oldData = getSysDictById(sysDict.getId());
            if (Objects.isNull(oldData)) {
                sysDict.setCreateTime(new Date());
            } else {
                sysDict.setCreateTime(oldData.getCreateTime());
            }
        }
        sysDictRepository.save(sysDict);
    }

    @Override
    public Page<SysDict> getDictForPage(String dictName, String codeValue, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        QSysDict qSysDict = QSysDict.sysDict;

        if (StringUtils.isNotBlank(dictName)) {
            builder.and(qSysDict.dictName.eq(dictName));
        }
        if (StringUtils.isNotBlank(codeValue)) {
            builder.and(qSysDict.codeValue.eq(codeValue));
        }

        return sysDictRepository.findAll(builder, pageable);
    }

    @Override
    public void deleteSysDict(String dictId) {
        sysDictRepository.deleteById(dictId);
    }
}
