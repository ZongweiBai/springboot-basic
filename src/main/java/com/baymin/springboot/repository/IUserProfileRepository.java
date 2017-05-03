package com.baymin.springboot.repository;

import com.baymin.springboot.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by Baymin on 2017/4/9.
 */
public interface IUserProfileRepository extends PagingAndSortingRepository<UserProfile, String>, JpaSpecificationExecutor<UserProfile> {

    UserProfile findByAccount(String account);

}
