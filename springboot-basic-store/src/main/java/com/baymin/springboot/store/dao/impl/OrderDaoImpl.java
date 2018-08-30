package com.baymin.springboot.store.dao.impl;

import com.baymin.springboot.store.dao.IOrderDao;
import com.baymin.springboot.store.entity.Order;
import com.baymin.springboot.store.entity.OrderExt;
import com.baymin.springboot.store.entity.QOrder;
import com.baymin.springboot.store.repository.IOrderExtRepository;
import com.baymin.springboot.store.repository.IOrderRepository;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

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
    public List<Order> queryUserOrder(String userId, String status) {
        Predicate predicate = QOrder.order.id.eq(userId);
        JPAQuery<Order> jpaQuery = queryFactory.select(QOrder.order)
                .from(QOrder.order)
                .where(predicate);

        return jpaQuery.fetch();
    }
}
