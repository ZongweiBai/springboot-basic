package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by ebaizon on 7/31/2017.
 */
public interface IUserProfileRepository extends PagingAndSortingRepository<UserProfile, String>,
        JpaSpecificationExecutor<UserProfile>,
        QuerydslPredicateExecutor<UserProfile> {

    UserProfile findByAccount(String account);

    UserProfile findByIdpId(String openid);

    @Query("select vo from UserProfile vo where vo.id in :ids")
    List<UserProfile> findByIds(@Param("ids") List<String> userIds);
}
