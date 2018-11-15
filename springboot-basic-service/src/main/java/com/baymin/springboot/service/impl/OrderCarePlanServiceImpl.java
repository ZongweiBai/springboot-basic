package com.baymin.springboot.service.impl;

import com.baymin.springboot.common.exception.ErrorCode;
import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.common.exception.WebServerException;
import com.baymin.springboot.common.util.DateUtil;
import com.baymin.springboot.service.IOrderCarePlanService;
import com.baymin.springboot.store.entity.*;
import com.baymin.springboot.store.enumconstant.OrderStatus;
import com.baymin.springboot.store.payload.SubCarePlanVo;
import com.baymin.springboot.store.repository.IOrderCarePlanRecordRepository;
import com.baymin.springboot.store.repository.IOrderCarePlanRepository;
import com.baymin.springboot.store.repository.IOrderRepository;
import com.baymin.springboot.store.repository.IServiceStaffRepository;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.baymin.springboot.common.exception.ErrorDescription.ORDER_INFO_NOT_CORRECT;

@Service
@Transactional
public class OrderCarePlanServiceImpl implements IOrderCarePlanService {

    @Autowired
    private IOrderCarePlanRepository orderCarePlanRepository;

    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private IOrderCarePlanRecordRepository orderCarePlanRecordRepository;

    @Autowired
    private IServiceStaffRepository serviceStaffRepository;

    @Override
    public OrderCarePlan findByOrderId(String orderId) {
        return orderCarePlanRepository.findByOrderId(orderId);
    }

    @Override
    public void saveOrderCarePlan(OrderCarePlan orderCarePlan) {
        Order order = orderRepository.findById(orderCarePlan.getOrderId()).orElse(null);
        if (Objects.isNull(order) ||
                (order.getStatus() != OrderStatus.ORDER_PROCESSING && order.getStatus() != OrderStatus.ORDER_ASSIGN)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), ORDER_INFO_NOT_CORRECT));
        }

        orderCarePlan.setCreateTime(new Date());
        orderCarePlanRepository.save(orderCarePlan);

        order.setCarePlanExists(true);
        orderRepository.save(order);
    }

    @Override
    public void saveOrderCarePlanRecord(String careplanId, String careplanSubId, String operate) {
        OrderCarePlan orderCarePlan = orderCarePlanRepository.findById(careplanId).orElse(null);
        if (Objects.isNull(orderCarePlan)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), "照护计划不存在"));
        }

        List<SubCarePlanVo> subCarePlanVoList = orderCarePlan.getSubCarePlans();
        Map<String, SubCarePlanVo> subCarePlanVoMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(subCarePlanVoList)) {
            subCarePlanVoMap = subCarePlanVoList.stream().collect(Collectors.toMap(SubCarePlanVo::getPlanId, Function.identity()));
        }

        SubCarePlanVo subCarePlanVo = subCarePlanVoMap.get(careplanSubId);
        if (Objects.isNull(subCarePlanVo)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), "照护计划详细信息不存在"));
        }

        Order order = orderRepository.findById(orderCarePlan.getOrderId()).orElse(null);
        if (Objects.isNull(order) ||
                (order.getStatus() != OrderStatus.ORDER_PROCESSING && order.getStatus() != OrderStatus.ORDER_ASSIGN)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), ORDER_INFO_NOT_CORRECT));
        }

        if (StringUtils.equals("start", operate)) {
            boolean executable = executable(careplanId, careplanSubId, subCarePlanVo);
            if (!executable) {
                throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), "该项照护计划今天已执行"));
            }

            OrderCarePlanRecord record = new OrderCarePlanRecord();
            record.setOrderId(order.getId());
            record.setOrderPlanId(careplanId);
            record.setOrderPlanSubId(careplanSubId);
            record.setStartTime(new Date());
            record.setStaffId(order.getServiceStaffId());
            orderCarePlanRecordRepository.save(record);

            if (order.getStatus() != OrderStatus.ORDER_PROCESSING) {
                order.setStatus(OrderStatus.ORDER_PROCESSING);
                orderRepository.save(order);
            }

            subCarePlanVo.setStatus(true);
            orderCarePlanRepository.save(orderCarePlan);
        } else if (StringUtils.equals("finish", operate)) {
            orderCarePlanRecordRepository.finishExecute(careplanId, careplanSubId, new Date(), order.getServiceStaffId());

            subCarePlanVo.setStatus(false);
            orderCarePlanRepository.save(orderCarePlan);
        }

    }

    @Override
    public List<OrderCarePlanRecord> findRecordByCarePlanId(String carePlanId) {
        BooleanBuilder builder = new BooleanBuilder();
        QOrderCarePlanRecord qRecord = QOrderCarePlanRecord.orderCarePlanRecord;
        builder.and(qRecord.orderPlanId.eq(carePlanId));

        Sort sort = Sort.by(Sort.Direction.DESC, "startTime");

        Iterable<OrderCarePlanRecord> iterable = orderCarePlanRecordRepository.findAll(builder, sort);
        List<OrderCarePlanRecord> recordList = new ArrayList<>();
        iterable.forEach(recordList::add);

        if (CollectionUtils.isEmpty(recordList)) {
            return recordList;
        }

        List<String> staffIds = recordList.stream().map(OrderCarePlanRecord::getStaffId).collect(Collectors.toList());
        List<ServiceStaff> staffList = serviceStaffRepository.findByIds(staffIds);
        Map<String, ServiceStaff> serviceStaffMap = staffList.stream().collect(Collectors.toMap(ServiceStaff::getId, Function.identity()));

        recordList.forEach(record -> {
            if (StringUtils.isNotBlank(record.getStaffId())) {
                if (Objects.nonNull(serviceStaffMap.get(record.getStaffId()))) {
                    record.setStaffName(serviceStaffMap.get(record.getStaffId()).getUserName());
                }
            }
        });

        return recordList;
    }

    private boolean executable(String careplanId, String careplanSubId, SubCarePlanVo subCarePlanVo) {
        if (subCarePlanVo.getStatus()) {
            return false;
        }
        List<OrderCarePlanRecord> recordList = findTodayRecord(careplanId, careplanSubId);
        if (CollectionUtils.isNotEmpty(recordList)) {
            return false;
        }
        return true;
    }

    private List<OrderCarePlanRecord> findTodayRecord(String careplanId, String careplanSubId) {
        Date today = new Date();

        BooleanBuilder builder = new BooleanBuilder();
        QOrderCarePlanRecord qRecord = QOrderCarePlanRecord.orderCarePlanRecord;
        builder.and(qRecord.orderPlanId.eq(careplanId));
        builder.and(qRecord.orderPlanSubId.eq(careplanSubId));
        builder.and(qRecord.endTime.between(DateUtil.dayBegin(today), DateUtil.dayEnd(today)));
        Iterable<OrderCarePlanRecord> recordIterable = orderCarePlanRecordRepository.findAll(builder);
        List<OrderCarePlanRecord> recordList = new ArrayList<>();
        recordIterable.forEach(recordList::add);
        return recordList;
    }
}
