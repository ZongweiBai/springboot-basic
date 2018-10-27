package com.baymin.springboot.service.impl;

import com.baymin.springboot.common.util.DateUtil;
import com.baymin.springboot.service.IReportService;
import com.baymin.springboot.store.enumconstant.OrderStatus;
import com.baymin.springboot.store.payload.report.PlatformOrderReport;
import com.baymin.springboot.store.payload.report.ServiceStaffReport;
import com.baymin.springboot.store.payload.report.UserInfoReport;
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
                new Object[]{minDate, maxDate, OrderStatus.ORDER_UN_PAY.getIndex(), OrderStatus.ORDER_ASSIGN.getIndex(),
                        OrderStatus.ORDER_FINISH.getIndex(), OrderStatus.ORDER_PAYED.getIndex(), OrderStatus.ORDER_PROCESSING.getIndex()}, Double.class);

        int validOrderCount = jdbcTemplate.queryForObject("select COALESCE(count(t.ID), 0) " +
                        "from T_ORDER t where  t.ORDER_TIME>=? and t.ORDER_TIME<=? and t.STATUS in(?, ?, ?, ?, ?)",
                new Object[]{minDate, maxDate, OrderStatus.ORDER_UN_PAY.getIndex(), OrderStatus.ORDER_ASSIGN.getIndex(),
                        OrderStatus.ORDER_FINISH.getIndex(), OrderStatus.ORDER_PAYED.getIndex(), OrderStatus.ORDER_PROCESSING.getIndex()}, Integer.class);

        double invalidOrderTotalFee = jdbcTemplate.queryForObject("select CAST(COALESCE(sum(t.TOTAL_FEE), 0) AS DECIMAL(18,2)) " +
                        "from T_ORDER t where  t.ORDER_TIME>=? and t.ORDER_TIME<=? and t.STATUS in(?)",
                new Object[]{minDate, maxDate, OrderStatus.ORDER_CANCELED.getIndex()}, Double.class);

        int invalidOrderCount = jdbcTemplate.queryForObject("select COALESCE(count(t.ID), 0) " +
                        "from T_ORDER t where  t.ORDER_TIME>=? and t.ORDER_TIME<=? and t.STATUS in(?)",
                new Object[]{minDate, maxDate, OrderStatus.ORDER_CANCELED.getIndex()}, Integer.class);

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

    @Override
    public UserInfoReport queryUserInfoReport(Date minDate, Date maxDate) {
        if (Objects.isNull(minDate)) {
            minDate = DateUtil.dayBegin("1970-01-01");
        }
        if (Objects.isNull(maxDate)) {
            maxDate = new Date();
        }

        long userCount = jdbcTemplate.queryForObject("select COALESCE(count(t.ID), 0) " +
                        "from T_USER_PROFILE t where  t.REGISTER_TIME>=? and t.REGISTER_TIME<=? ",
                new Object[]{minDate, maxDate}, Long.class);

        long userHaveOrderCount = jdbcTemplate.queryForObject("select count(m.ORDER_USER_ID) from (select t.ORDER_USER_ID from T_ORDER t where  t.ORDER_TIME>=? and t.ORDER_TIME<=? group by t.ORDER_USER_ID) m",
                new Object[]{minDate, maxDate}, Long.class);
        return new UserInfoReport(userCount, userHaveOrderCount);
    }

    @Override
    public ServiceStaffReport queryServiceStaffReport(Date minDate, Date maxDate) {
        if (Objects.isNull(minDate)) {
            minDate = DateUtil.dayBegin("1970-01-01");
        }
        if (Objects.isNull(maxDate)) {
            maxDate = new Date();
        }

        long staffCount = jdbcTemplate.queryForObject("select COALESCE(count(t.ID), 0) " +
                        "from T_SERVICE_STAFF t where  t.CREATE_TIME>=? and t.CREATE_TIME<=? ",
                new Object[]{minDate, maxDate}, Long.class);

        long staffHaveOrderCount = jdbcTemplate.queryForObject("select count(m.SERVICE_STAFF_ID) from (select t.SERVICE_STAFF_ID from T_ORDER t where  t.ORDER_TIME>=? and t.ORDER_TIME<=? group by t.SERVICE_STAFF_ID) m",
                new Object[]{minDate, maxDate}, Long.class);
        return new ServiceStaffReport(staffCount, staffHaveOrderCount);
    }
}
