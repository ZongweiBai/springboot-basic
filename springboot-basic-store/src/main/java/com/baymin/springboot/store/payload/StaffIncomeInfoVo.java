package com.baymin.springboot.store.payload;

import com.baymin.springboot.store.entity.StaffIncome;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@ApiModel(description = "护士/护工收入汇总信息")
@Data
public class StaffIncomeInfoVo {

    @ApiModelProperty(notes = "总收入")
    private Double totalIncome;

    @ApiModelProperty(notes = "当月收入")
    private Double monthlyIncome;

    @ApiModelProperty(notes = "收入明细")
    private List<StaffIncomeVo> incomeVoList;

    public StaffIncomeInfoVo(double totalIncome, double income, List<StaffIncome> incomeList) {
        this.totalIncome = totalIncome;
        this.monthlyIncome = income;
        if (CollectionUtils.isNotEmpty(incomeList)) {
            List<StaffIncomeVo> incomeVos = new ArrayList<>();
            for (StaffIncome staffIncome : incomeList) {
                incomeVos.add(new StaffIncomeVo(staffIncome));
            }
            this.incomeVoList = incomeVos;
        }
    }
}
