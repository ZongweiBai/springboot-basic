package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.CarePlan;
import com.baymin.springboot.store.enumconstant.CommonStatus;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface ICarePlanRepository extends PagingAndSortingRepository<CarePlan, String>,
        JpaSpecificationExecutor<CarePlan>,
        QuerydslPredicateExecutor<CarePlan> {

    @Modifying
    @Query("update CarePlan t set t.status = :status where t.id = :id")
    void changeStatus(@Param("id") String planId, @Param("status") CommonStatus status);
}
