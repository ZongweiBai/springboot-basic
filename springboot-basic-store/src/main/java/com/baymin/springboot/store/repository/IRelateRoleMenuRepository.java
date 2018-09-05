package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.RelateRoleMenu;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface IRelateRoleMenuRepository extends PagingAndSortingRepository<RelateRoleMenu, String>, JpaSpecificationExecutor<RelateRoleMenu> {
    List<RelateRoleMenu> findByRoleId(String roleId);
}
