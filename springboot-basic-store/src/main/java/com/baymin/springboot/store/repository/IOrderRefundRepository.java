package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.OrderRefund;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IOrderRefundRepository extends PagingAndSortingRepository<OrderRefund, String>,
        JpaSpecificationExecutor<OrderRefund>,
        QuerydslPredicateExecutor<OrderRefund> {
}
