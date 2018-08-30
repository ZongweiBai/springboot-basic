package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.Order;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IOrderRepository extends PagingAndSortingRepository<Order, String>, JpaSpecificationExecutor<Order>, QueryDslPredicateExecutor<Order> {
}
