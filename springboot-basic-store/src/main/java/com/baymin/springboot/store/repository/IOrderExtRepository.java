package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.OrderExt;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IOrderExtRepository extends PagingAndSortingRepository<OrderExt, String>,
        JpaSpecificationExecutor<OrderExt> {
    OrderExt findByOrderId(String orderId);

    @Query("select vo from OrderExt vo where vo.orderId in :orderIds")
    List<OrderExt> findByOrderIds(@Param("orderIds") List<String> orderIds);
}
