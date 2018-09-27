package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.WechatUserInfo;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IWechatUserInfoRepository extends PagingAndSortingRepository<WechatUserInfo, String>,
        JpaSpecificationExecutor<WechatUserInfo>,
        QuerydslPredicateExecutor<WechatUserInfo> {

    WechatUserInfo findByOpenid(String openid);

}
