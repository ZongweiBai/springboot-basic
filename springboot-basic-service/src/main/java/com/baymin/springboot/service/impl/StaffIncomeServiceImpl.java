package com.baymin.springboot.service.impl;

import com.baymin.springboot.service.IStaffIncomeService;
import com.baymin.springboot.store.entity.StaffIncome;
import com.baymin.springboot.store.payload.StaffRankVo;
import com.baymin.springboot.store.repository.IStaffIncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class StaffIncomeServiceImpl implements IStaffIncomeService {

    @Autowired
    private IStaffIncomeRepository staffIncomeRepository;

    @Override
    public List<StaffIncome> queryStaffIncome(String staffId) {
        return staffIncomeRepository.findByStaffId(staffId);
    }

    @Override
    public double sumIncomeByDate(String staffId, Date monthFirst, Date monthLast) {
        return staffIncomeRepository.sumIncomeByDate(staffId, monthFirst, monthLast);
    }

    @Override
    public List<StaffRankVo> queryStaffRank() {
        return staffIncomeRepository.queryStaffRank();
    }
}
