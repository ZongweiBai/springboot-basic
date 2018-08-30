package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by ebaizon on 7/31/2017.
 */
public interface IUserProfileRepository extends PagingAndSortingRepository<UserProfile, String>, JpaSpecificationExecutor<UserProfile>, QueryDslPredicateExecutor<UserProfile> {

    UserProfile findByAccount(String account);

}
