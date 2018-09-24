package com.baymin.springboot.store.payload;

import com.baymin.springboot.store.entity.StaffIncome;
import lombok.Data;

import java.util.Date;
import java.util.Objects;

@Data
public class StaffIncomeVo {

    private String id;

    private String staffId;

    private String orderId;

    private Double orderTotalFee;

    private Double income;

    private Date createTime;

    public StaffIncomeVo(StaffIncome staffIncome) {
        if (Objects.isNull(staffIncome)) {
            return;
        }
        this.id = staffIncome.getId();
        this.staffId = staffIncome.getStaffId();
        this.orderId = staffIncome.getOrderId();
        this.orderTotalFee = staffIncome.getOrderTotalFee();
        this.income = staffIncome.getIncome();
        this.createTime = staffIncome.getCreateTime();
    }
}
