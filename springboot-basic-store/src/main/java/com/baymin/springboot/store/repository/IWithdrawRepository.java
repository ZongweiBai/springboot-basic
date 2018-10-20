package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.Withdraw;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IWithdrawRepository extends PagingAndSortingRepository<Withdraw, String>,
        JpaSpecificationExecutor<Withdraw>,
        QuerydslPredicateExecutor<Withdraw> {
}
