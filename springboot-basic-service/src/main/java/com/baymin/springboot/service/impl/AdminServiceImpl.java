package com.baymin.springboot.service.impl;

import com.baymin.springboot.service.IAdminService;
import com.baymin.springboot.store.entity.Admin;
import com.baymin.springboot.store.repository.IAdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class AdminServiceImpl implements IAdminService {

    @Autowired
    private IAdminRepository adminRepository;

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
        return adminRepository.findAll(pageable);
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
