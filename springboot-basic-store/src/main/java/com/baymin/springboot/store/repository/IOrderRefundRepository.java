package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.OrderRefund;
import com.baymin.springboot.store.enumconstant.CommonDealStatus;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IOrderRefundRepository extends PagingAndSortingRepository<OrderRefund, String>,
        JpaSpecificationExecutor<OrderRefund>,
        QuerydslPredicateExecutor<OrderRefund> {

    @Query("select vo from OrderRefund vo where vo.userId = :userId order by vo.createTime asc")
    List<OrderRefund> findByUserId(@Param("userId") String userId);

    @Query("select COALESCE(sum(vo.refundFee),0) from OrderRefund  vo where vo.orderId = :orderId and vo.dealStatus in :dealStatus")
    Double sumRefundFeeByOrderId(@Param("orderId") String orderId, @Param("dealStatus") List<CommonDealStatus> dealStatus);

    @Query("select vo from OrderRefund vo where vo.orderId in :orderIds and vo.dealStatus = :dealStatus")
    List<OrderRefund> findByOrderIds(@Param("orderIds") List<String> orderIds, @Param("dealStatus") CommonDealStatus dealStatus);
}
