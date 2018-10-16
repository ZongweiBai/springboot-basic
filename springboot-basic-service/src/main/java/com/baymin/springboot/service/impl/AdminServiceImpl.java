package com.baymin.springboot.service.impl;

import com.baymin.springboot.service.IAdminService;
import com.baymin.springboot.service.IOrganizationService;
import com.baymin.springboot.store.entity.Admin;
import com.baymin.springboot.store.entity.Organization;
import com.baymin.springboot.store.entity.SysRole;
import com.baymin.springboot.store.repository.IAdminRepository;
import com.baymin.springboot.store.repository.ISysRoleRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminServiceImpl implements IAdminService {

    @Autowired
    private IAdminRepository adminRepository;

    @Autowired
    private IOrganizationService organizationService;

    @Autowired
    private ISysRoleRepository sysRoleRepository;

    @Override
    public Admin getAdminByAccount(String account) {
        return adminRepository.findByAccount(account);
    }

    @Override
    public void updateAdmin(Admin admin) {
        adminRepository.save(admin);
    }

    @Override
    public Page<Admin> queryAdminForPage(Pageable pageable) {
        Page<Admin> adminPage = adminRepository.findAll(pageable);
        if (CollectionUtils.isNotEmpty(adminPage.getContent())) {
            List<Organization> organizationList = organizationService.getAllOrg();
            List<SysRole> roleList = sysRoleRepository.findAllRoles();

            Map<String, Organization> organizationMap = organizationList.stream().collect(Collectors.toMap(Organization::getId, Function.identity()));
            Map<String, SysRole> roleMap = roleList.stream().collect(Collectors.toMap(SysRole::getId, Function.identity()));

            for (Admin admin : adminPage.getContent()) {
                admin.setSysRole(roleMap.get(admin.getRoleId()));
                admin.setOrganization(organizationMap.get(admin.getOrgId()));
            }
        }
        return adminPage;
    }

    @Override
    public Admin getAdminById(String userId) {
        return adminRepository.findById(userId).orElse(null);
    }

    @Override
    public void saveAdmin(Admin admin, Admin sysUser) {
        admin.setCreateTime(new Date());
        admin.setPassword("888888");
        adminRepository.save(admin);
    }
}
