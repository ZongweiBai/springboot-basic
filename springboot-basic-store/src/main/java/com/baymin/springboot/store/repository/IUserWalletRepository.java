package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.UserWallet;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface IUserWalletRepository extends PagingAndSortingRepository<UserWallet, String>,
        JpaSpecificationExecutor<UserWallet>,
        QuerydslPredicateExecutor<UserWallet> {

    @Query("select t from UserWallet t where t.userId= :userId and t.userType = :userType")
    UserWallet findByUserId(@Param("userId") String userId, @Param("userType") String userType);
}
