package com.baymin.springboot.store.payload.report;

import com.baymin.springboot.common.util.BigDecimalUtil;
import lombok.Data;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

@Data
public class QuickOrderReport implements RowMapper<QuickOrderReport> {

    private String hospitalName;

    private Double totalFee;

    private Double refundFee;

    private Double actualIncome;

    private Double totalInFee;

    private Double inOneToOne;

    private Double inOneToMany;

    private Double inManyToOne;

    private Double totalOutFee;

    private Double outOneToOne;

    private Double outOneToMany;

    private Double outManyToOne;

    @Override
    public QuickOrderReport mapRow(ResultSet resultSet, int i) throws SQLException {
        QuickOrderReport report = new QuickOrderReport();
        report.setHospitalName(resultSet.getString("hospitalName"));
        report.setTotalFee(resultSet.getDouble("totalFee"));
        report.setRefundFee(resultSet.getDouble("refundFee"));
        if (Objects.isNull(report.getRefundFee())) {
            report.setRefundFee(0.0D);
        }
        report.setActualIncome(BigDecimalUtil.sub(report.getTotalFee(), report.getRefundFee()));
        report.setRefundFee(resultSet.getDouble("refundFee"));
        report.setTotalInFee(resultSet.getDouble("totalInFee"));
        report.setInOneToOne(resultSet.getDouble("inOneToOne"));
        report.setInOneToMany(resultSet.getDouble("inOneToMany"));
        report.setInManyToOne(resultSet.getDouble("inManyToOne"));
        report.setTotalOutFee(resultSet.getDouble("totalOutFee"));
        report.setOutOneToOne(resultSet.getDouble("outOneToOne"));
        report.setOutOneToMany(resultSet.getDouble("outOneToMany"));
        report.setOutManyToOne(resultSet.getDouble("outManyToOne"));

        return report;
    }
}
