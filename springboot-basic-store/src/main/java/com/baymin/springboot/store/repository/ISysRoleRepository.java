package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.SysRole;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ISysRoleRepository extends PagingAndSortingRepository<SysRole, String>,
        JpaSpecificationExecutor<SysRole>,
        QuerydslPredicateExecutor<SysRole> {
}
