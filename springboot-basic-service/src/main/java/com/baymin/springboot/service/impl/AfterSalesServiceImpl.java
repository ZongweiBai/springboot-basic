package com.baymin.springboot.service.impl;

import com.baymin.springboot.service.IAfterSalesService;
import com.baymin.springboot.store.entity.*;
import com.baymin.springboot.store.enumconstant.CommonDealStatus;
import com.baymin.springboot.store.repository.IEvaluateRepository;
import com.baymin.springboot.store.repository.IOrderRefundRepository;
import com.baymin.springboot.store.repository.IOrderStaffChangeRepository;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

@Service
@Transactional
public class AfterSalesServiceImpl implements IAfterSalesService {

    @Autowired
    private IOrderStaffChangeRepository orderStaffChangeRepository;

    @Autowired
    private IEvaluateRepository evaluateRepository;

    @Autowired
    private IOrderRefundRepository orderRefundRepository;

    @Override
    public Page<OrderStaffChange> queryOrderChangePage(Pageable pageable, CommonDealStatus dealStatus, Date maxDate, Date minDate, String orderId) {
        BooleanBuilder builder = new BooleanBuilder();
        QOrderStaffChange qChange = QOrderStaffChange.orderStaffChange;

        if (Objects.nonNull(orderId)) {
            builder.and(qChange.orderId.eq(orderId));
        }
        if (Objects.nonNull(dealStatus)) {
            builder.and(qChange.dealStatus.eq(dealStatus));
        }
        if (Objects.nonNull(maxDate)) {
            builder.and(qChange.createTime.lt(maxDate));
        }
        if (Objects.nonNull(minDate)) {
            builder.and(qChange.createTime.gt(minDate));
        }

        return orderStaffChangeRepository.findAll(builder, pageable);
    }

    @Override
    public Page<Evaluate> queryEvaluatePage(Pageable pageable, Integer grade, String orderId, Date maxDate, Date minDate) {
        BooleanBuilder builder = new BooleanBuilder();
        QEvaluate qEvaluate = QEvaluate.evaluate;

        if (Objects.nonNull(grade)) {
            builder.and(qEvaluate.grade.eq(grade));
        }
        if (StringUtils.isNotBlank(orderId)) {
            builder.and(qEvaluate.orderId.eq(orderId));
        }
        if (Objects.nonNull(maxDate)) {
            builder.and(qEvaluate.createTime.lt(maxDate));
        }
        if (Objects.nonNull(minDate)) {
            builder.and(qEvaluate.createTime.gt(minDate));
        }

        return evaluateRepository.findAll(builder, pageable);
    }

    @Override
    public Page<OrderRefund> queryRefundPage(Pageable pageable, CommonDealStatus dealStatus, Date maxDate, Date minDate, String orderId) {
        BooleanBuilder builder = new BooleanBuilder();
        QOrderRefund qRefund = QOrderRefund.orderRefund;

        if (Objects.nonNull(dealStatus)) {
            builder.and(qRefund.dealStatus.eq(dealStatus));
        }
        if (StringUtils.isNotBlank(orderId)) {
            builder.and(qRefund.orderId.eq(orderId));
        }
        if (Objects.nonNull(maxDate)) {
            builder.and(qRefund.refundTime.lt(maxDate));
        }
        if (Objects.nonNull(minDate)) {
            builder.and(qRefund.refundTime.gt(minDate));
        }

        return orderRefundRepository.findAll(builder, pageable);
    }
}
