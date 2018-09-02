package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.ContactCared;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IContactCaredRepository extends PagingAndSortingRepository<ContactCared, String>, JpaSpecificationExecutor<ContactCared> {

    @Modifying
    @Query("update ContactCared set defaultFlag = :defaultFlag where userId = :userId")
    void updateDefault(@Param("userId") String userId, @Param("defaultFlag") String defaultFlag);

    List<ContactCared> findByUserId(String userId);
}
