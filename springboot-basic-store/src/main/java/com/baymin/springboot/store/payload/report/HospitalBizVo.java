package com.baymin.springboot.store.payload.report;

import com.baymin.springboot.common.util.BigDecimalUtil;
import lombok.Data;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
public class HospitalBizVo implements RowMapper<HospitalBizVo> {

    private String orderTime;

    private String staffId;

    private String staffName;

    private Double personFee;

    private Double totalFee;

    private Double companyFee;

    private Long orderCount;

    @Override
    public HospitalBizVo mapRow(ResultSet resultSet, int i) throws SQLException {
        HospitalBizVo vo = new HospitalBizVo();
        vo.setOrderTime(resultSet.getString("orderTime"));
        vo.setStaffId(resultSet.getString("staffId"));
        vo.setOrderCount(resultSet.getLong("orderCount"));
        vo.setTotalFee(resultSet.getDouble("totalFee"));
        vo.setPersonFee(BigDecimalUtil.mul(vo.getTotalFee(), 0.8D));
        vo.setCompanyFee(BigDecimalUtil.mul(vo.getTotalFee(), 0.2D));
        return vo;
    }
}
