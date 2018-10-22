package com.baymin.springboot.store.payload;

import com.baymin.springboot.store.entity.StaffIncome;
import com.baymin.springboot.store.enumconstant.IncomeType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Objects;

@ApiModel(description = "护工/护士收入明细")
@Data
public class StaffIncomeVo {

    @ApiModelProperty(hidden = true)
    private String id;

    @ApiModelProperty(notes = "护士/护工ID")
    private String staffId;

    @ApiModelProperty(notes = "订单ID")
    private String orderId;

    @ApiModelProperty(notes = "订单总金额")
    private Double orderTotalFee;

    @ApiModelProperty(notes = "实际收入")
    private String income;

    @ApiModelProperty(notes = "收入说明")
    private String incomeRemark;

    @ApiModelProperty(notes = "实时余额")
    private Double currentBalance;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    public StaffIncomeVo(StaffIncome staffIncome) {
        if (Objects.isNull(staffIncome)) {
            return;
        }
        this.id = staffIncome.getId();
        this.staffId = staffIncome.getStaffId();
        this.orderId = staffIncome.getOrderId();
        this.orderTotalFee = staffIncome.getOrderTotalFee();
        if (staffIncome.getIncomeType() == IncomeType.INCOME) {
            this.income = "+" + staffIncome.getIncome();
        } else {
            this.income = "-" + staffIncome.getIncome();
        }
        this.createTime = staffIncome.getCreateTime();
        this.incomeRemark = staffIncome.getIncomeRemark();
        this.currentBalance = staffIncome.getCurrentBalance();
    }
}
