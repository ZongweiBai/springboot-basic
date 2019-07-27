package com.baymin.springboot.service;

import com.baymin.springboot.store.entity.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IAdminService {
    Admin getAdminByAccount(String userName);

    void updateAdmin(Admin admin);

    void updateAdminForManage(Admin admin);

    Page<Admin> queryAdminForPage(Pageable pageable);

    Admin getAdminById(String userId);

    void saveAdmin(Admin admin, Admin sysUser);

    List<Admin> queryAdminByRoleType(String roleType);
}
