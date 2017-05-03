package com.baymin.springboot.dao;

import com.baymin.springboot.entity.UserProfile;

/**
 * Created by Baymin on 2017/4/9.
 */
public interface IUserProfileDao {

    void save(UserProfile userProfile);

    UserProfile findByAccount(String account);
}
