package com.baymin.springboot.service.impl;

import com.baymin.springboot.common.util.DateUtil;
import com.baymin.springboot.service.IReportService;
import com.baymin.springboot.store.entity.QOrder;
import com.baymin.springboot.store.payload.report.PlatformOrderReport;
import com.baymin.springboot.store.repository.IOrderRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.Objects;

@Service
@Transactional
public class ReportServiceImpl implements IReportService {

    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public PlatformOrderReport queryPlatformOrderReport(Date minDate, Date maxDate) {
        if (Objects.isNull(minDate)) {
            minDate = DateUtil.dayBegin("1970-01-01");
        }
        if (Objects.isNull(maxDate)) {
            maxDate = new Date();
        }

        double orderTotalFee = jdbcTemplate.queryForObject("select CAST(COALESCE(sum(t.TOTAL_FEE), 0) AS DECIMAL(18,2)) " +
                "from T_ORDER t where  t.ORDER_TIME>=? and t.ORDER_TIME<=?", new Object[]{minDate, maxDate}, Double.class);

        QOrder qOrder = QOrder.order;
//        QOrderRefund qOrderRefund = QOrderRefund.orderRefund;

        BooleanBuilder builder = new BooleanBuilder();
        if (Objects.nonNull(minDate)) {
            builder.and(qOrder.orderTime.gt(minDate));
        }
        if (Objects.nonNull(maxDate)) {
            builder.and(qOrder.orderTime.lt(maxDate));
        }

        PlatformOrderReport report = new PlatformOrderReport();
        long validOrderCount = /*queryFactory.select(qOrder.id.count()).from(qOrder).where(builder).fetchOne()*/0L;
        double validOrderTotalFee = orderTotalFee;
        long invalidOrderCount = 0;
        double invalidOrderTotalFee = 0.00D;

        BooleanBuilder refundBuilder = new BooleanBuilder();
        if (Objects.nonNull(minDate)) {
            refundBuilder.and(qOrder.orderTime.gt(minDate));
        }
        if (Objects.nonNull(maxDate)) {
            refundBuilder.and(qOrder.orderTime.lt(maxDate));
        }
        refundBuilder.and(qOrder.refundStatus.isNotNull());
        double refundOrderTotalFee = /*queryFactory.select(qOrder.totalFee.sum()).from(qOrder).where(refundBuilder).fetchOne()*/0.0D;
        long refundOrderCount = /*queryFactory.select(qOrder.id.count()).from(qOrder).where(refundBuilder).fetchOne()*/0L;

        report.setOrderTotalFee(orderTotalFee);
        report.setValidOrderTotalFee(validOrderTotalFee);
        report.setValidOrderCount(validOrderCount);
        report.setInvalidOrderCount(invalidOrderCount);
        report.setInvalidOrderTotalFee(invalidOrderTotalFee);
        report.setRefundOrderCount(refundOrderCount);
        report.setRefundOrderTotalFee(refundOrderTotalFee);

        return report;
    }
}
