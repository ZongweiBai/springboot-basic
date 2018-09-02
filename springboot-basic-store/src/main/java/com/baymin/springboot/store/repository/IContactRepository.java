package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.Contact;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IContactRepository extends PagingAndSortingRepository<Contact, String>, JpaSpecificationExecutor<Contact> {

    @Modifying
    @Query(value = "update Contact set defaultFlag = :defaultFlag where userId = :userId and myRole = :myRole")
    int updateDefault(@Param("userId") String userId, @Param("myRole") String myRole, @Param("defaultFlag") String defaultContact);

    List<Contact> findByUserId(String userId);
}
