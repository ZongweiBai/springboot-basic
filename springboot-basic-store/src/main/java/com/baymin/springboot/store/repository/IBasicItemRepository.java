package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.BasicItem;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IBasicItemRepository extends PagingAndSortingRepository<BasicItem, String>,
        JpaSpecificationExecutor<BasicItem>,
        QuerydslPredicateExecutor<BasicItem> {
}
