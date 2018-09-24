package com.baymin.springboot.service.impl;

import com.baymin.springboot.service.IUserProfileService;
import com.baymin.springboot.store.dao.IUserProfileDao;
import com.baymin.springboot.store.entity.Address;
import com.baymin.springboot.store.entity.Order;
import com.baymin.springboot.store.entity.QUserProfile;
import com.baymin.springboot.store.entity.UserProfile;
import com.baymin.springboot.store.repository.IAddressRepository;
import com.baymin.springboot.store.repository.IOrderRepository;
import com.baymin.springboot.store.repository.IUserProfileRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.catalina.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Autowired
    private IAddressRepository addressRepository;

    @Autowired
    private IOrderRepository orderRepository;

    @Override
    public UserProfile findByAccount(String account) {
        return userProfileDao.findByAccount(account);
    }

    @Override
    public UserProfile saveUserProfile(UserProfile userProfile) {
        if (StringUtils.isNotBlank(userProfile.getBirthdayStr())) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                userProfile.setBirthday(format.parse(userProfile.getBirthdayStr()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (StringUtils.isNotBlank(userProfile.getId())) {
            UserProfile oldData = userProfileRepository.findById(userProfile.getId()).orElse(null);
            userProfile.setRegisterTime(oldData.getRegisterTime());
            userProfile.setOrderCount(oldData.getOrderCount());
            userProfile.setPassword(oldData.getPassword());
            userProfile.setPayPassword(oldData.getPayPassword());
        } else {
            userProfile.setRegisterTime(new Date());
        }
        userProfileDao.save(userProfile);
        return userProfile;
    }

    @Override
    public UserProfile findById(String userId) {
        return userProfileRepository.findById(userId).orElse(null);
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

    @Override
    public Map<String, Object> getUserDetail(String userId) {
        UserProfile userProfile = userProfileRepository.findById(userId).orElse(null);
        List<Address> addressList = addressRepository.findByUserId(userId);
        List<Order> orderList = orderRepository.findByOrderUserIdOrderByOrderTimeDesc(userId);

        Map<String, Object> detailMap = new HashMap<>();
        detailMap.put("userProfile", userProfile);
        detailMap.put("addressList", addressList);
        detailMap.put("orderList", orderList);
        return detailMap;
    }

    @Override
    public UserProfile findByIdpId(String openid) {
        return userProfileRepository.findByIdpId(openid);
    }
}
