package com.baymin.springboot.store.dao.impl;

import com.baymin.springboot.common.constant.RequestConstant;
import com.baymin.springboot.store.dao.IOrderDao;
import com.baymin.springboot.store.entity.*;
import com.baymin.springboot.store.enumconstant.InvoiceStatus;
import com.baymin.springboot.store.enumconstant.OrderStatus;
import com.baymin.springboot.store.payload.OrderDetailVo;
import com.baymin.springboot.store.payload.ServiceProductVo;
import com.baymin.springboot.store.repository.IOrderExtRepository;
import com.baymin.springboot.store.repository.IOrderRepository;
import com.baymin.springboot.store.repository.IServiceStaffRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Repository
public class OrderDaoImpl implements IOrderDao {

    @Autowired
    @PersistenceContext
    private EntityManager entityManager;

    private JPAQueryFactory queryFactory;

    @PostConstruct
    public void init() {
        queryFactory = new JPAQueryFactory(entityManager);
    }

    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private IOrderExtRepository orderExtRepository;

    @Autowired
    private IServiceStaffRepository serviceStaffRepository;

    @Override
    public Order saveUserOrder(Order order, OrderExt orderExt) {
        order = orderRepository.save(order);

        orderExt.setOrderId(order.getId());
        orderExtRepository.save(orderExt);

        return order;
    }

    @Override
    public List<Order> queryUserOrder(String userId, String status, String ownerType, Date minDate, Date maxDate) {
        QOrder qOrder = QOrder.order;

        BooleanBuilder predicate = new BooleanBuilder();
        if (StringUtils.equals("user", ownerType)) {
            predicate.and(qOrder.orderUserId.eq(userId));
            if (StringUtils.equals(RequestConstant.ORDER_UNINVOCIED, status)) {
                predicate.and(qOrder.invoiceStatus.eq(InvoiceStatus.NOT_INVOICED));
                predicate.and(qOrder.payTime.isNotNull());
            } else {
                if (StringUtils.equals(RequestConstant.ORDER_INIT, status)) {
                    predicate.and(qOrder.status.eq(OrderStatus.ORDER_UN_PAY));
                } else if (StringUtils.equals(RequestConstant.ORDER_PROCESSING, status)) {
                    predicate.and(qOrder.status.in(OrderStatus.ORDER_PAYED, OrderStatus.ORDER_PROCESSING, OrderStatus.ORDER_ASSIGN));
                } else {
                    predicate.and(qOrder.status.eq(OrderStatus.ORDER_FINISH));
                }
            }
        } else if (StringUtils.equals("staff", ownerType)) {
            predicate.and(qOrder.serviceStaffId.eq(userId).or(qOrder.nurseId.eq(userId)));
            if (StringUtils.equals(RequestConstant.ORDER_INIT, status)) {
                predicate.and(qOrder.status.eq(OrderStatus.ORDER_ASSIGN));
            } else if (StringUtils.equals(RequestConstant.ORDER_PROCESSING, status)) {
                predicate.and(qOrder.status.eq(OrderStatus.ORDER_PROCESSING));
            } else {
                predicate.and(qOrder.status.eq(OrderStatus.ORDER_FINISH));
            }
        } else {
            predicate.and(qOrder.serviceAdminId.eq(userId));
            predicate.and(qOrder.orderSource.eq("WECHAT_QUICK"));
            if (Objects.nonNull(minDate)) {
                predicate.and(qOrder.orderTime.gt(minDate));
            }
            if (Objects.nonNull(maxDate)) {
                predicate.and(qOrder.orderTime.lt(maxDate));
            }
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "orderTime");

        Iterable<Order> iterable = orderRepository.findAll(predicate, sort);
        List<Order> orderList = new ArrayList<>();
        iterable.forEach(orderList::add);
        return orderList;
    }

    @Override
    public OrderDetailVo queryOrderDetail(String orderId) {
        OrderDetailVo detail = new OrderDetailVo();

        QOrder qOrder = QOrder.order;
        QOrderExt qOrderExt = QOrderExt.orderExt;
        QServiceProduct qProduct = QServiceProduct.serviceProduct;

        Order order = queryFactory.select(qOrder)
                .from(qOrder)
                .where(qOrder.id.eq(orderId))
                .fetchOne();

        OrderExt orderExt = queryFactory.select(qOrderExt)
                .from(qOrderExt)
                .where(qOrderExt.orderId.eq(orderId))
                .fetchOne();

        if (Objects.nonNull(order) && StringUtils.isNotBlank(order.getServiceProductId())) {
            ServiceProduct product = queryFactory.select(qProduct)
                    .from(qProduct)
                    .where(qProduct.id.eq(order.getServiceProductId()))
                    .fetchOne();
            detail.setServiceProduct(new ServiceProductVo(product, null));
        }

        ServiceStaff staff = null;
        if (StringUtils.isNotBlank(order.getServiceStaffId())) {
            staff = serviceStaffRepository.findById(order.getServiceStaffId()).orElse(null);
        }

        detail.setOrder(order);
        detail.setOrderExt(orderExt);
        detail.setServiceStaff(staff);
        return detail;
    }
}
