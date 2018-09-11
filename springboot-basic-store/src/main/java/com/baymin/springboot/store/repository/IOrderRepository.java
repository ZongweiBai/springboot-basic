package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.Order;
import com.baymin.springboot.store.enumconstant.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IOrderRepository extends PagingAndSortingRepository<Order, String>,
        JpaSpecificationExecutor<Order>,
        QuerydslPredicateExecutor<Order> {

    @Query("select o from Order o where o.id in :orderIds and o.invoiceStatus <> :notInvoiced")
    List<Order> findInvoicedOrder(@Param("orderIds") List<String> orderIdList, @Param("notInvoiced") InvoiceStatus notInvoiced);

    @Modifying
    @Query("update Order set invoiceStatus = :invoiceStatus, invoiceId = :invoiceId where id = :orderId")
    void updateInvoiceStatus(@Param("orderId") String orderId,
                             @Param("invoiceStatus") InvoiceStatus invoiceStatus,
                             @Param("invoiceId") String invoiceId);

    List<Order> findByOrderUserIdOrderByOrderTimeDesc(String userId);

    List<Order> findByServiceStaffIdOrderByOrderTimeDesc(String staffId);
}
