package com.baymin.springboot.store.payload;

import com.baymin.springboot.store.entity.StaffIncome;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class StaffIncomeInfoVo {

    private Double totalIncome;

    private Double monthlyIncome;

    private List<StaffIncomeVo> incomeVoList;

    public StaffIncomeInfoVo(double totalIncome, double income, List<StaffIncome> incomeList) {
        this.totalIncome = totalIncome;
        this.monthlyIncome = income;
        if (CollectionUtils.isNotEmpty(incomeList)) {
            List<StaffIncomeVo> incomeVos = new ArrayList<>();
            for (StaffIncome staffIncome : incomeList) {
                incomeVos.add(new StaffIncomeVo(staffIncome));
            }
        }
    }
}
