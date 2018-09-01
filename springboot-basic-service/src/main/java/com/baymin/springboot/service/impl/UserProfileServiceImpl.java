package com.baymin.springboot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Date;
import com.baymin.springboot.store.dao.IUserProfileDao;
import com.baymin.springboot.store.entity.UserProfile;
import com.baymin.springboot.service.IUserProfileService;

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
        userProfile.setRegisterTime(System.currentTimeMillis());
        userProfileDao.save(userProfile);
    }

    @Override
    public UserProfile findById(String userId) {
        return userProfileDao.findById(userId);
    }
}
