package com.baymin.springboot.service.impl;

import com.baymin.springboot.common.constant.Constant;
import com.baymin.springboot.common.exception.ErrorCode;
import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.common.exception.WebServerException;
import com.baymin.springboot.common.util.DateUtil;
import com.baymin.springboot.service.IAfterSalesService;
import com.baymin.springboot.service.ISmsSendRecordService;
import com.baymin.springboot.service.IWechatService;
import com.baymin.springboot.store.entity.*;
import com.baymin.springboot.store.enumconstant.CommonDealStatus;
import com.baymin.springboot.store.enumconstant.ServiceStatus;
import com.baymin.springboot.store.repository.*;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.baymin.springboot.common.constant.Constant.WechatTemplate.T_MSG_REDIRECT_URL;
import static com.baymin.springboot.common.exception.ErrorDescription.ORDER_INFO_NOT_CORRECT;
import static com.baymin.springboot.common.exception.ErrorDescription.RECORD_NOT_EXIST;

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

    @Autowired
    private IInvoiceRepository invoiceRepository;

    @Autowired
    private ISmsSendRecordService smsSendRecordService;

    @Autowired
    private IWechatService wechatService;

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

        Page<OrderStaffChange> page = orderStaffChangeRepository.findAll(builder, pageable);
        if (CollectionUtils.isNotEmpty(page.getContent())) {
            List<String> staffIds = new ArrayList<>();
            List<String> userIds = new ArrayList<>();
            page.getContent().forEach(staffChange -> {
                if (StringUtils.isNotBlank(staffChange.getOldStaffId())) {
                    staffIds.add(staffChange.getOldStaffId());
                }
                if (StringUtils.isNotBlank(staffChange.getNewStaffId())) {
                    staffIds.add(staffChange.getNewStaffId());
                }
                if (StringUtils.isNotBlank(staffChange.getUserId())) {
                    userIds.add(staffChange.getUserId());
                }
            });

            List<ServiceStaff> serviceStaffList = serviceStaffRepository.findByIds(staffIds);
            Map<String, String> staffMap = serviceStaffList.stream().collect(Collectors.toMap(ServiceStaff::getId, ServiceStaff::getUserName));

            List<UserProfile> userProfileList = userProfileRepository.findByIds(userIds);
            Map<String, String> userMap = userProfileList.stream().collect(Collectors.toMap(UserProfile::getId, UserProfile::getNickName));

            page.getContent().forEach(staffChange -> {
                if (StringUtils.isNotBlank(staffChange.getOldStaffId())) {
                    staffChange.setOldStaffName(staffMap.get(staffChange.getOldStaffId()));
                }
                if (StringUtils.isNotBlank(staffChange.getNewStaffId())) {
                    staffChange.setNewStaffName(staffMap.get(staffChange.getNewStaffId()));
                }
                if (StringUtils.isNotBlank(staffChange.getUserId())) {
                    staffChange.setUserName(userMap.get(staffChange.getUserId()));
                }
            });


        }

        return page;
    }

    @Override
    public Page<Evaluate> queryEvaluatePage(Pageable pageable, Integer grade, String orderId, CommonDealStatus auditStatus, Date maxDate, Date minDate, Set<String> hospitalNameSet) {
        BooleanBuilder builder = new BooleanBuilder();
        QEvaluate qEvaluate = QEvaluate.evaluate;

        if (Objects.nonNull(grade)) {
            builder.and(qEvaluate.grade.eq(grade));
        }
        if (StringUtils.isNotBlank(orderId)) {
            builder.and(qEvaluate.orderId.eq(orderId));
        }
        if (Objects.nonNull(auditStatus)) {
            builder.and(qEvaluate.auditStatus.eq(auditStatus));
        }
        if (Objects.nonNull(maxDate)) {
            builder.and(qEvaluate.createTime.lt(maxDate));
        }
        if (Objects.nonNull(minDate)) {
            builder.and(qEvaluate.createTime.gt(minDate));
        }
        if (CollectionUtils.isNotEmpty(hospitalNameSet)) {
            builder.and(qEvaluate.hospitalName.in(hospitalNameSet));
        }

        Page<Evaluate> page = evaluateRepository.findAll(builder, pageable);
        if (CollectionUtils.isNotEmpty(page.getContent())) {
            List<String> userIds = new ArrayList<>();
            page.getContent().forEach(evaluate -> {
                if (StringUtils.isNotBlank(evaluate.getUserId())) {
                    userIds.add(evaluate.getUserId());
                }
            });

            List<UserProfile> userProfileList = userProfileRepository.findByIds(userIds);
            Map<String, UserProfile> userMap = userProfileList.stream().collect(Collectors.toMap(UserProfile::getId, Function.identity()));

            page.getContent().forEach(evaluate -> {
                if (StringUtils.isNotBlank(evaluate.getUserId())) {
                    evaluate.setUserProfile(userMap.get(evaluate.getUserId()));
                }
            });
        }
        return page;
    }

    @Override
    public List<Evaluate> queryEvaluateList(Integer grade, String orderId, Date maxDate, Date minDate) {
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

        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");

        Iterable<Evaluate> evaluates = evaluateRepository.findAll(builder, sort);
        List<Evaluate> evaluateList = new ArrayList<>();
        List<String> userIds = new ArrayList<>();
        evaluates.forEach(evaluate -> {
            if (StringUtils.isNotBlank(evaluate.getUserId())) {
                userIds.add(evaluate.getUserId());
            }
        });

        List<UserProfile> userProfileList = userProfileRepository.findByIds(userIds);
        Map<String, UserProfile> userMap = userProfileList.stream().collect(Collectors.toMap(UserProfile::getId, Function.identity()));

        evaluates.forEach(evaluate -> {
            if (StringUtils.isNotBlank(evaluate.getUserId())) {
                evaluate.setUserProfile(userMap.get(evaluate.getUserId()));
            }
            evaluateList.add(evaluate);
        });
        return evaluateList;
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
    public Page<OrderRefund> queryRefundPageForFinance(Pageable pageable, CommonDealStatus dealStatus, Date maxDate, Date minDate, String orderId) {
        BooleanBuilder builder = new BooleanBuilder();
        QOrderRefund qRefund = QOrderRefund.orderRefund;

        if (Objects.nonNull(dealStatus)) {
            builder.and(qRefund.dealStatus.eq(dealStatus));
        } else {
            builder.and(qRefund.dealStatus.in(CommonDealStatus.AGREE, CommonDealStatus.COMPLETED));
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
    public Page<Invoice> queryInvoicePage(Pageable pageable, CommonDealStatus dealStatus, Date maxDate, Date minDate, String orderId) {
        BooleanBuilder builder = new BooleanBuilder();
        QInvoice qInvoice = QInvoice.invoice;

        if (Objects.nonNull(dealStatus)) {
            builder.and(qInvoice.dealStatus.eq(dealStatus));
        }
        if (StringUtils.isNotBlank(orderId)) {
            builder.and(qInvoice.orderIds.likeIgnoreCase("%" + orderId + "%"));
        }
        if (Objects.nonNull(maxDate)) {
            builder.and(qInvoice.createTime.lt(maxDate));
        }
        if (Objects.nonNull(minDate)) {
            builder.and(qInvoice.createTime.gt(minDate));
        }

        return invoiceRepository.findAll(builder, pageable);
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
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), RECORD_NOT_EXIST));
        }

        Order order = orderRepository.findById(oldData.getOrderId()).orElse(null);
        if (Objects.isNull(order)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), ORDER_INFO_NOT_CORRECT));
        }

        UserProfile userProfile = userProfileRepository.findById(order.getOrderUserId()).orElse(null);

        if (change.getDealStatus() == CommonDealStatus.AGREE) {
            oldData.setNewStaffId(change.getNewStaffId());
            order.setServiceStaffId(change.getNewStaffId());
            orderRepository.save(order);

            serviceStaffRepository.updateServiceStatus(change.getOldStaffId(), ServiceStatus.FREE);
            serviceStaffRepository.updateServiceStatus(change.getNewStaffId(), ServiceStatus.ASSIGNED);

            // 换人申请审核通过
            if (Objects.nonNull(userProfile)) {
                Map<String, String> templateParam = new HashMap<>();
                templateParam.put("orderNo", oldData.getOrderId());
                templateParam.put("name", userProfile.getNickName());
                smsSendRecordService.addSmsSendRecord(userProfile.getAccount(), Constant.AliyunAPI.ORDER_CHANGE_AGREE, templateParam);

                if (StringUtils.isNotBlank(userProfile.getIdpId())) {
                    String redirectUrl = T_MSG_REDIRECT_URL + "#/applylist?t=user";
                    Map<String, String> extension = new HashMap<>();
                    extension.put("first", "您好，您的换人申请已通过");
                    extension.put("keyword1", userProfile.getNickName());
                    extension.put("keyword2", DateUtil.formatDate(new Date(), "yyyy年MM月dd号"));
                    extension.put("keyword3", "已通过");
                    extension.put("remark", "点击查看详情");
                    wechatService.sendTemplateMsg(userProfile.getIdpId(), Constant.WechatTemplate.T_REQUEST_AGREE, redirectUrl, extension);
                }
            }
        } else {
            if (StringUtils.isBlank(change.getDealDesc())) {
                change.setDealDesc("后台审核不通过");
            }

            // 换人申请审核不通过
            if (Objects.nonNull(userProfile)) {
                Map<String, String> templateParam = new HashMap<>();
                templateParam.put("name", userProfile.getNickName());
                templateParam.put("orderNo", oldData.getOrderId());
                templateParam.put("reason", change.getDealDesc());
                smsSendRecordService.addSmsSendRecord(userProfile.getAccount(), Constant.AliyunAPI.ORDER_CHANGE_REJECT, templateParam);

                if (StringUtils.isNotBlank(userProfile.getIdpId())) {
                    String redirectUrl = T_MSG_REDIRECT_URL + "#/applylist?t=user";
                    Map<String, String> extension = new HashMap<>();
                    extension.put("first", userProfile.getNickName() + "，您好。您的换人申请未能通过审核");
                    extension.put("keyword1", DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    extension.put("keyword2", change.getDealDesc());
                    extension.put("remark", "点击查看详情");
                    wechatService.sendTemplateMsg(userProfile.getIdpId(), Constant.WechatTemplate.T_REQUEST_DENY, redirectUrl, extension);
                }
            }
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

    @Override
    public void updateInvoice(String invoiceId, CommonDealStatus dealStatus) {
        invoiceRepository.updateDealStatus(invoiceId, dealStatus, new Date());
    }

    @Override
    public void deleteEvaluate(String evaluateId) {
        evaluateRepository.deleteById(evaluateId);
    }

    @Override
    public Evaluate getEvaluateInfo(String evaluateId) {
        return evaluateRepository.findById(evaluateId).orElse(null);
    }

    @Override
    public void dealEvaluate(Evaluate evaluate) {
        Evaluate oldDate = getEvaluateInfo(evaluate.getId());
        if (Objects.nonNull(oldDate)) {
            oldDate.setAuditStatus(evaluate.getAuditStatus());
            oldDate.setAuditDesc(evaluate.getAuditDesc());
            oldDate.setAuditTime(new Date());
            oldDate.setAuditUser(evaluate.getAuditUser());
            evaluateRepository.save(oldDate);
        }
    }

    @Override
    public void replyEvaluate(Evaluate evaluate) {
        Evaluate oldDate = getEvaluateInfo(evaluate.getId());
        if (Objects.nonNull(oldDate)) {
            oldDate.setReply(evaluate.getReply());
            oldDate.setReplyTime(new Date());
            evaluateRepository.save(oldDate);
        }
    }
}
