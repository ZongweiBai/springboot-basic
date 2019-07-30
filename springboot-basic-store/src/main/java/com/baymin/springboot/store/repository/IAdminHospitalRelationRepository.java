package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.AdminHospitalRelation;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IAdminHospitalRelationRepository extends PagingAndSortingRepository<AdminHospitalRelation, String>, JpaSpecificationExecutor<AdminHospitalRelation> {

    List<AdminHospitalRelation> findByAdminId(String adminId);

    @Modifying
    @Query("delete from AdminHospitalRelation where adminId = :adminId")
    void deleteByAdminId(@Param("adminId") String adminId);
}
