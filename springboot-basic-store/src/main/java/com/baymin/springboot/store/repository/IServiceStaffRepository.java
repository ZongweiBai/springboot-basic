package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.ServiceStaff;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IServiceStaffRepository extends PagingAndSortingRepository<ServiceStaff, String>, JpaSpecificationExecutor<ServiceStaff> {
}
