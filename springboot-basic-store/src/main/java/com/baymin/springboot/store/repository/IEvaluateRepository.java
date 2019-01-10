package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.Evaluate;
import com.baymin.springboot.store.enumconstant.CommonDealStatus;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IEvaluateRepository extends PagingAndSortingRepository<Evaluate, String>,
        JpaSpecificationExecutor<Evaluate>,
        QuerydslPredicateExecutor<Evaluate> {

    @Query("select vo from Evaluate vo where vo.orderId = :orderId order by vo.createTime desc")
    List<Evaluate> findByOrderId(@Param("orderId") String orderId);
}
