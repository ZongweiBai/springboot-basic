package com.baymin.springboot.service.impl;

import com.baymin.springboot.common.util.DateUtil;
import com.baymin.springboot.service.IReportService;
import com.baymin.springboot.store.enumconstant.OrderStatus;
import com.baymin.springboot.store.payload.report.PlatformOrderReport;
import com.baymin.springboot.store.repository.IOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                        "from T_ORDER t where  t.ORDER_TIME>=? and t.ORDER_TIME<=?",
                new Object[]{minDate, maxDate}, Double.class);

        double validOrderTotalFee = jdbcTemplate.queryForObject("select CAST(COALESCE(sum(t.TOTAL_FEE), 0) AS DECIMAL(18,2)) " +
                        "from T_ORDER t where  t.ORDER_TIME>=? and t.ORDER_TIME<=? and t.STATUS in(?, ?, ?, ?, ?)",
                new Object[]{minDate, maxDate, OrderStatus.ORDER_UN_PAY, OrderStatus.ORDER_ASSIGN,
                        OrderStatus.ORDER_FINISH, OrderStatus.ORDER_PAYED, OrderStatus.ORDER_PROCESSING}, Double.class);

        int validOrderCount = jdbcTemplate.queryForObject("select COALESCE(count(t.ID), 0) " +
                        "from T_ORDER t where  t.ORDER_TIME>=? and t.ORDER_TIME<=? and t.STATUS in(?, ?, ?, ?, ?)",
                new Object[]{minDate, maxDate, OrderStatus.ORDER_UN_PAY, OrderStatus.ORDER_ASSIGN,
                        OrderStatus.ORDER_FINISH, OrderStatus.ORDER_PAYED, OrderStatus.ORDER_PROCESSING}, Integer.class);

        double invalidOrderTotalFee = jdbcTemplate.queryForObject("select CAST(COALESCE(sum(t.TOTAL_FEE), 0) AS DECIMAL(18,2)) " +
                        "from T_ORDER t where  t.ORDER_TIME>=? and t.ORDER_TIME<=? and t.STATUS in(?)",
                new Object[]{minDate, maxDate, OrderStatus.ORDER_CANCELED}, Double.class);

        int invalidOrderCount = jdbcTemplate.queryForObject("select COALESCE(count(t.ID), 0) " +
                        "from T_ORDER t where  t.ORDER_TIME>=? and t.ORDER_TIME<=? and t.STATUS in(?)",
                new Object[]{minDate, maxDate, OrderStatus.ORDER_CANCELED}, Integer.class);

        double refundOrderTotalFee = jdbcTemplate.queryForObject("select CAST(COALESCE(sum(t.TOTAL_FEE), 0) AS DECIMAL(18,2)) " +
                        "from T_ORDER t where  t.ORDER_TIME>=? and t.ORDER_TIME<=? and t.REFUND_STATUS IS NOT NULL ",
                new Object[]{minDate, maxDate}, Double.class);

        int refundOrderCount = jdbcTemplate.queryForObject("select COALESCE(count(t.ID), 0) " +
                        "from T_ORDER t where  t.ORDER_TIME>=? and t.ORDER_TIME<=? and t.REFUND_STATUS IS NOT NULL ",
                new Object[]{minDate, maxDate}, Integer.class);

        PlatformOrderReport report = new PlatformOrderReport();
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
