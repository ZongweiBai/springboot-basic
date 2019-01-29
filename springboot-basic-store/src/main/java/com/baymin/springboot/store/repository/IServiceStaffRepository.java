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
    @Query("update ServiceStaff set staffStatus = :statusType, idCard = :idCard where id = :staffId")
    void updateStaffStatus(@Param("staffId") String staffId, @Param("statusType") CommonStatus statusType, @Param("idCard") String idCard);

    @Query("select s from ServiceStaff s where s.serviceStaffType = :staffType " +
            "and s.serviceStatus = :serviceStatus and s.staffStatus = :staffStatus")
    List<ServiceStaff> findFreeStaff(@Param("staffType") ServiceStaffType serviceStaffType,
                                     @Param("serviceStatus") ServiceStatus free,
                                     @Param("staffStatus") CommonStatus staffStatus);

    @Modifying
    @Query("update ServiceStaff set serviceStatus = :status where id = :staffId")
    void updateServiceStatus(@Param("staffId") String staffId, @Param("status") ServiceStatus status);

    @Query("select vo from ServiceStaff vo where vo.mobile = :mobile and vo.staffStatus = :status")
    ServiceStaff findByMobile(@Param("mobile") String userAccount, @Param("status") CommonStatus status);

    @Modifying
    @Query("update ServiceStaff set idpId = :idpId where id = :id")
    void updateIdpId(@Param("id") String id, @Param("idpId") String openId);

    ServiceStaff findByIdpId(String openid);

    @Modifying
    @Query("update ServiceStaff  set assignOrderNotification = :notification where id = :id")
    void updateAssignOrderNotification(@Param("id") String staffId, @Param("notification") Boolean enableNotification);

    @Query("select s from ServiceStaff s where s.serviceStaffType = :staffType " +
            "and s.staffStatus = :staffStatus order by s.serviceStatus asc")
    List<ServiceStaff> findByStaffType(@Param("staffType") ServiceStaffType serviceStaffType,
                                       @Param("staffStatus") CommonStatus staffStatus);

    @Query("select s from ServiceStaff s where s.id in :ids")
    List<ServiceStaff> findByIds(@Param("ids") List<String> staffIds);

    @Modifying
    @Query("update ServiceStaff set idpId = null where id = :staffId")
    void resetIdpId(@Param("staffId") String staffId);

    @Query("select count(vo.mobile) from ServiceStaff vo where vo.mobile = :mobile and vo.staffStatus = :status and vo.id <> :staffId")
    long countByMobileAndStaffId(@Param("mobile") String mobile, @Param("staffId") String staffId, @Param("status") CommonStatus status);

    @Query("select count(vo.mobile) from ServiceStaff vo where vo.mobile = :mobile and vo.staffStatus = :status")
    long countByMobile(@Param("mobile") String mobile, @Param("status") CommonStatus status);
}
