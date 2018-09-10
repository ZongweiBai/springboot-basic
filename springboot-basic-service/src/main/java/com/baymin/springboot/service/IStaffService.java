package com.baymin.springboot.service;

import com.baymin.springboot.store.entity.ServiceStaff;
import com.baymin.springboot.store.enumconstant.CommonStatus;
import com.baymin.springboot.store.enumconstant.ServiceStaffType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface IStaffService {
    Page<ServiceStaff> queryStaffForPage(Pageable pageable, String userName, String account, String sex);

    void saveStaff(ServiceStaff serviceStaff);

    ServiceStaff findById(String staffId);

    Map<String,Object> getStaffDetail(String staffId);

    void updateStaffStatus(String staffId, CommonStatus statusType);

    List<ServiceStaff> queryStaffByType(ServiceStaffType serviceStaffType);
}
