package com.baymin.springboot.service.impl;

import com.baymin.springboot.common.constant.Constant;
import com.baymin.springboot.common.exception.ErrorCode;
import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.common.exception.WebServerException;
import com.baymin.springboot.common.util.BigDecimalUtil;
import com.baymin.springboot.common.util.DateUtil;
import com.baymin.springboot.service.IOrderRefundService;
import com.baymin.springboot.service.ISmsSendRecordService;
import com.baymin.springboot.service.IWechatService;
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

    @Autowired
    private IWechatService wechatService;

    @Override
    public OrderRefund saveOrderRefund(OrderRefund orderRefund) {
        Order order = orderRepository.findById(orderRefund.getOrderId()).orElse(null);
        if (Objects.isNull(order) ||
                (order.getStatus() != OrderStatus.ORDER_PROCESSING
                        && order.getStatus() != OrderStatus.ORDER_ASSIGN
                        && order.getStatus() != OrderStatus.ORDER_PAYED)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), ORDER_INFO_NOT_CORRECT));
        }

        if (order.getTotalFee() < orderRefund.getRefundFee()) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), "退款金额不能大于订单金额"));
        }

        OrderExt orderExt = orderExtRepository.findByOrderId(order.getId());
        if (Objects.nonNull(orderExt)) {
            if (orderExt.getServiceDuration() < orderRefund.getRefundDuration()) {
                throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), "退款数量不能大于订单数量"));
            }
        }

        // save orderRefundRequest
        orderRefund.setCareType(order.getCareType());
        orderRefund.setDealTime(new Date());
        orderRefund.setCreateTime(new Date());
        if (orderRefund.getRefundFee() == null) {
            double refundFee = calcRefundFee(orderRefund, order);
            orderRefund.setRefundFee(refundFee);
        }
        if (StringUtils.equals("SYS", orderRefund.getApplyType())) {
            orderRefund.setDealStatus(CommonDealStatus.AGREE);
        } else {
            orderRefund.setDealStatus(CommonDealStatus.APPLY);
        }
        orderRefund.setUserId(order.getOrderUserId());
        orderRefundRepository.save(orderRefund);

        order.setRefundStatus(orderRefund.getDealStatus());
        orderRepository.save(order);

        return orderRefund;
    }

    @Override
    public void dealOrderRefund(OrderRefund orderRefund) {
        OrderRefund oldData = orderRefundRepository.findById(orderRefund.getId()).orElse(null);
        if (Objects.isNull(oldData)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), RECORD_NOT_EXIST));
        }
        oldData.setDealDesc(orderRefund.getDealDesc());
        oldData.setDealStatus(orderRefund.getDealStatus());
        if (orderRefund.getDealStatus() == CommonDealStatus.AGREE ||
                orderRefund.getDealStatus() == CommonDealStatus.COMPLETED) {
            oldData.setRefundDuration(orderRefund.getRefundDuration());
            oldData.setRefundFee(orderRefund.getRefundFee());
            orderRefund.setDealTime(new Date());
        }
        if (orderRefund.getDealStatus() == CommonDealStatus.COMPLETED) {
            oldData.setRefundTime(new Date());
        }
        orderRefundRepository.save(oldData);

        Order order = orderRepository.findById(oldData.getOrderId()).orElse(null);
        if (Objects.nonNull(order)) {
            order.setRefundStatus(orderRefund.getDealStatus());
            orderRepository.save(order);
        }

        UserProfile userProfile = userProfileRepository.findById(oldData.getUserId()).orElse(null);
        if (Objects.nonNull(userProfile)) {
            // 退款申请审核通过
            if (orderRefund.getDealStatus() == CommonDealStatus.AGREE) {
                Map<String, String> templateParam = new HashMap<>();
                templateParam.put("orderno", oldData.getOrderId());
                templateParam.put("name", userProfile.getNickName());
                smsSendRecordService.addSmsSendRecord(userProfile.getAccount(), Constant.AliyunAPI.ORDER_REFUND_AGREE, templateParam);

                if (StringUtils.isNotBlank(userProfile.getIdpId())) {
                    Map<String, String> extension = new HashMap<>();
                    extension.put("first", "您好，您的退款申请已通过");
                    extension.put("keyword1", userProfile.getNickName());
                    extension.put("keyword2", DateUtil.formatDate(new Date(), "yyyy年MM月dd号"));
                    extension.put("keyword3", "已通过");
                    extension.put("remark", "点击查看详情");
                    wechatService.sendTemplateMsg(userProfile.getIdpId(), Constant.WechatTemplate.T_REQUEST_AGREE, extension);
                }
            }
            // 退款申请审核不通过
            else if (orderRefund.getDealStatus() == CommonDealStatus.REJECT) {
                if (StringUtils.isBlank(orderRefund.getDealDesc())) {
                    orderRefund.setDealDesc("后台审核不通过");
                }

                Map<String, String> templateParam = new HashMap<>();
                templateParam.put("name", userProfile.getNickName());
                templateParam.put("orderNo", oldData.getOrderId());
                templateParam.put("reason", orderRefund.getDealDesc());
                smsSendRecordService.addSmsSendRecord(userProfile.getAccount(), Constant.AliyunAPI.ORDER_REFUND_REJECT, templateParam);

                if (StringUtils.isNotBlank(userProfile.getIdpId())) {
                    Map<String, String> extension = new HashMap<>();
                    extension.put("first", userProfile.getNickName() + "，您好。您的退款申请未能通过审核");
                    extension.put("keyword1", DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    extension.put("keyword2", orderRefund.getDealDesc());
                    extension.put("remark", "点击查看详情");
                    wechatService.sendTemplateMsg(userProfile.getIdpId(), Constant.WechatTemplate.T_REQUEST_DENY, extension);
                }
            }
        }
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
        if (Objects.isNull(refund)) {
            return;
        }
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
