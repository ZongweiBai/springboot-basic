package com.baymin.springboot.service.impl;

import com.baymin.springboot.common.exception.ErrorCode;
import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.common.exception.WebServerException;
import com.baymin.springboot.common.util.BigDecimalUtil;
import com.baymin.springboot.service.IWithdrawService;
import com.baymin.springboot.store.entity.QWithdraw;
import com.baymin.springboot.store.entity.StaffIncome;
import com.baymin.springboot.store.entity.UserWallet;
import com.baymin.springboot.store.entity.Withdraw;
import com.baymin.springboot.store.enumconstant.IncomeType;
import com.baymin.springboot.store.enumconstant.WithdrawResult;
import com.baymin.springboot.store.repository.IStaffIncomeRepository;
import com.baymin.springboot.store.repository.IUserWalletRepository;
import com.baymin.springboot.store.repository.IWithdrawRepository;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class WithdrawServiceImpl implements IWithdrawService {

    @Autowired
    private IWithdrawRepository withdrawRepository;

    @Autowired
    private IUserWalletRepository userWalletRepository;

    @Autowired
    private IStaffIncomeRepository staffIncomeRepository;

    @Override
    public void saveWithdraw(Withdraw withdraw) {
        UserWallet userWallet = userWalletRepository.findByUserId(withdraw.getUserId(), withdraw.getUserType());
        if (Objects.isNull(userWallet) || withdraw.getWithdrawAmount() > userWallet.getBalance()) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), "用户余额不足，无法提现"));
        }

        withdraw.setCreateTime(new Date());
        withdraw.setResult(WithdrawResult.UN_DEAL);
        withdrawRepository.save(withdraw);

        userWallet.setTotalInWithdrawing(BigDecimalUtil.add(userWallet.getTotalInWithdrawing(), withdraw.getWithdrawAmount()));
        userWallet.setBalance(BigDecimalUtil.sub(userWallet.getBalance(), withdraw.getWithdrawAmount()));
        userWalletRepository.save(userWallet);
    }

    @Override
    public void dealWithdraw(Withdraw withdraw) {
        Withdraw oldData = withdrawRepository.findById(withdraw.getId()).orElse(null);
        if (Objects.isNull(oldData)) {
            return;
        }
        oldData.setResult(withdraw.getResult());
        oldData.setDealDesc(withdraw.getDealDesc());
        oldData.setDealTime(new Date());
        withdrawRepository.save(oldData);

        UserWallet userWallet = userWalletRepository.findByUserId(oldData.getUserId(), oldData.getUserType());
        if (Objects.isNull(userWallet)) {
            return;
        }
        if (oldData.getResult() == WithdrawResult.SUCCESS) {
            userWallet.setTotalInWithdrawing(BigDecimalUtil.sub(userWallet.getTotalInWithdrawing(), oldData.getWithdrawAmount()));
            userWallet.setTotalWithdraw(BigDecimalUtil.add(userWallet.getTotalWithdraw(), oldData.getWithdrawAmount()));
            userWalletRepository.save(userWallet);

            StaffIncome staffIncome = new StaffIncome();
            staffIncome.setCreateTime(new Date());
            staffIncome.setIncome(oldData.getWithdrawAmount());
            staffIncome.setOrderTotalFee(0.00D);
            staffIncome.setStaffId(oldData.getUserId());
            staffIncome.setCurrentBalance(userWallet.getBalance());
            staffIncome.setIncomeType(IncomeType.WITHDRAW);
            staffIncome.setIncomeRemark("账户结算");
            staffIncomeRepository.save(staffIncome);
        } else if (oldData.getResult() == WithdrawResult.DENY) {
            userWallet.setTotalInWithdrawing(BigDecimalUtil.sub(userWallet.getTotalInWithdrawing(), oldData.getWithdrawAmount()));
            userWallet.setBalance(BigDecimalUtil.add(userWallet.getBalance(), oldData.getWithdrawAmount()));
            userWalletRepository.save(userWallet);
        }
    }

    @Override
    public List<Withdraw> queryByUserIdAndType(String userId, String userType, WithdrawResult result) {
        BooleanBuilder builder = new BooleanBuilder();
        QWithdraw qWithdraw = QWithdraw.withdraw;

        if (StringUtils.isNotBlank(userId)) {
            builder.and(qWithdraw.userId.eq(userId));
        }
        if (Objects.nonNull(result)) {
            builder.and(qWithdraw.result.eq(result));
        }
        if (StringUtils.isNotBlank(userType)) {
            builder.and(qWithdraw.userType.eq(userType));
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");

        Iterable<Withdraw> iterable = withdrawRepository.findAll(builder, sort);
        List<Withdraw> list = new ArrayList<>();
        iterable.forEach(list::add);
        return list;
    }

    @Override
    public UserWallet queryUserWalletByUserId(String staffId, String userType) {
        return userWalletRepository.findByUserId(staffId, userType);
    }

    @Override
    public Page<Withdraw> queryWithdrawPage(PageRequest pageRequest, WithdrawResult result, Date maxDate, Date minDate, String userId) {
        QWithdraw qWithdraw = QWithdraw.withdraw;

        BooleanBuilder predicate = new BooleanBuilder();
        if (StringUtils.isNotBlank(userId)) {
            predicate.and(qWithdraw.userId.eq(userId));
        }
        if (Objects.nonNull(result)) {
            predicate.and(qWithdraw.result.eq(result));
        }
        if (Objects.nonNull(maxDate)) {
            predicate.and(qWithdraw.createTime.lt(maxDate));
        }
        if (Objects.nonNull(minDate)) {
            predicate.and(qWithdraw.createTime.gt(minDate));
        }

        return withdrawRepository.findAll(predicate, pageRequest);
    }
}
