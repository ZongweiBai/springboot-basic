package com.baymin.springboot.service.impl;

import com.baymin.springboot.service.IUserProfileService;
import com.baymin.springboot.store.dao.IUserProfileDao;
import com.baymin.springboot.store.entity.QUserProfile;
import com.baymin.springboot.store.entity.UserProfile;
import com.baymin.springboot.store.repository.IUserProfileRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Objects;

/**
 * Created by Baymin on 2017/4/9.
 */
@Service
@Transactional
public class UserProfileServiceImpl implements IUserProfileService {

    @Autowired
    private IUserProfileDao userProfileDao;

    @Autowired
    private IUserProfileRepository userProfileRepository;

    @Override
    public UserProfile findByAccount(String account) {
        return userProfileDao.findByAccount(account);
    }

    @Override
    public void saveUserProfile(UserProfile userProfile) {
        if (StringUtils.isNotBlank(userProfile.getId())) {
            UserProfile oldData = userProfileRepository.findById(userProfile.getId()).orElse(null);
            userProfile.setRegisterTime(oldData.getRegisterTime());
            userProfile.setIdpId(oldData.getIdpId());
            userProfile.setIdpNickName(oldData.getIdpNickName());
            userProfile.setOrderCount(oldData.getOrderCount());
        } else {
            userProfile.setRegisterTime(new Date());
        }
        userProfileDao.save(userProfile);
    }

    @Override
    public UserProfile findById(String userId) {
        return userProfileDao.findById(userId);
    }

    @Override
    public Page<UserProfile> queryUserForPage(Pageable pageable, String nickName, String account, String sex, Date maxDate, Date minDate) {
        QUserProfile qUserProfile = QUserProfile.userProfile;

        BooleanExpression predicate = qUserProfile.id.isNotNull();
        if (StringUtils.isNotBlank(nickName)) {
            predicate.and(qUserProfile.nickName.eq(nickName));
        }
        if (StringUtils.isNotBlank(account)) {
            predicate.and(qUserProfile.account.eq(account));
        }
        if (StringUtils.isNotBlank(sex)) {
            predicate.and(qUserProfile.sex.eq(sex));
        }
        if (Objects.nonNull(maxDate)) {
            predicate.and(qUserProfile.registerTime.lt(maxDate));
        }
        if (Objects.nonNull(minDate)) {
            predicate.and(qUserProfile.registerTime.gt(minDate));
        }

        return userProfileRepository.findAll(predicate, pageable);
    }
}
