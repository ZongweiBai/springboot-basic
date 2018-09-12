package com.baymin.springboot.service;

import com.baymin.springboot.store.entity.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOrganizationService {
    Page<Organization> queryOrgForPage(Pageable pageable);

    Organization getOrgById(String orgId);

    List<Organization> getAllOrg();

    void saveOrg(Organization organization);
}
