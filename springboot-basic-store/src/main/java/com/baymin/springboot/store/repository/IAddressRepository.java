package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.Address;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface IAddressRepository extends PagingAndSortingRepository<Address, String>, JpaSpecificationExecutor<Address> {

    List<Address> findByUserId(String userId);
}
