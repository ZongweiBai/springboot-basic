package com.baymin.springboot.service.impl;

import com.baymin.springboot.common.constant.Constant;
import com.baymin.springboot.common.exception.ErrorCode;
import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.common.exception.WebServerException;
import com.baymin.springboot.common.util.BigDecimalUtil;
import com.baymin.springboot.service.IOrderRefundService;
import com.baymin.springboot.service.ISmsSendRecordService;
import com.baymin.springboot.store.entity.Order;
import com.baymin.springboot.store.entity.OrderExt;
import com.baymin.springboot.store.entity.OrderRefund;
import com.baymin.springboot.store.entity.UserProfile;
import com.baymin.springboot.store.enumconstant.CommonDealStatus;
import com.baymin.springboot.store.enumconstant.OrderStatus;
import com.baymin.springboot.store.repository.IOrderExtRepository;
import com.baymin.springboot.store.repository.IOrderRefundRepository;
import com.baymin.springboot.store.repository.IOrderRepository;
import com.baymin.springboot.store.repository.IUserProfileRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.baymin.springboot.common.exception.ErrorDescription.ORDER_INFO_NOT_CORRECT;
import static com.baymin.springboot.common.exception.ErrorDescription.RECORD_NOT_EXIST;

@Service
@Transactional
public class OrderRefundServiceImpl implements IOrderRefundService {

    private static final Logger logger = LoggerFactory.getLogger(OrderRefundServiceImpl.class);

    @Autowired
    private IOrderRefundRepository orderRefundRepository;

    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private IOrderExtRepository orderExtRepository;

    @Autowired
    private IUserProfileRepository userProfileRepository;

    @Autowired
    private ISmsSendRecordService smsSendRecordService;

    @Override
    public OrderRefund saveOrderRefund(OrderRefund orderRefund) {
        Order order = orderRepository.findById(orderRefund.getOrderId()).orElse(null);
        if (Objects.isNull(order) ||
                (order.getStatus() != OrderStatus.ORDER_PROCESSING
                        && order.getStatus() != OrderStatus.ORDER_ASSIGN
                        && order.getStatus() != OrderStatus.ORDER_PAYED)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), ORDER_INFO_NOT_CORRECT));
        }

        if (StringUtils.isNotBlank(orderRefund.getId())) {
            OrderRefund oldData = orderRefundRepository.findById(orderRefund.getId()).orElse(null);
            if (Objects.isNull(oldData)) {
                throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), RECORD_NOT_EXIST));
            }
            oldData.setDealDesc(orderRefund.getDealDesc());
            oldData.setDealStatus(orderRefund.getDealStatus());
            if (orderRefund.getDealStatus() == CommonDealStatus.AGREE) {
                oldData.setRefundDuration(orderRefund.getRefundDuration());
                oldData.setRefundFee(orderRefund.getRefundFee());
            }
            orderRefundRepository.save(oldData);

            order.setRefundStatus(orderRefund.getDealStatus());
            orderRepository.save(order);

            UserProfile userProfile = userProfileRepository.findById(orderRefund.getUserId()).orElse(null);
            if (Objects.nonNull(userProfile)) {
                // 退款申请审核通过
                if (orderRefund.getDealStatus() == CommonDealStatus.AGREE) {
                    Map<String, String> templateParam = new HashMap<>();
                    templateParam.put("orderno", oldData.getOrderId());
                    templateParam.put("name", userProfile.getNickName());
                    smsSendRecordService.addSmsSendRecord(userProfile.getAccount(), Constant.AliyunAPI.ORDER_REFUND_AGREE, templateParam);
                }
                // 退款申请审核不通过
                else if (orderRefund.getDealStatus() == CommonDealStatus.REJECT) {
                    Map<String, String> templateParam = new HashMap<>();
                    templateParam.put("name", userProfile.getNickName());
                    templateParam.put("orderNo", oldData.getOrderId());
                    templateParam.put("reason", orderRefund.getDealDesc());
                    smsSendRecordService.addSmsSendRecord(userProfile.getAccount(), Constant.AliyunAPI.ORDER_REFUND_REJECT, templateParam);
                }
            }

        } else {
            orderRefund.setCareType(order.getCareType());
            orderRefund.setRefundTime(new Date());
            orderRefund.setDealTime(new Date());
            orderRefund.setCreateTime(new Date());
            if (orderRefund.getRefundFee() == null) {
                double refundFee = calcRefundFee(orderRefund, order);
                orderRefund.setRefundFee(refundFee);
            }
            orderRefundRepository.save(orderRefund);

            order.setRefundStatus(CommonDealStatus.APPLY);
            orderRepository.save(order);
        }
        return orderRefund;
    }

    private double calcRefundFee(OrderRefund orderRefund, Order order) {
        OrderExt orderExt = orderExtRepository.findByOrderId(orderRefund.getOrderId());
        try {
            double price = BigDecimalUtil.div(order.getTotalFee(), orderExt.getServiceDuration(), 2);
            return BigDecimalUtil.mul(price, orderRefund.getRefundDuration());
        } catch (IllegalAccessException e) {
            logger.error("计算单价出错", e);
        }
        return 0;
    }

    @Override
    public void updateOrderRefund(String refundId, CommonDealStatus dealStatus, String dealDesc) {
        OrderRefund refund = orderRefundRepository.findById(refundId).orElse(null);
        refund.setDealStatus(dealStatus);
        if (Objects.nonNull(dealDesc)) {
            refund.setDealDesc(dealDesc);
        }
        refund.setDealTime(new Date());
        orderRefundRepository.save(refund);
    }

    @Override
    public List<OrderRefund> queryUserOrderRefund(String userId) {
        return orderRefundRepository.findByUserId(userId);
    }
}
