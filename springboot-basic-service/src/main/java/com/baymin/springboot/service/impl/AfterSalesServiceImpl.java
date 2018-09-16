package com.baymin.springboot.service.impl;

import com.baymin.springboot.common.exception.ErrorCode;
import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.common.exception.WebServerException;
import com.baymin.springboot.service.IAfterSalesService;
import com.baymin.springboot.store.entity.*;
import com.baymin.springboot.store.enumconstant.CommonDealStatus;
import com.baymin.springboot.store.enumconstant.ServiceStatus;
import com.baymin.springboot.store.repository.*;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.baymin.springboot.common.exception.ErrorDescription.ORDER_INFO_NOT_CORRECT;

@Service
@Transactional
public class AfterSalesServiceImpl implements IAfterSalesService {

    @Autowired
    private IOrderStaffChangeRepository orderStaffChangeRepository;

    @Autowired
    private IEvaluateRepository evaluateRepository;

    @Autowired
    private IOrderRefundRepository orderRefundRepository;

    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private IOrderExtRepository orderExtRepository;

    @Autowired
    private IServiceStaffRepository serviceStaffRepository;

    @Autowired
    private IUserProfileRepository userProfileRepository;

    @Override
    public Page<OrderStaffChange> queryOrderChangePage(Pageable pageable, CommonDealStatus dealStatus, Date maxDate, Date minDate, String orderId) {
        BooleanBuilder builder = new BooleanBuilder();
        QOrderStaffChange qChange = QOrderStaffChange.orderStaffChange;

        if (StringUtils.isNotBlank(orderId)) {
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

    @Override
    public Map<String, Object> getRefundInfo(String refundId) {
        Map<String, Object> reMap = new HashMap<>();

        OrderRefund refund = orderRefundRepository.findById(refundId).orElse(null);
        reMap.put("refund", refund);

        Order order = orderRepository.findById(refund.getOrderId()).orElse(null);
        reMap.put("order", order);

        OrderExt orderExt = orderExtRepository.findByOrderId(order.getId());
        reMap.put("orderExt", orderExt);
        return reMap;
    }

    @Override
    public void dealStaffChange(OrderStaffChange change) {
        OrderStaffChange oldData = orderStaffChangeRepository.findById(change.getId()).orElse(null);
        if (Objects.isNull(oldData)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), ORDER_INFO_NOT_CORRECT));
        }

        if (change.getDealStatus() == CommonDealStatus.AGREE) {
            oldData.setNewStaffId(change.getNewStaffId());

            Order order = orderRepository.findById(oldData.getOrderId()).orElse(null);
            if (Objects.isNull(order)) {
                throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), ORDER_INFO_NOT_CORRECT));
            }
            order.setServiceStaffId(change.getNewStaffId());
            orderRepository.save(order);

            serviceStaffRepository.updateServiceStatus(change.getOldStaffId(), ServiceStatus.FREE);
            serviceStaffRepository.updateServiceStatus(change.getNewStaffId(), ServiceStatus.ASSIGNED);
        }
        oldData.setDealDesc(change.getDealDesc());
        oldData.setDealStatus(change.getDealStatus());
        oldData.setDealTime(new Date());
        orderStaffChangeRepository.save(oldData);
    }

    @Override
    public Map<String, Object> getChangeDetail(String changeId) {
        Map<String, Object> detailMap = new HashMap<>();
        OrderStaffChange change = orderStaffChangeRepository.findById(changeId).orElse(null);
        if (Objects.isNull(change)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), ORDER_INFO_NOT_CORRECT));
        }
        detailMap.put("change", change);

        Order order = orderRepository.findById(change.getOrderId()).orElse(null);
        detailMap.put("order", order);

        UserProfile userProfile = userProfileRepository.findById(order.getOrderUserId()).orElse(null);
        detailMap.put("user", userProfile);

        OrderExt orderExt = orderExtRepository.findByOrderId(change.getOrderId());
        detailMap.put("orderExt", orderExt);

        return detailMap;
    }
}
