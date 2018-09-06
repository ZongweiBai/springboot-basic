package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.SysMenu;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ISysMenuRepository extends PagingAndSortingRepository<SysMenu, String>, JpaSpecificationExecutor<SysMenu> {

    @Query("select o from SysMenu o where o.id in :menuIds")
    List<SysMenu> findByMenuIds(@Param("menuIds") List<String> menuIdList);

    List<SysMenu> findByLevel(int level);
}
