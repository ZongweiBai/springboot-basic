package com.baymin.springboot.service;

import com.baymin.springboot.store.entity.UserWallet;
import com.baymin.springboot.store.entity.Withdraw;
import com.baymin.springboot.store.enumconstant.WithdrawResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Date;
import java.util.List;

public interface IWithdrawService {

    void saveWithdraw(Withdraw withdraw);

    List<Withdraw> queryByUserIdAndType(String userId, String userType, WithdrawResult result);

    UserWallet queryUserWalletByUserId(String staffId, String userType);

    void dealWithdraw(Withdraw withdraw);

    Page<Withdraw> queryWithdrawPage(PageRequest pageRequest, WithdrawResult result, Date maxDate, Date minDate, String userId);
}
