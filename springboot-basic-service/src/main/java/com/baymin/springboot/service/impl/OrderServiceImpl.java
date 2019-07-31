package com.baymin.springboot.service.impl;

import com.baymin.springboot.common.constant.Constant;
import com.baymin.springboot.common.constant.RequestConstant;
import com.baymin.springboot.common.exception.ErrorCode;
import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.common.exception.WebServerException;
import com.baymin.springboot.common.util.BigDecimalUtil;
import com.baymin.springboot.common.util.DateUtil;
import com.baymin.springboot.service.IOrderService;
import com.baymin.springboot.service.ISmsSendRecordService;
import com.baymin.springboot.service.IWechatService;
import com.baymin.springboot.store.dao.IInvoiceDao;
import com.baymin.springboot.store.dao.IOrderDao;
import com.baymin.springboot.store.entity.*;
import com.baymin.springboot.store.enumconstant.*;
import com.baymin.springboot.store.payload.BasicItemRequestVo;
import com.baymin.springboot.store.payload.EditOrderRequestVo;
import com.baymin.springboot.store.payload.OrderDetailVo;
import com.baymin.springboot.store.payload.UserOrderVo;
import com.baymin.springboot.store.payload.report.HospitalBizVo;
import com.baymin.springboot.store.payload.report.JSVo;
import com.baymin.springboot.store.payload.report.QuickOrderReport;
import com.baymin.springboot.store.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.baymin.springboot.common.constant.Constant.WechatTemplate.T_MSG_REDIRECT_URL;
import static com.baymin.springboot.common.exception.ErrorDescription.INVALID_REQUEST;
import static com.baymin.springboot.common.exception.ErrorDescription.ORDER_INFO_NOT_CORRECT;
import static com.baymin.springboot.store.enumconstant.CareType.HOME_CARE;
import static com.baymin.springboot.store.enumconstant.CareType.HOSPITAL_CARE;

