package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.Area;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface IAreaRepository extends PagingAndSortingRepository<Area, String>,
        JpaSpecificationExecutor<Area>,
        QuerydslPredicateExecutor<Area> {

    List<Area> findByParentId(String parentId);
}
