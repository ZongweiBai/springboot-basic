package com.baymin.springboot.service.impl;

import com.baymin.springboot.service.IAfterSalesService;
import com.baymin.springboot.store.entity.Evaluate;
import com.baymin.springboot.store.entity.OrderStaffChange;
import com.baymin.springboot.store.entity.QEvaluate;
import com.baymin.springboot.store.entity.QOrderStaffChange;
import com.baymin.springboot.store.enumconstant.CommonDealStatus;
import com.baymin.springboot.store.repository.IEvaluateRepository;
import com.baymin.springboot.store.repository.IOrderStaffChangeRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Objects;

@Service
@Transactional
public class AfterSalesServiceImpl implements IAfterSalesService {

    @Autowired
    private IOrderStaffChangeRepository orderStaffChangeRepository;

    @Autowired
    private IEvaluateRepository evaluateRepository;

    @Override
    public Page<OrderStaffChange> queryOrderChangePage(Pageable pageable, CommonDealStatus dealStatus, Date maxDate, Date minDate) {
        QOrderStaffChange qChange = QOrderStaffChange.orderStaffChange;

        BooleanExpression predicate = qChange.id.isNotNull();
        if (Objects.nonNull(dealStatus)) {
            predicate.and(qChange.dealStatus.eq(dealStatus));
        }
        if (Objects.nonNull(maxDate)) {
            predicate.and(qChange.createTime.lt(maxDate));
        }
        if (Objects.nonNull(minDate)) {
            predicate.and(qChange.createTime.gt(minDate));
        }

        return orderStaffChangeRepository.findAll(predicate, pageable);
    }

    @Override
    public Page<Evaluate> queryEvaluatePage(Pageable pageable, Integer grade, String orderId, Date maxDate, Date minDate) {
        QEvaluate qEvaluate = QEvaluate.evaluate;

        BooleanExpression predicate = qEvaluate.id.isNotNull();
        if (Objects.nonNull(grade)) {
            predicate.and(qEvaluate.grade.eq(grade));
        }
        if (StringUtils.isNotBlank(orderId)) {
            predicate.and(qEvaluate.orderId.eq(orderId));
        }
        if (Objects.nonNull(maxDate)) {
            predicate.and(qEvaluate.createTime.lt(maxDate));
        }
        if (Objects.nonNull(minDate)) {
            predicate.and(qEvaluate.createTime.gt(minDate));
        }

        return evaluateRepository.findAll(predicate, pageable);
    }
}
