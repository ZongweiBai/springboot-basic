package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.Admin;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IAdminRepository extends PagingAndSortingRepository<Admin, String>, JpaSpecificationExecutor<Admin> {
    Admin findByAccount(String account);

    @Query("select vo from Admin vo where vo.roleId in :roleIds order by vo.createTime asc")
    List<Admin> findByRoleIds(@Param("roleIds") List<String> roleIds);
}
