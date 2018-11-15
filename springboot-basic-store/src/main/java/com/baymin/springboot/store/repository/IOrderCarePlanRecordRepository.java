package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.OrderCarePlanRecord;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface IOrderCarePlanRecordRepository extends PagingAndSortingRepository<OrderCarePlanRecord, String>,
        JpaSpecificationExecutor<OrderCarePlanRecord>,
        QuerydslPredicateExecutor<OrderCarePlanRecord> {

    @Modifying
    @Query("update OrderCarePlanRecord t set t.endTime= :endTime, t.staffId = :staffId where t.orderPlanId = :orderPlanId and t.orderPlanSubId = :planSubId and t.endTime is null")
    void finishExecute(@Param("orderPlanId") String careplanId, @Param("planSubId") String careplanSubId,
                       @Param("endTime") Date endTime, @Param("staffId") String staffId);
}
