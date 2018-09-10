package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.ServiceStaff;
import com.baymin.springboot.store.enumconstant.CommonStatus;
import com.baymin.springboot.store.enumconstant.ServiceStaffType;
import com.baymin.springboot.store.enumconstant.ServiceStatus;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IServiceStaffRepository extends PagingAndSortingRepository<ServiceStaff, String>,
        JpaSpecificationExecutor<ServiceStaff>,
        QuerydslPredicateExecutor<ServiceStaff> {

    @Modifying
    @Query("update ServiceStaff set staffStatus = :statusType where id = :staffId")
    void updateStaffStatus(@Param("staffId") String staffId, @Param("statusType") CommonStatus statusType);

    @Query("select s from ServiceStaff s where s.serviceStaffType = :staffType " +
            "and s.serviceStatus = :serviceStatus and s.staffStatus = :staffStatus")
    List<ServiceStaff> findFreeStaff(@Param("staffType") ServiceStaffType serviceStaffType,
                                     @Param("serviceStatus") ServiceStatus free,
                                     @Param("staffStatus") CommonStatus staffStatus);
}
