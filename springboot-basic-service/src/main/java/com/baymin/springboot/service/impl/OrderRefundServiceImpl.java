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
import com.baymin.springboot.store.entity.*;
import com.baymin.springboot.store.enumconstant.*;
import com.baymin.springboot.store.repository.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.baymin.springboot.common.constant.Constant.WechatTemplate.T_MSG_REDIRECT_URL;
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

    @Autowired
    private IServiceStaffRepository serviceStaffRepository;

    @Autowired
    private IUserWalletRepository userWalletRepository;

    @Autowired
    private IStaffIncomeRepository staffIncomeRepository;

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
            if ((order.getCareType() == CareType.HOME_CARE || order.getCareType() == CareType.HOSPITAL_CARE) && orderExt.getServiceDuration() < orderRefund.getRefundDuration()) {
                throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), "退款天数不能大于订单天数"));
            } else if (order.getCareType() == CareType.REHABILITATION && orderExt.getServiceNumber() < orderRefund.getRefundDuration()) {
                throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), "退款次数不能大于订单次数"));
            }
        }

        // save orderRefundRequest
        orderRefund.setCareType(order.getCareType());
        orderRefund.setDealTime(new Date());
        orderRefund.setCreateTime(new Date());
        if (StringUtils.equals("SYS", orderRefund.getApplyType())) {
            orderRefund.setDealStatus(CommonDealStatus.AGREE);
            if (orderRefund.getRefundFee().equals(order.getTotalFee())) {
                order.setFullRefund(true);
            } else {
                order.setFullRefund(false);
            }
        } else {
            /*if (order.getCareType() == CareType.HOME_CARE || order.getCareType() == CareType.HOSPITAL_CARE) {
                if (orderExt.getServiceDuration().equals(orderRefund.getRefundDuration())) {
                    orderRefund.setRefundFee(order.getTotalFee());
                    order.setFullRefund(true);
                } else {
                    double refundFee = calcRefundFee(orderRefund, order);
                    orderRefund.setRefundFee(refundFee);
                    orderRefund.setDealStatus(CommonDealStatus.APPLY);
                    order.setFullRefund(false);
                }
            } else {
                if (orderRefund.getRefundFee().equals(order.getTotalFee())) {
                    order.setFullRefund(true);
                } else {
                    order.setFullRefund(false);
                }
            }*/
            orderRefund.setDealStatus(CommonDealStatus.APPLY);
            if (orderRefund.getRefundFee().equals(order.getTotalFee())) {
                order.setFullRefund(true);
            } else {
                order.setFullRefund(false);
            }
        }
        orderRefund.setUserId(order.getOrderUserId());
        orderRefundRepository.save(orderRefund);

        if (order.getFullRefund() && StringUtils.equals("SYS", orderRefund.getApplyType())) {
            order.setRefundStatus(orderRefund.getDealStatus());
            order.setStatus(OrderStatus.ORDER_FULL_REFUND);
            order.setCloseTime(new Date());
            orderRepository.save(order);

            fullRefundOrder(order);
        } else {
            order.setRefundStatus(orderRefund.getDealStatus());
            orderRepository.save(order);
        }

        return orderRefund;
    }

    @Override
    public void dealOrderRefund(OrderRefund orderRefund) {
        OrderRefund oldData = orderRefundRepository.findById(orderRefund.getId()).orElse(null);
        if (Objects.isNull(oldData)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), RECORD_NOT_EXIST));
        }
        Order order = orderRepository.findById(oldData.getOrderId()).orElse(null);
        if (Objects.isNull(order)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), ORDER_INFO_NOT_CORRECT));
        }

        oldData.setDealDesc(orderRefund.getDealDesc());
        oldData.setDealStatus(orderRefund.getDealStatus());
        if (orderRefund.getDealStatus() == CommonDealStatus.AGREE ||
                orderRefund.getDealStatus() == CommonDealStatus.COMPLETED) {
            oldData.setRefundDuration(orderRefund.getRefundDuration());
            oldData.setRefundFee(orderRefund.getRefundFee());
            orderRefund.setDealTime(new Date());

            if (orderRefund.getRefundFee().equals(order.getTotalFee())) {
                order.setFullRefund(true);
                order.setCloseTime(new Date());
                order.setStatus(OrderStatus.ORDER_FULL_REFUND);
            } else {
                order.setFullRefund(false);
            }
        }
        if (orderRefund.getDealStatus() == CommonDealStatus.COMPLETED) {
            oldData.setRefundTime(new Date());

            order.setFullRefund(null);
        }
        orderRefundRepository.save(oldData);

        orderRepository.save(order);
        order.setRefundStatus(orderRefund.getDealStatus());

        // 修改订单
        orderRepository.save(order);

        if (order.getStatus() == OrderStatus.ORDER_FULL_REFUND) {
            fullRefundOrder(order);
        }

        UserProfile userProfile = userProfileRepository.findById(oldData.getUserId()).orElse(null);
        if (Objects.nonNull(userProfile)) {
            // 退款申请审核通过
            if (orderRefund.getDealStatus() == CommonDealStatus.AGREE) {
                Map<String, String> templateParam = new HashMap<>();
                templateParam.put("orderNo", oldData.getOrderId());
                templateParam.put("name", userProfile.getNickName());
                smsSendRecordService.addSmsSendRecord(userProfile.getAccount(), Constant.AliyunAPI.ORDER_REFUND_AGREE, templateParam);

                if (StringUtils.isNotBlank(userProfile.getIdpId())) {
                    String redirectUrl = T_MSG_REDIRECT_URL + "#/applylist";
                    Map<String, String> extension = new HashMap<>();
                    extension.put("first", "您好，您的退款申请已通过");
                    extension.put("keyword1", userProfile.getNickName());
                    extension.put("keyword2", DateUtil.formatDate(new Date(), "yyyy年MM月dd号"));
                    extension.put("keyword3", "已通过");
                    extension.put("remark", "点击查看详情");
                    wechatService.sendTemplateMsg(userProfile.getIdpId(), Constant.WechatTemplate.T_REQUEST_AGREE, redirectUrl, extension);
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
                    String redirectUrl = T_MSG_REDIRECT_URL + "#/applylist";
                    Map<String, String> extension = new HashMap<>();
                    extension.put("first", userProfile.getNickName() + "，您好。您的退款申请未能通过审核");
                    extension.put("keyword1", DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    extension.put("keyword2", orderRefund.getDealDesc());
                    extension.put("remark", "点击查看详情");
                    wechatService.sendTemplateMsg(userProfile.getIdpId(), Constant.WechatTemplate.T_REQUEST_DENY, redirectUrl, extension);
                }
            }
        }
    }

    private void fullRefundOrder(Order order) {
        // 修改用户订单数
        UserProfile userProfile = userProfileRepository.findById(order.getOrderUserId()).orElse(null);
        if (Objects.nonNull(userProfile)) {
            if (Objects.isNull(userProfile.getOrderCount())) {
                userProfile.setOrderCount(1);
            } else {
                userProfile.setOrderCount(userProfile.getOrderCount() + 1);
            }
            userProfileRepository.save(userProfile);
        }

        if (StringUtils.isNotBlank(order.getServiceStaffId())) {
            ServiceStaff staff = serviceStaffRepository.findById(order.getServiceStaffId()).orElse(null);
            releaseStaffStatus(order, staff);
        }

        if (StringUtils.isNotBlank(order.getNurseId())) {
            ServiceStaff staff = serviceStaffRepository.findById(order.getServiceStaffId()).orElse(null);
            releaseStaffStatus(order, staff);
        }
    }

    private void releaseStaffStatus(Order order, ServiceStaff staff) {
        if (Objects.nonNull(staff)) {
            // 释放护士/护工的服务状态
            if (Objects.isNull(staff.getServiceCount())) {
                staff.setServiceCount(1);
            } else {
                staff.setServiceCount(staff.getServiceCount() + 1);
            }
            staff.setServiceOrderCount(staff.getServiceOrderCount() - 1);
            if (staff.getServiceOrderCount() <= 0) {
                staff.setServiceOrderCount(0);
                staff.setServiceStatus(ServiceStatus.FREE);
            }
            serviceStaffRepository.save(staff);

            // 计算护士/护工收入并记录
            double realIncome = 0.00D;

            UserWallet userWallet = userWalletRepository.findByUserId(staff.getId(), "S");
            if (Objects.isNull(userWallet)) {
                userWallet = new UserWallet();
                userWallet.setUserId(staff.getId());
                userWallet.setUserType("S");
                userWallet.setBalance(0.0);
                userWallet.setTotalIncome(0.0);
                userWallet.setTotalWithdraw(0.0);
                userWallet.setTotalInWithdrawing(0.0);
            }
            userWallet.setTotalIncome(BigDecimalUtil.add(userWallet.getTotalIncome(), realIncome));
            userWallet.setBalance(BigDecimalUtil.add(userWallet.getBalance(), realIncome));
            userWalletRepository.save(userWallet);

            StaffIncome staffIncome = new StaffIncome();
            staffIncome.setCreateTime(new Date());
            staffIncome.setIncome(realIncome);
            staffIncome.setOrderId(order.getId());
            staffIncome.setOrderTotalFee(order.getTotalFee());
            staffIncome.setStaffId(order.getServiceStaffId());
            staffIncome.setCurrentBalance(userWallet.getBalance());
            staffIncome.setIncomeType(IncomeType.INCOME);
            staffIncome.setIncomeRemark(order.getCareType().getName() + "结算");
            staffIncomeRepository.save(staffIncome);
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
