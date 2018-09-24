package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.OrderCarePlan;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IOrderCarePlanRepository extends PagingAndSortingRepository<OrderCarePlan, String>,
        JpaSpecificationExecutor<OrderCarePlan>,
        QuerydslPredicateExecutor<OrderCarePlan> {
    OrderCarePlan findByOrderId(String orderId);
}
