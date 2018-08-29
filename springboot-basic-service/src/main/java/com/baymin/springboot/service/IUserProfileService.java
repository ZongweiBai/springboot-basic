package com.baymin.springboot.service;

import com.baymin.springboot.store.entity.UserProfile;

/**
 * Created by ebaizon on 7/31/2017.
 */
public interface IUserProfileService {

    UserProfile findByAccount(String account);

    void saveUserProfile(UserProfile userProfile);

    UserProfile findById(String userId);
}
