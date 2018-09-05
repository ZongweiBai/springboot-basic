package com.baymin.springboot.service;

import com.baymin.springboot.store.entity.Admin;

public interface IAdminService {
    Admin getAdminByAccount(String userName);
}