@Service
@Transactional
public class OrderServiceImpl implements IOrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private IOrderDao orderDao;

    @Autowired
    private IInvoiceDao invoiceDao;

    @Autowired
    private IEvaluateRepository evaluateRepository;

    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private IOrderRefundRepository orderRefundRepository;

    @Autowired
    private IInvoiceRepository invoiceRepository;

    @Autowired
    private IServiceStaffRepository serviceStaffRepository;

    @Autowired
    private IPayRecordRepository payRecordRepository;

    @Autowired
    private IOrderStaffChangeRepository orderStaffChangeRepository;

    @Autowired
    private IUserProfileRepository userProfileRepository;

    @Autowired
    private IOrderExtRepository orderExtRepository;

    @Autowired
    private IServiceProductRepository serviceProductRepository;

    @Autowired
    private IBasicItemRepository basicItemRepository;

    @Autowired
    private ISmsSendRecordService smsSendRecordService;

    @Autowired
    private IStaffIncomeRepository staffIncomeRepository;

    @Autowired
    private IAdminRepository adminRepository;

    @Autowired
    private IUserWalletRepository userWalletRepository;

    @Autowired
    private IWechatService wechatService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Order saveUserOrder(UserOrderVo request) {
        Invoice invoice = request.getInvoice();
        Order order = new Order();

        order.setId(String.valueOf(System.currentTimeMillis()));
        order.setOrderUserId(request.getOrderUserId());
        order.setOrderTime(new Date());
        if ("WECHAT".equalsIgnoreCase(request.getOrderSource())) {
            order.setCareType(request.getOrderType());
            if (StringUtils.equals(RequestConstant.ONLINE_WECHAT, request.getPayway())) {
                order.setPayWay(PayWay.PAY_ONLINE_WITH_WECHAT);
            }
            order.setOrderSource("WECHAT");
            order.setStatus(OrderStatus.ORDER_UN_PAY);
        } else if ("PC".equalsIgnoreCase(request.getOrderSource())) {
            order.setCareType(request.getOrderType());
            PayWay payWay = PayWay.valueOf(request.getPayway());
            order.setPayWay(payWay);
//            order.setPayTime(new Date());
            order.setOrderSource("PC");
            order.setStatus(OrderStatus.ORDER_UN_PAY);
        } else {
            if (!validRequest(request)) {
                throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
            }
            order.setOrderUserId(queryUserIdByMobile(request.getContactMobile(), request.getContact()));
            order.setCareType(HOSPITAL_CARE);
            order.setPayWay(PayWay.PAY_ONLINE_WITH_WECHAT);
            order.setOrderSource("WECHAT_QUICK");
            order.setStatus(OrderStatus.ORDER_UN_PAY);
            order.setHospitalName(request.getHospitalName());
            order.setHospitalDepartment(request.getHospitalDepartment());
            order.setBedNo(request.getBedNo());
            order.setServiceMode(request.getServiceMode());
            order.setServiceScope(request.getServiceScope());
            order.setServiceAdminId(request.getServiceAdminId());
            request.setHospitalAddress(request.getHospitalName() + " " + request.getHospitalDepartment() + " " + request.getBedNo());
            request.setServiceAddress(request.getHospitalAddress());
            request.setOrderType(HOSPITAL_CARE);
        }
        order.setServiceProductId(request.getProductId());
        order.setBasicItemInfo(request.getBasicItems());

        if (Objects.nonNull(request.getTotalFee())) {
            order.setTotalFee(request.getTotalFee());
        } else {
            double totalFee = calculateTotalFee(request);
            order.setTotalFee(totalFee);
        }
        order.setVersion(0);
        if (Objects.isNull(request.getInvoice())) {
            order.setInvoiceStatus(InvoiceStatus.NOT_INVOICED);
        } else {
            order.setInvoiceStatus(InvoiceStatus.INVOICING);
        }
        order.setEvaluated(false);
        order.setCarePlanExists(false);
        order.setRemark(request.getRemark());

        OrderExt orderExt = new OrderExt();
        orderExt.setContact(request.getContact());
        orderExt.setContactMobile(request.getContactMobile());
        orderExt.setServiceAddress(request.getServiceAddress());
        orderExt.setServiceDuration(request.getServiceDuration());
        orderExt.setServiceNumber(request.getServiceNumber());
        orderExt.setHospitalAddress(request.getHospitalAddress());

        if (HOSPITAL_CARE == request.getOrderType() || HOME_CARE == request.getOrderType()) {
            orderExt.setServiceStartTime(new Date(request.getServiceStartDate()));
            if (Objects.nonNull(request.getServiceEndDate())) {
                orderExt.setServiceEndDate(new Date(request.getServiceEndDate()));
            }

            // 如果前端没有传ServiceDuration，自己计算
            if (Objects.isNull(orderExt.getServiceDuration()) && Objects.nonNull(orderExt.getServiceStartTime()) && Objects.nonNull(orderExt.getServiceEndDate())) {
                double duration = DateUtil.daysBetween(orderExt.getServiceStartTime(), orderExt.getServiceEndDate());
                orderExt.setServiceDuration(duration);
            }

            Map<String, Object> patientInfo = new HashMap<>();
            if (CollectionUtils.isNotEmpty(request.getQuestions())) {
                Map<String, List<Question>> questionMap = request.getQuestions().stream().collect(Collectors.groupingBy(Question::getQuestionType));
                if (!questionMap.isEmpty()) {
                    questionMap.forEach((key, value) -> patientInfo.put(key, value));
                    orderExt.setPatientInfo(patientInfo);
                }
            }
        }

        order = orderDao.saveUserOrder(order, orderExt);

        if (Objects.nonNull(invoice)) {
            invoice.setCreateTime(new Date());
            invoice.setDealStatus(CommonDealStatus.APPLY);
            invoice.setOrderIds(order.getId());
            invoice.setInvoiceFee(order.getTotalFee());
            invoiceDao.save(invoice);
        }

        return order;
    }

    private String queryUserIdByMobile(String contactMobile, String contact) {
        UserProfile userProfile = userProfileRepository.findByAccount(contactMobile);
        if (Objects.isNull(userProfile)) {
            userProfile = new UserProfile();
            userProfile.setAccount(contactMobile);
            userProfile.setActualName(contact);
            userProfile.setNickName(contact);
            userProfile.setRegisterTime(new Date());
            userProfile.setOrderCount(0);
            userProfile = userProfileRepository.save(userProfile);
        }
        return userProfile.getId();
    }

    private boolean validRequest(UserOrderVo request) {
        if (StringUtils.isBlank(request.getContact()) ||
                StringUtils.isBlank(request.getContactMobile()) ||
                StringUtils.isBlank(request.getHospitalName()) ||
                StringUtils.isBlank(request.getHospitalDepartment()) ||
//                StringUtils.isBlank(request.getBedNo()) ||
                Objects.isNull(request.getServiceMode()) ||
                Objects.isNull(request.getServiceScope()) ||
                Objects.isNull(request.getServiceStartDate()) ||
                Objects.isNull(request.getServiceEndDate()) ||
                Objects.isNull(request.getTotalFee()) ||
                StringUtils.isBlank(request.getServiceAdminId())) {
            return false;
        }
        return true;
    }

    private Double calculateTotalFee(UserOrderVo request) {
        double totalFee = 0.0;
        ServiceProduct product = serviceProductRepository.findById(request.getProductId()).orElse(null);
        if (Objects.nonNull(product)) {
            totalFee = BigDecimalUtil.mul(product.getProductPrice(), request.getServiceDuration());
        }
        if (HOSPITAL_CARE == request.getOrderType() || HOME_CARE == request.getOrderType()) {
            if (CollectionUtils.isNotEmpty(request.getBasicItems())) {
                List<String> productItems = Arrays.asList(product.getBasicItems().split(","));
                List<BasicItem> billingItems = new ArrayList<>();
                for (BasicItemRequestVo itemRequestVo : request.getBasicItems()) {
                    if (!productItems.contains(itemRequestVo.getId())) {
                        BasicItem itemInDB = basicItemRepository.findById(itemRequestVo.getId()).orElse(null);
                        if (Objects.nonNull(itemInDB)) {
                            billingItems.add(itemInDB);
                        }
                    }
                }

                if (CollectionUtils.isNotEmpty(billingItems)) {
                    double itemTotalFee = billingItems.stream().map(BasicItem::getItemFee).reduce(0.0, BigDecimalUtil::add);
                    totalFee = BigDecimalUtil.add(totalFee, itemTotalFee);
                }
            }
        }
        totalFee = BigDecimalUtil.mul(totalFee, request.getServiceDuration());
        return totalFee;
    }

    @Override
    public List<Order> queryUserOrder(String userId, String status, String ownerType, Date minDate, Date maxDate) {
        List<Order> orderList = orderDao.queryUserOrder(userId, status, ownerType, minDate, maxDate);

        for (Order order : orderList) {
            OrderExt orderExt = orderExtRepository.findByOrderId(order.getId());
            if (Objects.nonNull(orderExt)) {
                order.setOrderExt(orderExt);
            }
            if (StringUtils.isNotBlank(order.getServiceStaffId())) {
                ServiceStaff staff = serviceStaffRepository.findById(order.getServiceStaffId()).orElse(null);
                order.setServiceStaff(Objects.nonNull(staff) ? staff : null);
            }
            if (StringUtils.isNotBlank(order.getServiceProductId())) {
                ServiceProduct product = serviceProductRepository.findById(order.getServiceProductId()).orElse(null);
                order.setProduct(product);
            }
        }
        return orderList;
    }

    @Override
    public OrderDetailVo queryOrderDetail(String orderId) {
        return orderDao.queryOrderDetail(orderId);
    }

    @Override
    public Map<String, Object> getOrderBasic(String orderId, String type) {
        Map<String, Object> detailMap = new HashMap<>();
        Order order = orderRepository.findById(orderId).orElse(null);
        detailMap.put("order", order);

        OrderExt orderExt = orderExtRepository.findByOrderId(orderId);
        detailMap.put("orderExt", orderExt);

        if (StringUtils.equals(type, "advance")) {
            UserProfile profile = userProfileRepository.findById(order.getOrderUserId()).orElse(null);
            order.setUserProfile(profile);

            double productFee = 0.0;
            double itemFee = 0.0;
            String basicItems = "";
            ServiceProduct product = serviceProductRepository.findById(order.getServiceProductId()).orElse(null);
            if (Objects.nonNull(product)) {
                productFee = product.getProductPrice();
                basicItems = product.getBasicItems();
            }
            List<BasicItemRequestVo> basicItemInfo = order.getBasicItemInfo();
            String finalBasicItems = basicItems;

            if (CollectionUtils.isNotEmpty(basicItemInfo)) {
                List<String> basicItemIds = basicItemInfo.stream().filter(basicItemRequestVo -> !finalBasicItems.contains(basicItemRequestVo.getId())).map(BasicItemRequestVo::getId).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(basicItemIds)) {
                    List<BasicItem> basicItemList = basicItemRepository.findByIds(basicItemIds);
                    itemFee = basicItemList.stream().map(BasicItem::getItemFee).reduce((x, y) -> BigDecimalUtil.add(x, y)).orElse(0.00);
                }
            }
            order.setUnitPrice(BigDecimalUtil.add(productFee, itemFee));
        }

        return detailMap;
    }

    @Override
    public Map<String, Object> getOrderBasicWithUserInfo(String orderId) {
        Map<String, Object> detailMap = new HashMap<>();
        Order order = orderRepository.findById(orderId).orElse(null);
        detailMap.put("order", order);

        OrderExt orderExt = orderExtRepository.findByOrderId(orderId);
        detailMap.put("orderExt", orderExt);

        if (Objects.nonNull(order) && StringUtils.isNotBlank(order.getServiceStaffId())) {
            ServiceStaff staff = serviceStaffRepository.findById(order.getServiceStaffId()).orElse(null);
            detailMap.put("staff", staff);
        }

        if (Objects.nonNull(order) && StringUtils.isNotBlank(order.getServiceAdminId())) {
            Admin admin = adminRepository.findById(order.getServiceAdminId()).orElse(null);
            detailMap.put("admin", admin);
        }

        return detailMap;
    }

    @Override
    public Order queryOrderById(String orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    @Override
    public void orderEvaluate(Evaluate evaluate) {
        Order order = orderRepository.findById(evaluate.getOrderId()).orElse(null);
        if (Objects.isNull(order)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), ORDER_INFO_NOT_CORRECT));
        }

        evaluate.setCareType(order.getCareType());
        evaluate.setHospitalName(order.getHospitalName());
        evaluate.setCreateTime(new Date());
        evaluate.setAuditStatus(CommonDealStatus.APPLY);
        evaluate.setUserId(order.getOrderUserId());
        evaluateRepository.save(evaluate);

        order.setEvaluated(true);
        orderRepository.save(order);
    }

    @Override
    public List<Evaluate> queryOrderEvaluate(String orderId, CommonDealStatus dealStatus) {
        return evaluateRepository.findByOrderId(orderId);
    }

    @Override
    public List<Evaluate> queryEvaluate(String orderId, String userId) {
        BooleanBuilder builder = new BooleanBuilder();
        QEvaluate qEvaluate = QEvaluate.evaluate;
        if (StringUtils.isNotBlank(orderId)) {
            builder.and(qEvaluate.orderId.eq(orderId));
        }
        if (StringUtils.isNotBlank(userId)) {
            builder.and(qEvaluate.userId.eq(userId));
        }
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        Iterable<Evaluate> evaluateIterable = evaluateRepository.findAll(builder, sort);
        List<Evaluate> list = new ArrayList<>();
        evaluateIterable.forEach(list::add);
        return list;
    }

    @Override
    public void saveInvoiceRequest(Invoice invoice) {
        String orderIds = invoice.getOrderIds();
        List<String> orderIdList = Arrays.asList(orderIds.split(","));
        List<Order> invoicedOrders = orderRepository.findInvoicedOrder(orderIdList, InvoiceStatus.NOT_INVOICED);
        if (CollectionUtils.isNotEmpty(invoicedOrders)) {
            logger.error("Those orders:{} has been invoiced!", StringUtils.join(orderIdList));
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), "部分订单已开票，请重新选择"));
        }

        invoice.setCreateTime(new Date());
        invoice.setDealStatus(CommonDealStatus.APPLY);
        invoice.setInvoiceFee(orderRepository.sumTotalFeeByIds(orderIdList));
        invoiceDao.save(invoice);

        for (String orderId : orderIdList) {
            orderRepository.updateInvoiceStatus(orderId, InvoiceStatus.INVOICING, invoice.getId());
        }
    }

    @Override
    public Page<Order> queryOrderForPage(Pageable pageable, OrderStatus status, String orderId, CareType careType, Date maxDate, Date minDate, String payStatus, String orderSource, String account, String address, Set<String> hospitalNameSet) {

        String userId = null;
        if (StringUtils.isNotBlank(account)) {
            UserProfile profile = userProfileRepository.findByAccount(account);
            if (Objects.isNull(profile)) {
                return (PageImpl) new PageImpl<>(new ArrayList<>(), pageable, 0);
            }
            userId = profile.getId();
        }
        List<String> orderIds = null;
        if (StringUtils.isNotBlank(address)) {
            orderIds = jdbcTemplate.queryForList("select t.ORDER_ID from T_ORDER_EXT t where INSTR(t.SERVICE_ADDRESS, ?) > 0", new Object[]{address.trim()}, String.class);
            if (CollectionUtils.isEmpty(orderIds)) {
                return (PageImpl) new PageImpl<>(new ArrayList<>(), pageable, 0);
            }
        }

        BooleanBuilder builder = new BooleanBuilder();
        QOrder qOrder = QOrder.order;

        if (Objects.nonNull(status)) {
            builder.and(qOrder.status.eq(status));
        }
        if (StringUtils.isNotBlank(orderId)) {
            builder.and(qOrder.id.eq(orderId));
        }
        if (Objects.nonNull(careType)) {
            builder.and(qOrder.careType.eq(careType));
        }
        if (Objects.nonNull(maxDate)) {
            builder.and(qOrder.orderTime.lt(maxDate));
        }
        if (Objects.nonNull(minDate)) {
            builder.and(qOrder.orderTime.gt(minDate));
        }
        if (StringUtils.equals("T", payStatus)) {
            builder.and(qOrder.payTime.isNotNull());
        } else if (StringUtils.equals("F", payStatus)) {
            builder.and(qOrder.payTime.isNull());
        }
        if (StringUtils.isNotBlank(orderSource)) {
            builder.and(qOrder.orderSource.eq(orderSource));
        }
        if (StringUtils.isNotBlank(userId)) {
            builder.and(qOrder.orderUserId.eq(userId));
        }
        if (CollectionUtils.isNotEmpty(orderIds)) {
            builder.and(qOrder.id.in(orderIds));
        }
        if (CollectionUtils.isNotEmpty(hospitalNameSet)) {
            builder.and(qOrder.hospitalName.in(hospitalNameSet).or(qOrder.careType.eq(CareType.REHABILITATION)));
//            builder.and(qOrder.hospitalName.in(hospitalNameSet));
        }

        Page<Order> page = orderRepository.findAll(builder, pageable);
        if (CollectionUtils.isNotEmpty(page.getContent())) {
            List<String> staffIds = new ArrayList<>();
            List<String> adminIds = new ArrayList<>();
            List<String> userIds = new ArrayList<>();
            List<String> resultOrderIds = new ArrayList<>();

            page.getContent().forEach(order -> {
                resultOrderIds.add(order.getId());
                if (StringUtils.isNotBlank(order.getServiceStaffId())) {
                    staffIds.add(order.getServiceStaffId());
                }
                if (StringUtils.isNotBlank(order.getServiceAdminId())) {
                    adminIds.add(order.getServiceAdminId());
                    staffIds.add(order.getServiceAdminId());
                }
                if (StringUtils.isNotBlank(order.getOfflinePayAdminId())) {
                    adminIds.add(order.getOfflinePayAdminId());
                }
                if (StringUtils.isNotBlank(order.getOrderUserId())) {
                    userIds.add(order.getOrderUserId());
                }
            });

            Map<String, ServiceStaff> staffMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(staffIds)) {
                List<ServiceStaff> serviceStaffList = serviceStaffRepository.findByIds(staffIds);
                staffMap = serviceStaffList.stream().collect(Collectors.toMap(ServiceStaff::getId, Function.identity()));
            }

            Map<String, Admin> adminMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(adminIds)) {
                List<Admin> adminList = adminRepository.findByIds(adminIds);
                adminMap = adminList.stream().collect(Collectors.toMap(Admin::getId, Function.identity()));
            }

            Map<String, UserProfile> userMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(userIds)) {
                List<UserProfile> userProfileList = userProfileRepository.findByIds(userIds);
                userMap = userProfileList.stream().collect(Collectors.toMap(UserProfile::getId, Function.identity()));
            }

            Map<String, OrderExt> orderExtMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(resultOrderIds)) {
                List<OrderExt> orderExtList = orderExtRepository.findByOrderIds(resultOrderIds);
                orderExtMap = orderExtList.stream().collect(Collectors.toMap(OrderExt::getOrderId, Function.identity()));
            }

            Map<String, ServiceStaff> finalStaffMap = staffMap;
            Map<String, Admin> finalAdminMap = adminMap;
            Map<String, OrderExt> finalOrderExtMap = orderExtMap;
            Map<String, UserProfile> finalUserMap = userMap;
            page.getContent().forEach(order -> {
                if (StringUtils.isNotBlank(order.getServiceStaffId())) {
                    order.setServiceStaff(finalStaffMap.get(order.getServiceStaffId()));
                }
                if (StringUtils.isNotBlank(order.getServiceAdminId())) {
                    order.setAdmin(finalAdminMap.get(order.getServiceAdminId()));
                    order.setAdminStaff(finalStaffMap.get(order.getServiceAdminId()));
                }
                if (StringUtils.isNotBlank(order.getOfflinePayAdminId())) {
                    order.setOfflinePayAdmin(finalAdminMap.get(order.getOfflinePayAdminId()));
                }
                if (StringUtils.isNotBlank(order.getOrderUserId())) {
                    order.setUserProfile(finalUserMap.get(order.getOrderUserId()));
                }
                order.setOrderExt(finalOrderExtMap.get(order.getId()));
            });
        }

        return page;
    }

    @Override
    public List<Order> queryOrderForList(CareType careType, Date maxDate, Date minDate, OrderStatus status) {
        BooleanBuilder builder = new BooleanBuilder();
        QOrder qOrder = QOrder.order;

        if (Objects.nonNull(careType)) {
            builder.and(qOrder.careType.eq(careType));
        }
        if (Objects.nonNull(maxDate)) {
            builder.and(qOrder.orderTime.lt(maxDate));
        }
        if (Objects.nonNull(minDate)) {
            builder.and(qOrder.orderTime.gt(minDate));
        }
        if (Objects.nonNull(status)) {
            builder.and(qOrder.status.eq(status));
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "orderTime");

        Iterable<Order> iterable = orderRepository.findAll(builder, sort);

        List<Order> orderList = new ArrayList<>();

        List<String> staffIds = new ArrayList<>();
        List<String> adminIds = new ArrayList<>();
        iterable.forEach(order -> {
            if (StringUtils.isNotBlank(order.getServiceStaffId())) {
                staffIds.add(order.getServiceStaffId());
            }
            if (StringUtils.isNotBlank(order.getServiceAdminId())) {
                adminIds.add(order.getServiceAdminId());
                staffIds.add(order.getServiceAdminId());
            }
        });

        Map<String, ServiceStaff> staffMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(staffIds)) {
            List<ServiceStaff> serviceStaffList = serviceStaffRepository.findByIds(staffIds);
            staffMap = serviceStaffList.stream().collect(Collectors.toMap(ServiceStaff::getId, Function.identity()));
        }

        Map<String, Admin> adminMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(adminIds)) {
            List<Admin> adminList = adminRepository.findByIds(adminIds);
            adminMap = adminList.stream().collect(Collectors.toMap(Admin::getId, Function.identity()));
        }
        Map<String, ServiceStaff> finalStaffMap = staffMap;
        Map<String, Admin> finalAdminMap = adminMap;
        iterable.forEach(order -> {
            if (StringUtils.isNotBlank(order.getServiceStaffId())) {
                order.setServiceStaff(finalStaffMap.get(order.getServiceStaffId()));
            }
            if (StringUtils.isNotBlank(order.getServiceAdminId())) {
                order.setAdmin(finalAdminMap.get(order.getServiceAdminId()));
                order.setAdminStaff(finalStaffMap.get(order.getServiceAdminId()));
            }
            orderList.add(order);
        });
        return orderList;
    }

    @Override
    public Map<String, Object> getOrderDetail(String orderId) {
        Map<String, Object> detailMap = new HashMap<>();
        Order order = orderRepository.findById(orderId).orElse(null);
        detailMap.put("order", order);

        if (Objects.nonNull(order)) {
            UserProfile userProfile = userProfileRepository.findById(order.getOrderUserId()).orElse(null);
            detailMap.put("user", userProfile);
        }

        OrderExt orderExt = orderExtRepository.findByOrderId(orderId);
        detailMap.put("orderExt", orderExt);

        if (Objects.nonNull(order) && StringUtils.isNotBlank(order.getInvoiceId())) {
            Invoice invoice = invoiceRepository.findById(order.getInvoiceId()).orElse(null);
            if (Objects.nonNull(invoice)) {
                detailMap.put("invoice", invoice);
            }
        }

        if (Objects.nonNull(order) && StringUtils.isNotBlank(order.getServiceProductId())) {
            ServiceProduct product = serviceProductRepository.findById(order.getServiceProductId()).orElse(null);
            if (Objects.nonNull(product)) {
                detailMap.put("product", product);
            }
        }

        return detailMap;
    }

    @Override
    public void assignOrderStaff(String orderId, String staffId, String adminId, String nurseId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        ServiceStaff staff = serviceStaffRepository.findById(staffId).orElse(null);
        if (Objects.isNull(order) || Objects.isNull(staff)) {
            return;
        }
        ServiceStaff nurseStaff = null;
        if (StringUtils.isNotBlank(nurseId)) {
            nurseStaff = serviceStaffRepository.findById(nurseId).orElse(null);
            if (Objects.nonNull(nurseStaff)) {
                order.setNurseId(nurseId);
            }
        }

        order.setServiceStaffId(staffId);
        order.setStatus(OrderStatus.ORDER_ASSIGN);
        order.setServiceAdminId(adminId);
        orderRepository.save(order);

        if (staff.getServiceOrderCount() == null) {
            staff.setServiceOrderCount(1);
        } else {
            staff.setServiceOrderCount(staff.getServiceOrderCount() + 1);
        }
        staff.setServiceStatus(ServiceStatus.ASSIGNED);
        serviceStaffRepository.save(staff);

        // send sms
        Map<String, String> templateParam = new HashMap<>();
        templateParam.put("orderno", orderId);
        smsSendRecordService.addSmsSendRecord(staff.getMobile(), Constant.AliyunAPI.ORDER_ASSING, templateParam);
        if (Objects.nonNull(nurseStaff) && StringUtils.isNotBlank(nurseStaff.getMobile())) {
            smsSendRecordService.addSmsSendRecord(nurseStaff.getMobile(), Constant.AliyunAPI.ORDER_ASSING, templateParam);
        }

        UserProfile userProfile = userProfileRepository.findById(order.getOrderUserId()).orElse(null);
        if (Objects.nonNull(userProfile) && StringUtils.isNotBlank(userProfile.getIdpId())) {
            String redirectUrl = T_MSG_REDIRECT_URL + "#/orderDetail?id=" + orderId;
            Map<String, String> extension = new HashMap<>();
            extension.put("first", "订单完成指派通知");
            extension.put("keyword1", "订单服务");
            extension.put("keyword2", DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            extension.put("keyword3", "订单号" + orderId + "，完成服务人员调度");
            if (Objects.isNull(nurseStaff)) {
                extension.put("keyword4", "上门服务人员" + staff.getUserName());
            } else {
                extension.put("keyword4", "上门服务护工人员：" + staff.getUserName() + ";上门服务护士人员：" + nurseStaff.getUserName());
            }
            extension.put("remark", "点击查看详情");
            wechatService.sendTemplateMsg(userProfile.getIdpId(), Constant.WechatTemplate.T_ORDER_ASSIGNED, redirectUrl, extension);
        }
    }

    @Override
    public void offlinePay(PayRecord payRecord, Admin sysUser) {
        Order order = orderRepository.findById(payRecord.getOrderId()).orElse(null);
        if (Objects.isNull(order)) {
            return;
        }
        if (order.getStatus() == OrderStatus.ORDER_UN_PAY) {
            order.setStatus(OrderStatus.ORDER_PAYED);
        }
        order.setPayWay(payRecord.getPayWay());
        order.setPayTime(new Date());
        if (Objects.nonNull(sysUser)) {
            order.setOfflinePayAdminId(sysUser.getId());
        }
        orderRepository.save(order);

        payRecord.setCreateTime(new Date());
        payRecord.setFinishTime(new Date());
        payRecord.setPayerUserId(order.getOrderUserId());
        payRecord.setPayFee(order.getTotalFee());
        payRecord.setPayResult(true);
        payRecord.setResultDesc("线下支付");
        payRecordRepository.save(payRecord);
    }

    @Override
    public void staffChangeRequest(OrderStaffChange staffChange) {
        Order order = orderRepository.findById(staffChange.getOrderId()).orElse(null);
        if (Objects.isNull(order) ||
                (order.getStatus() != OrderStatus.ORDER_PROCESSING && order.getStatus() != OrderStatus.ORDER_ASSIGN)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), ORDER_INFO_NOT_CORRECT));
        }

        staffChange.setCreateTime(new Date());
        if (Objects.isNull(staffChange.getDealStatus())) {
            staffChange.setDealStatus(CommonDealStatus.APPLY);
            staffChange.setOldStaffId(order.getServiceStaffId());

            order.setStaffChangeStatus(CommonDealStatus.APPLY);
            orderRepository.save(order);
        } else if (staffChange.getDealStatus() == CommonDealStatus.AGREE) {
            // 后台操作，直接成功
            order.setStaffChangeStatus(CommonDealStatus.AGREE);
            order.setServiceStaffId(staffChange.getNewStaffId());
            orderRepository.save(order);

            ServiceStaff oldStaff = serviceStaffRepository.findById(staffChange.getOldStaffId()).orElse(null);
            if (Objects.nonNull(oldStaff)) {
                oldStaff.setServiceOrderCount(oldStaff.getServiceOrderCount() - 1);
                if (oldStaff.getServiceOrderCount() <= 0) {
                    oldStaff.setServiceOrderCount(0);
                    oldStaff.setServiceStatus(ServiceStatus.FREE);
                }
                serviceStaffRepository.save(oldStaff);
            }

            ServiceStaff newStaff = serviceStaffRepository.findById(staffChange.getNewStaffId()).orElse(null);
            if (Objects.nonNull(newStaff)) {
                newStaff.setServiceStatus(ServiceStatus.IN_SERVICE);
                newStaff.setServiceOrderCount(newStaff.getServiceOrderCount() + 1);
                serviceStaffRepository.save(newStaff);
            }
        }
        orderStaffChangeRepository.save(staffChange);
    }

    @Override
    public List<OrderStaffChange> queryUserStaffChange(String userId) {
        return orderStaffChangeRepository.findByUserId(userId);
    }

    @Override
    public void serviceStart(String orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (Objects.isNull(order) || order.getStatus() != OrderStatus.ORDER_ASSIGN) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), ORDER_INFO_NOT_CORRECT));
        }
        ServiceStaff serviceStaff = serviceStaffRepository.findById(order.getServiceStaffId()).orElse(null);
        if (Objects.isNull(serviceStaff)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), ORDER_INFO_NOT_CORRECT));
        }

        order.setStatus(OrderStatus.ORDER_PROCESSING);
        orderRepository.save(order);

        serviceStaff.setServiceStatus(ServiceStatus.IN_SERVICE);
        serviceStaffRepository.save(serviceStaff);
    }

    @Override
    public void orderCompleted(String orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (Objects.isNull(order)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), ORDER_INFO_NOT_CORRECT));
        }
        if (order.getStatus() != OrderStatus.ORDER_PROCESSING) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), "订单不处于服务中状态，不能完成"));
        }
        if (Objects.nonNull(order.getRefundStatus()) && order.getRefundStatus() == CommonDealStatus.APPLY) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), "尚有退款未处理，不能完成订单"));
        }
