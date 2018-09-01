package com.baymin.springboot.store.dao.impl;

import com.baymin.springboot.common.constant.RequestConstant;
import com.baymin.springboot.store.dao.IOrderDao;
import com.baymin.springboot.store.entity.Order;
import com.baymin.springboot.store.entity.OrderExt;
import com.baymin.springboot.store.entity.QOrder;
import com.baymin.springboot.store.entity.QOrderExt;
import com.baymin.springboot.store.enumconstant.OrderStatus;
import com.baymin.springboot.store.repository.IOrderExtRepository;
import com.baymin.springboot.store.repository.IOrderRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public Order saveUserOrder(Order order, OrderExt orderExt) {
        order = orderRepository.save(order);

        orderExt.setOrderId(order.getId());
        orderExtRepository.save(orderExt);

        return order;
    }

    @Override
    public List<Order> queryUserOrder(String userId, String status, String ownerType) {
        QOrder qOrder = QOrder.order;

        BooleanExpression predicate;
        if (StringUtils.equals("user", ownerType)) {
            predicate = qOrder.id.eq(userId);
        } else {
            predicate = qOrder.serviceStaffId.eq(userId);
        }
        if (StringUtils.equals(RequestConstant.ORDER_INIT, status)) {
            predicate.and(qOrder.status.eq(OrderStatus.ORDER_UN_PAY));
        } else if (StringUtils.equals(RequestConstant.ORDER_PROCESSING, status)) {
            predicate.and(qOrder.status.eq(OrderStatus.ORDER_PAYED)
                    .or(qOrder.status.eq(OrderStatus.ORDER_PROCESSING)));
        } else {
            predicate.and(qOrder.status.eq(OrderStatus.ORDER_FINISH));
        }

        JPAQuery<Order> jpaQuery = queryFactory.select(qOrder)
                .from(qOrder)
                .where(predicate);

        return jpaQuery.fetch();
    }

    @Override
    public Map<String, Object> queryOrderDetail(String orderId) {
        QOrder qOrder = QOrder.order;
        QOrderExt qOrderExt = QOrderExt.orderExt;

        Order order = queryFactory.select(qOrder)
                .from(qOrder)
                .where(qOrder.id.eq(orderId))
                .fetchOne();

        OrderExt orderExt = queryFactory.select(qOrderExt)
                .from(qOrderExt)
                .where(qOrderExt.orderId.eq(orderId))
                .fetchOne();

        Map<String, Object> reMap = new HashMap<>();
        reMap.put("order", order);
        reMap.put("orderExt", orderExt);
        return reMap;
    }
}
