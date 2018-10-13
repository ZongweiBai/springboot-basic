package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.OrderRefund;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IOrderRefundRepository extends PagingAndSortingRepository<OrderRefund, String>,
        JpaSpecificationExecutor<OrderRefund>,
        QuerydslPredicateExecutor<OrderRefund> {

    @Query("select vo from OrderRefund vo where vo.userId = :userId order by vo.createTime asc")
    List<OrderRefund> findByUserId(@Param("userId") String userId);
}
