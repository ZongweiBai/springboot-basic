package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.Invoice;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IInvoiceRepository extends PagingAndSortingRepository<Invoice, String>, JpaSpecificationExecutor<Invoice> {
}
