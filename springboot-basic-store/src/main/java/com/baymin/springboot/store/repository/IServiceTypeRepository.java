package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.ServiceType;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IServiceTypeRepository extends PagingAndSortingRepository<ServiceType, String>,
        JpaSpecificationExecutor<ServiceType>,
        QuerydslPredicateExecutor<ServiceType> {
}
