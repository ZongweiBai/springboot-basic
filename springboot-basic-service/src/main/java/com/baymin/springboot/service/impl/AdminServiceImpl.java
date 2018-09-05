package com.baymin.springboot.service.impl;

import com.baymin.springboot.service.IAdminService;
import com.baymin.springboot.store.entity.Admin;
import com.baymin.springboot.store.repository.IAdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class AdminServiceImpl implements IAdminService {

    @Autowired
    private IAdminRepository adminRepository;

    @Override
    public Admin getAdminByAccount(String account) {
        return adminRepository.findByAccount(account);
    }
}
