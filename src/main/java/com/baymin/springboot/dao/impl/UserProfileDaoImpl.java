package com.baymin.springboot.dao.impl;

import com.baymin.springboot.dao.IUserProfileDao;
import com.baymin.springboot.entity.UserProfile;
import com.baymin.springboot.repository.IUserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by Baymin on 2017/4/9.
 */
@Repository
public class UserProfileDaoImpl implements IUserProfileDao {

    @Autowired
    private IUserProfileRepository userProfileRepository;

    @Override
    public void save(UserProfile userProfile) {
        userProfileRepository.save(userProfile);
    }

    @Override
    public UserProfile findByAccount(String account) {
        return userProfileRepository.findByAccount(account);
    }
}
