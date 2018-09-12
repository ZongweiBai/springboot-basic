package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.Organization;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IOrganizationRepository extends PagingAndSortingRepository<Organization, String>,
        JpaSpecificationExecutor<Organization>,
        QuerydslPredicateExecutor<Organization> {
}
