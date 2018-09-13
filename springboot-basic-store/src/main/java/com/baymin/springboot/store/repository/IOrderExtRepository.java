package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.OrderExt;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IOrderExtRepository extends PagingAndSortingRepository<OrderExt, String>,
        JpaSpecificationExecutor<OrderExt> {
    OrderExt findByOrderId(String orderId);
}
