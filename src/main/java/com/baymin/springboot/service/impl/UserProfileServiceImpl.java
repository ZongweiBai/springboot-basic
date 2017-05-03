package com.baymin.springboot.service.impl;

import com.baymin.springboot.dao.IUserProfileDao;
import com.baymin.springboot.entity.UserProfile;
import com.baymin.springboot.service.IUserProfileService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by Baymin on 2017/4/9.
 */
@Service
@Transactional
public class UserProfileServiceImpl implements IUserProfileService {

    @Autowired
    private IUserProfileDao userProfileDao;

    @Override
    public UserProfile findByAccount(String account) {
        return userProfileDao.findByAccount(account);
    }

    @Override
    public void saveUserProfile(UserProfile userProfile) {
        userProfile.setRegisterTime(new DateTime());
        userProfileDao.save(userProfile);
    }
}
