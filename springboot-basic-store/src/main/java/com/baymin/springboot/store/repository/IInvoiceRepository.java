package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.Invoice;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface IInvoiceRepository extends PagingAndSortingRepository<Invoice, String>, JpaSpecificationExecutor<Invoice> {
    List<Invoice> findByOrderIds(String orderId);
}
