package com.baymin.springboot.service.impl;

import com.baymin.springboot.common.constant.Constant;
import com.baymin.springboot.common.util.JwtUtil;
import com.baymin.springboot.pay.wechat.param.pojo.UserInfoResponse;
import com.baymin.springboot.service.IUserProfileService;
import com.baymin.springboot.store.dao.IUserProfileDao;
import com.baymin.springboot.store.entity.*;
import com.baymin.springboot.store.payload.TokenVo;
import com.baymin.springboot.store.repository.IAddressRepository;
import com.baymin.springboot.store.repository.IOrderRepository;
import com.baymin.springboot.store.repository.IUserProfileRepository;
import com.baymin.springboot.store.repository.IWechatUserInfoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
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

    @Autowired
    private IWechatUserInfoRepository wechatUserInfoRepository;

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

        BooleanBuilder predicate = new BooleanBuilder();
        if (StringUtils.isNotBlank(nickName)) {
            predicate.and(qUserProfile.nickName.likeIgnoreCase(Expressions.asString("%").concat(nickName).concat("%")));
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

    @Override
    public WechatUserInfo saveWechatUserInfo(UserInfoResponse wechatResponse) {
        WechatUserInfo oldData = wechatUserInfoRepository.findByOpenid(wechatResponse.getOpenid());
        if (Objects.nonNull(oldData)) {
            return oldData;
        }

        WechatUserInfo userInfo = new WechatUserInfo();
        userInfo.setCity(wechatResponse.getCity());
        userInfo.setCountry(wechatResponse.getCountry());
        userInfo.setHeadimgurl(wechatResponse.getHeadimgurl());
        userInfo.setNickname(wechatResponse.getNickname());
        userInfo.setOpenid(wechatResponse.getOpenid());
        userInfo.setProvince(wechatResponse.getProvince());
        userInfo.setSex(wechatResponse.getSex());
        userInfo.setUnionid(wechatResponse.getUnionid());
        return wechatUserInfoRepository.save(userInfo);
    }

    @Override
    public TokenVo getTokenVo(String userId, String userType) throws JsonProcessingException {
        String subject = JwtUtil.generalSubject(userId, userType, Constant.JWTAPI.JWT_TOKEN);
        String accessToken = JwtUtil.createJWT(Constant.JWTAPI.JWT_ID, subject, Constant.JWTAPI.JWT_TTL);
        subject = JwtUtil.generalSubject(userId, userType, Constant.JWTAPI.JWT_REFRESH_TOKEN);
        String refreshToken = JwtUtil.createJWT(Constant.JWTAPI.JWT_ID, subject, Constant.JWTAPI.JWT_REFRESH_TTL);

        TokenVo tokenVo = new TokenVo();
        tokenVo.setUserId(userId);
        tokenVo.setAccessToken(accessToken);
        tokenVo.setRefreshToken(refreshToken);
        tokenVo.setExpiresIn(Constant.JWTAPI.JWT_TTL / 1000);
        tokenVo.setUserType(userType);
        tokenVo.setTokenType("bearer");
        return tokenVo;
    }

    @Override
    public WechatUserInfo getWechatUserInfoById(String wechatId) {
        return wechatUserInfoRepository.findById(wechatId).orElse(null);
    }

    @Override
    public List<UserProfile> queryUserProfile() {
        Iterable<UserProfile> iterable = userProfileRepository.findAll();
        List<UserProfile> list = Lists.newArrayList();
        iterable.forEach(list::add);
        return list;
    }
}
