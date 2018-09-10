package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.PayRecord;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IPayRecordRepository extends PagingAndSortingRepository<PayRecord, String>,
        JpaSpecificationExecutor<PayRecord>,
        QuerydslPredicateExecutor<PayRecord> {
}
