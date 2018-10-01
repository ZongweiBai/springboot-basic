package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.Hospital;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IHospitalRepository extends PagingAndSortingRepository<Hospital, String>,
        JpaSpecificationExecutor<Hospital>,
        QuerydslPredicateExecutor<Hospital> {
}
