package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.SmsSendRecord;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ISmsSendRecordRepository extends PagingAndSortingRepository<SmsSendRecord, String>,
        JpaSpecificationExecutor<SmsSendRecord>,
        QuerydslPredicateExecutor<SmsSendRecord> {

    @Query("select t from SmsSendRecord t where t.sendTime is null order by t.createTime asc")
    List<SmsSendRecord> findUnsendRecord();

}
