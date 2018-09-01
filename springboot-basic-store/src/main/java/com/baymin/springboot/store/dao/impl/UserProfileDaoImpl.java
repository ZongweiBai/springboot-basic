package com.baymin.springboot.store.dao.impl;

import com.baymin.springboot.store.dao.IUserProfileDao;
import com.baymin.springboot.store.entity.QUserProfile;
import com.baymin.springboot.store.entity.UserProfile;
import com.baymin.springboot.store.repository.IUserProfileRepository;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by Baymin on 2017/4/9.
 */
@Repository
public class UserProfileDaoImpl implements IUserProfileDao {

    @Autowired
    @PersistenceContext
    private EntityManager entityManager;

    private JPAQueryFactory queryFactory;

    @PostConstruct
    public void init() {
        queryFactory = new JPAQueryFactory(entityManager);
    }

    @Autowired
    private IUserProfileRepository userProfileRepository;

    @Override
    @CacheEvict(value = "userProfile", allEntries = true)
    public void save(UserProfile userProfile) {
        userProfileRepository.save(userProfile);
    }

    @Override
    @Cacheable(value = "userProfile", keyGenerator = "wiselyKeyGenerator")
    public UserProfile findByAccount(String account) {
        return userProfileRepository.findByAccount(account);
    }

    @Override
    public UserProfile findById(String userId) {
        QUserProfile qUserProfile = QUserProfile.userProfile;
        JPAQuery<UserProfile> jpaQuery = queryFactory.select(qUserProfile)
                .where(qUserProfile.id.eq(userId));

        return jpaQuery.fetchOne();
    }
}
