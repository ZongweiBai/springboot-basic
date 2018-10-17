package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.SysRole;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ISysRoleRepository extends PagingAndSortingRepository<SysRole, String>,
        JpaSpecificationExecutor<SysRole>,
        QuerydslPredicateExecutor<SysRole> {

    @Query("select vo from SysRole vo")
    List<SysRole> findAllRoles();

    List<SysRole> findByRoleType(String roleType);
}
