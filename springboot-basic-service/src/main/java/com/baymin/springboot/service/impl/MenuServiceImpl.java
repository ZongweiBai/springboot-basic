package com.baymin.springboot.service.impl;

import com.baymin.springboot.service.IMenuService;
import com.baymin.springboot.store.entity.RelateRoleMenu;
import com.baymin.springboot.store.entity.SysMenu;
import com.baymin.springboot.store.repository.IRelateRoleMenuRepository;
import com.baymin.springboot.store.repository.ISysMenuRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class MenuServiceImpl implements IMenuService {

    @Autowired
    private ISysMenuRepository sysMenuRepository;

    @Autowired
    private IRelateRoleMenuRepository relateRoleMenuRepository;

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
}
