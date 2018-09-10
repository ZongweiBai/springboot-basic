package com.baymin.springboot.service;

import com.baymin.springboot.store.entity.Evaluate;
import com.baymin.springboot.store.entity.OrderStaffChange;
import com.baymin.springboot.store.enumconstant.CommonDealStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface IAfterSalesService {
    Page<OrderStaffChange> queryOrderChangePage(Pageable pageable, CommonDealStatus dealStatus, Date maxDate, Date minDate);

    Page<Evaluate> queryEvaluatePage(Pageable pageable, Integer grade, String orderId, Date maxDate, Date minDate);
}
