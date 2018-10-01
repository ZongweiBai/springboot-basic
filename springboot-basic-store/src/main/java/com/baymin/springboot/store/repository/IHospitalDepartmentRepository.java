package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.HospitalDepartment;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IHospitalDepartmentRepository extends PagingAndSortingRepository<HospitalDepartment, String>,
        JpaSpecificationExecutor<HospitalDepartment>,
        QuerydslPredicateExecutor<HospitalDepartment> {

    @Modifying
    @Query("delete from HospitalDepartment t where t.hospitalId = :hospitalId")
    void deleteByHospitalId(@Param("hospitalId") String hospitalId);

    List<HospitalDepartment> findByHospitalId(String hospitalId);
}
