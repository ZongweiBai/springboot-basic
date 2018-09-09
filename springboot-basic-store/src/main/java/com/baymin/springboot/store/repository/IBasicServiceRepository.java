package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.BasicServiceFee;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IBasicServiceRepository extends PagingAndSortingRepository<BasicServiceFee, String>,
        JpaSpecificationExecutor<BasicServiceFee>,
        QuerydslPredicateExecutor<BasicServiceFee> {
}
