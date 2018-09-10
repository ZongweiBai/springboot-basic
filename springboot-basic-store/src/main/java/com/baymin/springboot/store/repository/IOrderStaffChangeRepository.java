package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.OrderStaffChange;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IOrderStaffChangeRepository extends PagingAndSortingRepository<OrderStaffChange, String>,
        JpaSpecificationExecutor<OrderStaffChange>,
        QuerydslPredicateExecutor<OrderStaffChange> {
}
