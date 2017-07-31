package com.baymin.springboot.store.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.baymin.springboot.store.entity.UserProfile;

/**
 * Created by ebaizon on 7/31/2017.
 */
public interface IUserProfileRepository extends PagingAndSortingRepository<UserProfile, String>, JpaSpecificationExecutor<UserProfile> {

    UserProfile findByAccount(String account);

}
