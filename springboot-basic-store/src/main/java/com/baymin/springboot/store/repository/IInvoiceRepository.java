package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.Invoice;
import com.baymin.springboot.store.enumconstant.CommonDealStatus;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface IInvoiceRepository extends PagingAndSortingRepository<Invoice, String>,
        JpaSpecificationExecutor<Invoice>,
        QuerydslPredicateExecutor<Invoice> {

    @Modifying
    @Query("update Invoice set dealStatus = :dealStatus, dealTime = :date where id = :id")
    void updateDealStatus(@Param("id") String invoiceId, @Param("dealStatus") CommonDealStatus dealStatus,
                          @Param("date") Date date);
}
