package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.ServiceProduct;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IServiceProductRepository extends PagingAndSortingRepository<ServiceProduct, String>,
        JpaSpecificationExecutor<ServiceProduct>,
        QuerydslPredicateExecutor<ServiceProduct> {

    @Query("select t from ServiceProduct t where t.serviceTypeId = :typeId order by t.createTime desc")
    List<ServiceProduct> findByTypeId(@Param("typeId") String serviceTypeId);

    @Query("select t from ServiceProduct t where t.serviceTypeId in :typeIds order by t.createTime desc")
    List<ServiceProduct> findByTypeIds(@Param("typeIds") List<String> typeIds);
}