//        if (order.getPayWay() != PayWay.PAY_ONLINE_WITH_WECHAT && Objects.isNull(order.getPayTime())) {
//            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), "订单尚未支付，不能完成订单"));
//        }

        // 修改订单
        order.setCloseTime(new Date());
        order.setStatus(OrderStatus.ORDER_FINISH);
        orderRepository.save(order);

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

        ServiceStaff staff = null;
        if (StringUtils.isNotBlank(order.getServiceStaffId())) {
            staff = serviceStaffRepository.findById(order.getServiceStaffId()).orElse(null);
            dealStaffBiz(orderId, order, staff);
        }
        if (StringUtils.isNotBlank(order.getNurseId())) {
            ServiceStaff nurseStaff = serviceStaffRepository.findById(order.getServiceStaffId()).orElse(null);
            dealStaffBiz(orderId, order, nurseStaff);
        }

        if (Objects.nonNull(staff) && Objects.nonNull(userProfile) && StringUtils.isNotBlank(userProfile.getIdpId())) {
            String redirectUrl = T_MSG_REDIRECT_URL + "#/orderDetail?id=" + orderId;
            Map<String, String> extension = new HashMap<>();
            extension.put("first", "服务已结束，请您评价！");
            extension.put("keyword1", staff.getUserName());
            extension.put("keyword2", order.getCareType().getName());
            extension.put("keyword3", "一家依护");
            extension.put("keyword4", DateUtil.formatDate(new Date(), "yyyy-MM-dd"));
            extension.put("remark", "点击查看详情");
            wechatService.sendTemplateMsg(userProfile.getIdpId(), Constant.WechatTemplate.T_ORDER_COMPLETED, redirectUrl, extension);
        }
    }

    private void dealStaffBiz(String orderId, Order order, ServiceStaff staff) {
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
            List<CommonDealStatus> dealStatuses = new ArrayList<>();
            dealStatuses.add(CommonDealStatus.AGREE);
            dealStatuses.add(CommonDealStatus.COMPLETED);

            StringBuilder sqlBuilder = new StringBuilder("select CAST(COALESCE(sum(t.REFUND_FEE), 0) AS DECIMAL(18,2)) ");
            sqlBuilder.append("from T_ORDER_REFUND t where 1=1 ");
            sqlBuilder.append("and t.ORDER_ID = ? ");
            sqlBuilder.append("and t.DEAL_STATUS in (?,?)");

            double refundFee = jdbcTemplate.queryForObject(sqlBuilder.toString(),
                    new Object[]{orderId, CommonDealStatus.AGREE.getIndex(), CommonDealStatus.COMPLETED.getIndex()}, Double.class);

            double realFee = BigDecimalUtil.sub(order.getTotalFee(), refundFee);
            if (realFee <= 0) {
                realFee = 0.00D;
            }
            double realIncome = BigDecimalUtil.mul(realFee, 0.6);
            if (staff.getServiceStaffType() == ServiceStaffType.NURSE) {
                realIncome = BigDecimalUtil.mul(realFee, 0.2);
            }

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
            staffIncome.setOrderId(orderId);
            staffIncome.setOrderTotalFee(order.getTotalFee());
            staffIncome.setStaffId(order.getServiceStaffId());
            staffIncome.setCurrentBalance(userWallet.getBalance());
            staffIncome.setIncomeType(IncomeType.INCOME);
            staffIncome.setIncomeRemark(order.getCareType().getName() + "结算");
            staffIncomeRepository.save(staffIncome);
        }
    }

    @Override
    public void editUserOrder(EditOrderRequestVo requestVo) {
        Order order = orderRepository.findById(requestVo.getOrderId()).orElse(null);
        if (Objects.nonNull(order)) {
            order.setTotalFee(requestVo.getTotalFee());
            orderRepository.save(order);
        }

        OrderExt orderExt = orderExtRepository.findByOrderId(requestVo.getOrderId());
        if (Objects.nonNull(orderExt)) {
            orderExt.setServiceDuration(requestVo.getServiceDuration());
            orderExt.setServiceStartTime(new Date(requestVo.getServiceStartDate()));
            orderExt.setServiceEndDate(new Date(requestVo.getServiceEndDate()));
            orderExtRepository.save(orderExt);
        }
    }

    @Override
    public Page<Order> queryOrderStatisticForPage(PageRequest pageRequest, String careTypes, String hospitalAddress, Date maxDate, Date minDate) {
        List<String> orderIds = null;
        if (StringUtils.isNotBlank(hospitalAddress)) {
            orderIds = jdbcTemplate.queryForList("select t.ORDER_ID from T_ORDER_EXT t where INSTR(t.HOSPITAL_ADDRESS, ?) > 0", new Object[]{hospitalAddress.trim()}, String.class);
            if (CollectionUtils.isEmpty(orderIds)) {
                return (PageImpl) new PageImpl<>(new ArrayList<>(), pageRequest, 0);
            }
        }

        BooleanBuilder builder = new BooleanBuilder();
        QOrder qOrder = QOrder.order;

        if (StringUtils.isNotBlank(careTypes)) {
            String[] careTypeArr = careTypes.split(",");
            ArrayList<CareType> careTypeList = new ArrayList<>();
            for (String careType : careTypeArr) {
                if (StringUtils.isNotBlank(careType)) {
                    careTypeList.add(CareType.valueOf(careType));
                }
            }
            builder.and(qOrder.careType.in(careTypeList));
        }
        if (Objects.nonNull(maxDate)) {
            builder.and(qOrder.orderTime.lt(maxDate));
        }
        if (Objects.nonNull(minDate)) {
            builder.and(qOrder.orderTime.gt(minDate));
        }
        if (CollectionUtils.isNotEmpty(orderIds)) {
            builder.and(qOrder.id.in(orderIds));
        }

        Page<Order> page = orderRepository.findAll(builder, pageRequest);
        if (CollectionUtils.isNotEmpty(page.getContent())) {
            List<String> staffIds = new ArrayList<>();
            List<String> userIds = new ArrayList<>();
            List<String> resultOrderIds = new ArrayList<>();

            page.getContent().forEach(order -> {
                resultOrderIds.add(order.getId());
                if (StringUtils.isNotBlank(order.getServiceStaffId())) {
                    staffIds.add(order.getServiceStaffId());
                }
                if (StringUtils.isNotBlank(order.getOrderUserId())) {
                    userIds.add(order.getOrderUserId());
                }
            });

            Map<String, ServiceStaff> staffMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(staffIds)) {
                List<ServiceStaff> serviceStaffList = serviceStaffRepository.findByIds(staffIds);
                staffMap = serviceStaffList.stream().collect(Collectors.toMap(ServiceStaff::getId, Function.identity()));
            }

            Map<String, UserProfile> userMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(userIds)) {
                List<UserProfile> userProfileList = userProfileRepository.findByIds(userIds);
                userMap = userProfileList.stream().collect(Collectors.toMap(UserProfile::getId, Function.identity()));
            }

            Map<String, OrderExt> orderExtMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(resultOrderIds)) {
                List<OrderExt> orderExtList = orderExtRepository.findByOrderIds(resultOrderIds);
                orderExtMap = orderExtList.stream().collect(Collectors.toMap(OrderExt::getOrderId, Function.identity()));
            }

            Map<String, ServiceStaff> finalStaffMap = staffMap;
            Map<String, OrderExt> finalOrderExtMap = orderExtMap;
            Map<String, UserProfile> finalUserMap = userMap;
            page.getContent().forEach(order -> {
                if (StringUtils.isNotBlank(order.getServiceStaffId())) {
                    order.setServiceStaff(finalStaffMap.get(order.getServiceStaffId()));
                }
                if (StringUtils.isNotBlank(order.getOrderUserId())) {
                    order.setUserProfile(finalUserMap.get(order.getOrderUserId()));
                }
                order.setOrderExt(finalOrderExtMap.get(order.getId()));
            });
        }

        return page;
    }

    @Override
    public List<JSVo> queryOrderJSReport(PageRequest pageRequest, String careTypes, String hospitalAddress, Date maxDate, Date minDate) {
        if (Objects.isNull(minDate)) {
            minDate = DateUtil.dayBegin("1970-01-01");
        }
        if (Objects.isNull(maxDate)) {
            maxDate = new Date();
        }

        StringBuilder sb = new StringBuilder("select DATE_FORMAT(t.ORDER_TIME,'%Y-%m-%d') as orderTime, count(t.ID) as orderCount, CAST(COALESCE(sum(t.TOTAL_FEE), 0) AS DECIMAL(18,2)) as totalFee,\n" +
                " sum(if(t.PAY_WAY=10, 1, 0)) as wechatPayCount, sum(if(t.PAY_WAY=20, 1, 0)) as cashPayCount,  sum(if(t.PAY_WAY=21, 1, 0)) as posPayCount,sum(if(t.PAY_WAY=22, 1, 0)) as alipayPayCount,sum(if(t.PAY_WAY=24, 1, 0)) as offlineWechatPayCount\n" +
                " from T_ORDER t \n" +
                " where t.STATUS=4 and t.pay_Time is not null and t.ORDER_TIME>=? and t.ORDER_TIME<=? ");

        List<String> orderIds = null;
        if (StringUtils.isNotBlank(hospitalAddress)) {
            orderIds = jdbcTemplate.queryForList("select t.ORDER_ID from T_ORDER_EXT t where INSTR(t.HOSPITAL_ADDRESS, ?) > 0", new Object[]{hospitalAddress.trim()}, String.class);
            if (CollectionUtils.isEmpty(orderIds)) {
                return new ArrayList<>();
            }
        }
        if (CollectionUtils.isNotEmpty(orderIds)) {
            sb.append(" and t.ID in (");
            for (int i = 0; i < orderIds.size(); i++) {
                sb.append("'" + orderIds.get(i) + "'");
                if (i != orderIds.size() - 1) {
                    sb.append(",");
                }
            }
            sb.append(")");
        }

        StringBuilder careTypeName = new StringBuilder();
        if (StringUtils.isNotBlank(careTypes)) {
            String[] careTypeArr = careTypes.split(",");
            ArrayList<CareType> careTypeList = new ArrayList<>();
            for (String careType : careTypeArr) {
                if (StringUtils.isNotBlank(careType)) {
                    careTypeList.add(CareType.valueOf(careType));
                    careTypeName.append(CareType.valueOf(careType).getName() + " ");
                }
            }
            if (CollectionUtils.isNotEmpty(careTypeList)) {
                sb.append(" and t.CARE_TYPE in (");
                for (int i = 0; i < careTypeList.size(); i++) {
                    sb.append(careTypeList.get(i).getIndex());
                    if (i != careTypeList.size() - 1) {
                        sb.append(",");
                    }
                }
                sb.append(")");
            }
        } else {
            careTypeName.append(CareType.HOME_CARE.getName()).append(" ")
                    .append(CareType.HOSPITAL_CARE.getName()).append(" ")
                    .append(CareType.REHABILITATION.getName());
        }
        sb.append("  group by DATE_FORMAT(t.ORDER_TIME,'%Y-%m-%d') order by orderTime desc");

        List<JSVo> jsVoList = jdbcTemplate.query(sb.toString(), new Object[]{minDate, maxDate}, new JSVo());

        jsVoList.forEach(jsVo -> jsVo.setCareType(careTypeName.toString()));
        return jsVoList;
    }

    @Override
    public List<QuickOrderReport> queryQuickOrderReport(Set<String> hospitalNameSet, Date maxDate, Date minDate, Date paymaxDate, Date payminDate, String hospitalDepartment, String groupType) {
        int queryParamLength = 0;
        if (Objects.isNull(minDate)) {
            minDate = DateUtil.dayBegin("1970-01-01");
            queryParamLength++;
        }
        if (Objects.isNull(maxDate)) {
            maxDate = new Date();
            queryParamLength++;
        }
        if (Objects.nonNull(payminDate)) {
            queryParamLength++;
        }
        if (Objects.nonNull(paymaxDate)) {
            queryParamLength++;
        }
        if (StringUtils.isNoneBlank(hospitalDepartment)) {
            queryParamLength++;
        }

        int queryParamIndex = 0;
        Object[] queryParams = new Object[queryParamLength];

        StringBuilder sb = new StringBuilder();
        if (StringUtils.equals("hospital", groupType)) {
            sb.append("select t.HOSPITAL_NAME as title,  ");
        } else {
            sb.append("select t.HOSPITAL_DEPARTMENT as title,  ");
        }

        sb.append(
                " CAST(COALESCE(sum(t.TOTAL_FEE), 0) AS DECIMAL(18,2)) as totalFee,   " +
                " CAST(COALESCE(sum(t.REFUND_FEE), 0) AS DECIMAL(18,2)) as refundFee,  " +
                " CAST(COALESCE(sum(if(t.SERVICE_SCOPE=0,t.TOTAL_FEE,0)), 0) AS DECIMAL(18,2)) as totalInFee, " +
                " CAST(COALESCE(sum(if(t.SERVICE_SCOPE=0 and t.SERVICE_MODE=0,t.TOTAL_FEE,0)), 0) AS DECIMAL(18,2)) as inOneToOne, " +
                " CAST(COALESCE(sum(if(t.SERVICE_SCOPE=0 and t.SERVICE_MODE=1,t.TOTAL_FEE,0)), 0) AS DECIMAL(18,2)) as inOneToMany, " +
                " CAST(COALESCE(sum(if(t.SERVICE_SCOPE=0 and t.SERVICE_MODE=2,t.TOTAL_FEE,0)), 0) AS DECIMAL(18,2)) as inManyToOne,  " +
                " CAST(COALESCE(sum(if(t.SERVICE_SCOPE=1,t.TOTAL_FEE,0)), 0) AS DECIMAL(18,2)) as totalOutFee, " +
                " CAST(COALESCE(sum(if(t.SERVICE_SCOPE=1 and t.SERVICE_MODE=0,t.TOTAL_FEE,0)), 0) AS DECIMAL(18,2)) as outOneToOne, " +
                " CAST(COALESCE(sum(if(t.SERVICE_SCOPE=1 and t.SERVICE_MODE=1,t.TOTAL_FEE,0)), 0) AS DECIMAL(18,2)) as outOneToMany, " +
                " CAST(COALESCE(sum(if(t.SERVICE_SCOPE=1 and t.SERVICE_MODE=2,t.TOTAL_FEE,0)), 0) AS DECIMAL(18,2)) as outManyToOne " +
                " from (\n" +
                " SELECT o.id, o.TOTAL_FEE, r.REFUND_FEE, o.HOSPITAL_NAME, o.HOSPITAL_DEPARTMENT, o.SERVICE_SCOPE, o.SERVICE_MODE  " +
                " FROM T_ORDER o \n" +
                " LEFT JOIN T_ORDER_REFUND r on o.id=r.ORDER_ID and r.DEAL_STATUS=2  " +
                " WHERE o.ORDER_SOURCE='WECHAT_QUICK' and o.PAY_TIME is not null and o.ORDER_TIME>=? and o.ORDER_TIME<=? ");

        queryParams[queryParamIndex++] = minDate;
        queryParams[queryParamIndex++] = maxDate;
        if (Objects.nonNull(payminDate)) {
            sb.append(" and o.PAY_TIME>=? ");
            queryParams[queryParamIndex++] = payminDate;
        }
        if (Objects.nonNull(paymaxDate)) {
            sb.append("  and o.PAY_TIME<=? ");
            queryParams[queryParamIndex++] = paymaxDate;
        }
        if (StringUtils.isNoneBlank(hospitalDepartment)) {
            sb.append("  and o.HOSPITAL_DEPARTMENT=? ");
            queryParams[queryParamIndex++] = hospitalDepartment;
        }

        if (CollectionUtils.isNotEmpty(hospitalNameSet)) {
            sb.append(" and o.HOSPITAL_NAME in (");
            int loopCount = 1;
            for (String hospitalName : hospitalNameSet) {
                sb.append("'").append(hospitalName).append("'");
                if (loopCount != hospitalNameSet.size()) {
                    sb.append(",");
                }
                loopCount++;
            }
            sb.append(") ");
        }

        if (StringUtils.equals("hospital", groupType)) {
            sb.append(" ) t GROUP BY t.HOSPITAL_NAME ");
        } else {
            sb.append(" ) t GROUP BY t.HOSPITAL_DEPARTMENT ");
        }

        List<QuickOrderReport> jsVoList = jdbcTemplate.query(sb.toString(), queryParams, new QuickOrderReport());

        return jsVoList;
    }

    @Override
    public List<HospitalBizVo> queryHospitalBizReport(PageRequest pageRequest, String serviceStaffId, Date maxDate, Date minDate) {
        if (Objects.isNull(minDate)) {
            minDate = DateUtil.dayBegin("1970-01-01");
        }
        if (Objects.isNull(maxDate)) {
            maxDate = new Date();
        }

        StringBuilder sb = new StringBuilder("select DATE_FORMAT(t.ORDER_TIME,'%Y-%m-%d') as orderTime, t.SERVICE_STAFF_ID as staffId, count(t.ID) as orderCount, CAST(COALESCE(sum(t.TOTAL_FEE), 0) AS DECIMAL(18,2)) as totalFee\n" +
                " from T_ORDER t \n" +
                " where t.STATUS=4 and t.pay_Time is not null and t.ORDER_TIME>=? and t.ORDER_TIME<=? and t.CARE_TYPE in ('1') ");

        if (StringUtils.isNotBlank(serviceStaffId)) {
            sb.append(" and t.SERVICE_STAFF_ID='").append(serviceStaffId).append("'");
        }
        sb.append("   group by DATE_FORMAT(t.ORDER_TIME,'%Y-%m-%d'), t.SERVICE_STAFF_ID order by orderTime desc");

        List<HospitalBizVo> jsVoList = jdbcTemplate.query(sb.toString(), new Object[]{minDate, maxDate}, new HospitalBizVo());

        Map<String, String> staffMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(jsVoList)) {
            List<String> staffIds = jsVoList.stream().map(hospitalBizVo -> hospitalBizVo.getStaffId()).collect(Collectors.toList());
            List<ServiceStaff> staffList = serviceStaffRepository.findByIds(staffIds);
            staffMap = staffList.stream().collect(Collectors.toMap(ServiceStaff::getId, ServiceStaff::getUserName));
        }

        Map<String, String> finalStaffMap = staffMap;
        jsVoList.forEach(jsVo -> jsVo.setStaffName(finalStaffMap.get(jsVo.getStaffId())));
        return jsVoList;
    }

    @Override
    public List<Order> queryHospitalOrder(String orderTime, String staffId) {
        BooleanBuilder builder = new BooleanBuilder();
        QOrder qOrder = QOrder.order;

        Date minDate = DateUtil.dayBegin(orderTime);
        Date maxDate = DateUtil.dayEnd(orderTime);
        if (Objects.nonNull(maxDate)) {
            builder.and(qOrder.orderTime.lt(maxDate));
        }
        if (Objects.nonNull(minDate)) {
            builder.and(qOrder.orderTime.gt(minDate));
        }
        if (Objects.nonNull(staffId)) {
            builder.and(qOrder.serviceStaffId.eq(staffId));
        }
        builder.and(qOrder.status.eq(OrderStatus.ORDER_FINISH));
        builder.and(qOrder.payTime.isNotNull());

        Sort sort = Sort.by(Sort.Direction.DESC, "orderTime");

        Iterable<Order> iterable = orderRepository.findAll(builder, sort);

        List<Order> orderList = new ArrayList<>();

        List<String> staffIds = new ArrayList<>();
        List<String> orderIds = new ArrayList<>();
        List<String> userIds = new ArrayList<>();
        iterable.forEach(order -> {
            if (StringUtils.isNotBlank(order.getServiceStaffId())) {
                staffIds.add(order.getServiceStaffId());
            }
            orderIds.add(order.getId());
            userIds.add(order.getOrderUserId());
        });

        Map<String, ServiceStaff> staffMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(staffIds)) {
            List<ServiceStaff> serviceStaffList = serviceStaffRepository.findByIds(staffIds);
            staffMap = serviceStaffList.stream().collect(Collectors.toMap(ServiceStaff::getId, Function.identity()));
        }

        Map<String, UserProfile> userProfileMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(userIds)) {
            List<UserProfile> userProfileList = userProfileRepository.findByIds(userIds);
            userProfileMap = userProfileList.stream().collect(Collectors.toMap(UserProfile::getId, Function.identity()));
        }

        Map<String, OrderExt> orderExtMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(orderIds)) {
            List<OrderExt> adminList = orderExtRepository.findByOrderIds(orderIds);
            orderExtMap = adminList.stream().collect(Collectors.toMap(OrderExt::getOrderId, Function.identity()));
        }
        Map<String, ServiceStaff> finalStaffMap = staffMap;
        Map<String, OrderExt> finalOrderExtMap = orderExtMap;
        Map<String, UserProfile> finalUserProfileMap = userProfileMap;
        iterable.forEach(order -> {
            if (StringUtils.isNotBlank(order.getServiceStaffId())) {
                order.setServiceStaff(finalStaffMap.get(order.getServiceStaffId()));
            }
            order.setOrderExt(finalOrderExtMap.get(order.getId()));
            order.setUserProfile(finalUserProfileMap.get(order.getOrderUserId()));
            orderList.add(order);
        });
        return orderList;
    }

    @Override
    public List<Order> queryQuickOrder(Date minDate, Date maxDate, String hospitalName, Date paymaxDate, Date payminDate, String queryType, String serviceScope, String department) {
        BooleanBuilder builder = new BooleanBuilder();
        QOrder qOrder = QOrder.order;

        if (Objects.nonNull(maxDate)) {
            builder.and(qOrder.orderTime.lt(maxDate));
        }
        if (Objects.nonNull(minDate)) {
            builder.and(qOrder.orderTime.gt(minDate));
        }

        if (Objects.nonNull(paymaxDate)) {
            builder.and(qOrder.payTime.lt(paymaxDate));
        }
        if (Objects.nonNull(payminDate)) {
            builder.and(qOrder.payTime.gt(payminDate));
        }
        if (StringUtils.isNotBlank(hospitalName)) {
            builder.and(qOrder.hospitalName.eq(hospitalName));
        }
        if (StringUtils.isNotBlank(department)) {
            builder.and(qOrder.hospitalDepartment.eq(department));
        }
        if (StringUtils.equals("REFUND", queryType)) {
            builder.and(qOrder.refundStatus.eq(CommonDealStatus.COMPLETED));
        } else if (StringUtils.equals("NORMAL_WITH_PAID", queryType)) {
            builder.and(qOrder.payTime.isNotNull());
        }
        if (StringUtils.isNotBlank(serviceScope)) {
            if (StringUtils.equals(ServiceScope.INSIDE.toString(), serviceScope.trim())) {
                builder.and(qOrder.serviceScope.eq(ServiceScope.INSIDE));
            } else if (StringUtils.equals(ServiceScope.OUTSIDE.toString(), serviceScope.trim())) {
                builder.and(qOrder.serviceScope.eq(ServiceScope.OUTSIDE));
            }
        }
        builder.and(qOrder.orderSource.eq("WECHAT_QUICK"));

        Sort sort = Sort.by(Sort.Direction.DESC, "orderTime");

        Iterable<Order> iterable = orderRepository.findAll(builder, sort);

        List<Order> orderList = new ArrayList<>();

        List<String> staffIds = new ArrayList<>();
        List<String> orderIds = new ArrayList<>();
        List<String> userIds = new ArrayList<>();
        iterable.forEach(order -> {
            if (StringUtils.isNotBlank(order.getServiceStaffId())) {
                staffIds.add(order.getServiceStaffId());
            }
            if (StringUtils.isNotBlank(order.getServiceAdminId())) {
                staffIds.add(order.getServiceAdminId());
            }
            orderIds.add(order.getId());
            userIds.add(order.getOrderUserId());
        });

        Map<String, ServiceStaff> staffMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(staffIds)) {
            List<ServiceStaff> serviceStaffList = serviceStaffRepository.findByIds(staffIds);
            staffMap = serviceStaffList.stream().collect(Collectors.toMap(ServiceStaff::getId, Function.identity()));
        }

        Map<String, UserProfile> userProfileMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(userIds)) {
            List<UserProfile> userProfileList = userProfileRepository.findByIds(userIds);
            userProfileMap = userProfileList.stream().collect(Collectors.toMap(UserProfile::getId, Function.identity()));
        }

        Map<String, OrderExt> orderExtMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(orderIds)) {
            List<OrderExt> adminList = orderExtRepository.findByOrderIds(orderIds);
            orderExtMap = adminList.stream().collect(Collectors.toMap(OrderExt::getOrderId, Function.identity()));
        }

        Map<String, OrderRefund> orderRefundMap = new HashMap<>();
        if (StringUtils.equals("REFUND", queryType) && CollectionUtils.isNotEmpty(orderIds)) {
            List<OrderRefund> orderRefundList = orderRefundRepository.findByOrderIds(orderIds, CommonDealStatus.COMPLETED);
            orderRefundMap = orderRefundList.stream().collect(Collectors.toMap(OrderRefund::getOrderId, Function.identity()));
        }
        Map<String, ServiceStaff> finalStaffMap = staffMap;
        Map<String, OrderExt> finalOrderExtMap = orderExtMap;
        Map<String, UserProfile> finalUserProfileMap = userProfileMap;
        Map<String, OrderRefund> finalOrderRefundMap = orderRefundMap;
        iterable.forEach(order -> {
            if (StringUtils.isNotBlank(order.getServiceStaffId())) {
                order.setServiceStaff(finalStaffMap.get(order.getServiceStaffId()));
            }
            if (StringUtils.isNotBlank(order.getServiceAdminId())) {
                order.setAdminStaff(finalStaffMap.get(order.getServiceAdminId()));
            }
            order.setOrderExt(finalOrderExtMap.get(order.getId()));
            order.setUserProfile(finalUserProfileMap.get(order.getOrderUserId()));
            order.setOrderRefund(finalOrderRefundMap.get(order.getId()));
            orderList.add(order);
        });
        return orderList;
    }

    @Override
    public Page<Order> queryQuickOrderForPage(PageRequest pageRequest, Date minDate, Date maxDate, String hospitalName, Date paymaxDate, Date payminDate, String queryType, String serviceScope) {
        BooleanBuilder builder = new BooleanBuilder();
        QOrder qOrder = QOrder.order;

        if (Objects.nonNull(maxDate)) {
            builder.and(qOrder.orderTime.lt(maxDate));
        }
        if (Objects.nonNull(minDate)) {
            builder.and(qOrder.orderTime.gt(minDate));
        }

        if (Objects.nonNull(paymaxDate)) {
            builder.and(qOrder.payTime.lt(paymaxDate));
        }
        if (Objects.nonNull(payminDate)) {
            builder.and(qOrder.payTime.gt(payminDate));
        }
        if (Objects.nonNull(hospitalName)) {
            builder.and(qOrder.hospitalName.eq(hospitalName));
        }
        if (StringUtils.equals("REFUND", queryType)) {
            builder.and(qOrder.refundStatus.eq(CommonDealStatus.COMPLETED));
        } else if (StringUtils.equals("NORMAL_WITH_PAID", queryType)) {
            builder.and(qOrder.payTime.isNotNull());
        }
        if (StringUtils.isNotBlank(serviceScope)) {
            if (StringUtils.equals(ServiceScope.INSIDE.toString(), serviceScope.trim())) {
                builder.and(qOrder.serviceScope.eq(ServiceScope.INSIDE));
            } else if (StringUtils.equals(ServiceScope.OUTSIDE.toString(), serviceScope.trim())) {
                builder.and(qOrder.serviceScope.eq(ServiceScope.OUTSIDE));
            }
        }
        builder.and(qOrder.orderSource.eq("WECHAT_QUICK"));

//        Sort sort = Sort.by(Sort.Direction.DESC, "orderTime");

        Page<Order> page = orderRepository.findAll(builder, pageRequest);
//        Iterable<Order> iterable = orderRepository.findAll(builder, sort);

        List<Order> orderList = new ArrayList<>();

        List<String> staffIds = new ArrayList<>();
        List<String> orderIds = new ArrayList<>();
        List<String> userIds = new ArrayList<>();
        page.getContent().forEach(order -> {
            if (StringUtils.isNotBlank(order.getServiceStaffId())) {
                staffIds.add(order.getServiceStaffId());
            }
            if (StringUtils.isNotBlank(order.getServiceAdminId())) {
                staffIds.add(order.getServiceAdminId());
            }
            orderIds.add(order.getId());
            userIds.add(order.getOrderUserId());
        });

        Map<String, ServiceStaff> staffMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(staffIds)) {
            List<ServiceStaff> serviceStaffList = serviceStaffRepository.findByIds(staffIds);
            staffMap = serviceStaffList.stream().collect(Collectors.toMap(ServiceStaff::getId, Function.identity()));
        }

        Map<String, UserProfile> userProfileMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(userIds)) {
            List<UserProfile> userProfileList = userProfileRepository.findByIds(userIds);
            userProfileMap = userProfileList.stream().collect(Collectors.toMap(UserProfile::getId, Function.identity()));
        }

        Map<String, OrderExt> orderExtMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(orderIds)) {
            List<OrderExt> adminList = orderExtRepository.findByOrderIds(orderIds);
            orderExtMap = adminList.stream().collect(Collectors.toMap(OrderExt::getOrderId, Function.identity()));
        }

        Map<String, OrderRefund> orderRefundMap = new HashMap<>();
        if (StringUtils.equals("REFUND", queryType) && CollectionUtils.isNotEmpty(orderIds)) {
            List<OrderRefund> orderRefundList = orderRefundRepository.findByOrderIds(orderIds, CommonDealStatus.COMPLETED);
            orderRefundMap = orderRefundList.stream().collect(Collectors.toMap(OrderRefund::getOrderId, Function.identity()));
        }
        Map<String, ServiceStaff> finalStaffMap = staffMap;
        Map<String, OrderExt> finalOrderExtMap = orderExtMap;
        Map<String, UserProfile> finalUserProfileMap = userProfileMap;
        Map<String, OrderRefund> finalOrderRefundMap = orderRefundMap;
        page.getContent().forEach(order -> {
            if (StringUtils.isNotBlank(order.getServiceStaffId())) {
                order.setServiceStaff(finalStaffMap.get(order.getServiceStaffId()));
            }
            if (StringUtils.isNotBlank(order.getServiceAdminId())) {
                order.setAdminStaff(finalStaffMap.get(order.getServiceAdminId()));
            }
            order.setOrderExt(finalOrderExtMap.get(order.getId()));
            order.setUserProfile(finalUserProfileMap.get(order.getOrderUserId()));
            order.setOrderRefund(finalOrderRefundMap.get(order.getId()));
            orderList.add(order);
        });
        return page;
    }
}
