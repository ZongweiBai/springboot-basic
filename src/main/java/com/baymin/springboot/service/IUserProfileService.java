package com.baymin.springboot.service;

import com.baymin.springboot.entity.UserProfile;

/**
 * Created by Baymin on 2017/4/9.
 */
public interface IUserProfileService {

    UserProfile findByAccount(String account);

    void saveUserProfile(UserProfile userProfile);

}
