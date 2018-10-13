package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.OrderStaffChange;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IOrderStaffChangeRepository extends PagingAndSortingRepository<OrderStaffChange, String>,
        JpaSpecificationExecutor<OrderStaffChange>,
        QuerydslPredicateExecutor<OrderStaffChange> {

    @Query("select vo from OrderStaffChange vo where vo.userId = :userId order by vo.createTime asc")
    List<OrderStaffChange> findByUserId(@Param("userId") String userId);
}
