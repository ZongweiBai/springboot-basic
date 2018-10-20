package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.ServiceType;
import com.baymin.springboot.store.enumconstant.CareType;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface IServiceTypeRepository extends PagingAndSortingRepository<ServiceType, String>,
        JpaSpecificationExecutor<ServiceType>,
        QuerydslPredicateExecutor<ServiceType> {

    List<ServiceType> findByCareType(CareType careType);

}
