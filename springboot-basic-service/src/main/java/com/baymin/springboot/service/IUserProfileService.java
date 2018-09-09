package com.baymin.springboot.service;

import com.baymin.springboot.store.entity.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

/**
 * Created by ebaizon on 7/31/2017.
 */
public interface IUserProfileService {

    UserProfile findByAccount(String account);

    void saveUserProfile(UserProfile userProfile);

    UserProfile findById(String userId);

    Page<UserProfile> queryUserForPage(Pageable pageable, String nickName, String account, String sex, Date maxDate, Date minDate);
}
