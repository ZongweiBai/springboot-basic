package com.baymin.springboot.service;

import com.baymin.springboot.store.entity.Withdraw;
import com.baymin.springboot.store.enumconstant.WithdrawResult;

import java.util.List;

public interface IWithdrawService {

    void saveWithdraw(Withdraw withdraw);

    List<Withdraw> queryByUserIdAndType(String userId, String userType, WithdrawResult result);
}
