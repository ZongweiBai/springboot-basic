package com.baymin.springboot.service;

import com.baymin.springboot.store.entity.StaffIncome;
import com.baymin.springboot.store.payload.StaffRankVo;

import java.util.Date;
import java.util.List;

public interface IStaffIncomeService {
    List<StaffIncome> queryStaffIncome(String staffId);

    double sumIncomeByDate(String staffId, Date monthFirst, Date monthLast);

    List<StaffRankVo> queryStaffRank();

}
