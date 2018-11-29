package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.BasicItem;
import com.baymin.springboot.store.enumconstant.CommonStatus;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IBasicItemRepository extends PagingAndSortingRepository<BasicItem, String>,
        JpaSpecificationExecutor<BasicItem>,
        QuerydslPredicateExecutor<BasicItem> {

    @Query("select t from BasicItem t where t.status = :status order by t.createTime desc")
    List<BasicItem> findAllByStatus(@Param("status") CommonStatus status);

    @Query("select t from BasicItem t where t.id in :ids")
    List<BasicItem> findByIds(@Param("ids") List<String> basicItemIds);
}
