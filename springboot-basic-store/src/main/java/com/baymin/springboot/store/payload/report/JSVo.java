package com.baymin.springboot.store.payload.report;

import com.baymin.springboot.common.util.BigDecimalUtil;
import lombok.Data;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
public class JSVo implements RowMapper<JSVo> {

    private String orderTime;

    private String careType;

    private Long orderCount;

    private Double price;

    private Double totalFee;

    private Double wechatPayCount;

    private Double cashPayCount;

    private Double posPayCount;

    private Double alipayPayCount;

    private Double offlineWechatPayCount;

    @Override
    public JSVo mapRow(ResultSet resultSet, int i) throws SQLException {
        JSVo vo = new JSVo();
        vo.setOrderTime(resultSet.getString("orderTime"));
        vo.setOrderCount(resultSet.getLong("orderCount"));
        vo.setTotalFee(resultSet.getDouble("totalFee"));
        try {
            vo.setPrice(BigDecimalUtil.div(vo.getTotalFee(), vo.getOrderCount(), 2));
            vo.setWechatPayCount((BigDecimalUtil.div(resultSet.getDouble("wechatPayCount"), vo.getOrderCount(), 4) * 100));
            vo.setCashPayCount((BigDecimalUtil.div(resultSet.getDouble("cashPayCount"), vo.getOrderCount(), 4) * 100));
            vo.setPosPayCount((BigDecimalUtil.div(resultSet.getDouble("posPayCount"), vo.getOrderCount(), 4) * 100));
            vo.setAlipayPayCount((BigDecimalUtil.div(resultSet.getDouble("alipayPayCount"), vo.getOrderCount(), 4) * 100));
            vo.setOfflineWechatPayCount((BigDecimalUtil.div(resultSet.getDouble("offlineWechatPayCount"), vo.getOrderCount(), 4) * 100));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return vo;
    }
}
