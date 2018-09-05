package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.Admin;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IAdminRepository extends PagingAndSortingRepository<Admin, String>, JpaSpecificationExecutor<Admin> {
    Admin findByAccount(String account);
}
