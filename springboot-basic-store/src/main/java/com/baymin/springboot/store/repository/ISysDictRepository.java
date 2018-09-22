package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.SysDict;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ISysDictRepository extends PagingAndSortingRepository<SysDict, String>,
        JpaSpecificationExecutor<SysDict>,
        QuerydslPredicateExecutor<SysDict> {
    List<SysDict> findByDictName(String dictName);
}
