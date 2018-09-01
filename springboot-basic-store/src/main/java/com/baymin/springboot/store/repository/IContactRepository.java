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
    @Query(value = "update T_CONTACT set DEFAULT_CONTACT = :defaultContact where USER_ID = :userId and MY_ROLE = :myRole", nativeQuery = true)
//    @Query("update Contact set defaultContact = ?3 where userId = ?1 and myRole = ?2")
    int updateDefaultContacts(@Param("userId") String userId, @Param("myRole") String myRole, @Param("defaultContact") String defaultContact);

    List<Contact> findByUserId(String userId);
}
