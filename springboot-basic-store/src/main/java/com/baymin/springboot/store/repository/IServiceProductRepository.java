package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.ServiceProduct;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IServiceProductRepository extends PagingAndSortingRepository<ServiceProduct, String>,
        JpaSpecificationExecutor<ServiceProduct>,
        QuerydslPredicateExecutor<ServiceProduct> {
}
