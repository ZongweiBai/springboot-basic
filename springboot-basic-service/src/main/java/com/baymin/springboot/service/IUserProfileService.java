package com.baymin.springboot.service;

import com.baymin.springboot.pay.wechat.param.pojo.UserInfoResponse;
import com.baymin.springboot.store.entity.UserProfile;
import com.baymin.springboot.store.entity.WechatUserInfo;
import com.baymin.springboot.store.payload.TokenVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by ebaizon on 7/31/2017.
 */
public interface IUserProfileService {

    UserProfile findByAccount(String account);

    UserProfile saveUserProfile(UserProfile userProfile);

    UserProfile findById(String userId);

    Page<UserProfile> queryUserForPage(Pageable pageable, String nickName, String account, String sex, Date maxDate, Date minDate);

    Map<String,Object> getUserDetail(String userId);

    UserProfile findByIdpId(String openid);

    WechatUserInfo saveWechatUserInfo(UserInfoResponse userInfoResponse);

    TokenVo getTokenVo(String userId, String userType) throws JsonProcessingException;

    WechatUserInfo getWechatUserInfoById(String wechatId);

    List<UserProfile> queryUserProfile();

    void resetIdpId(String userId);
}
