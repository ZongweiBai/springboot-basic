package com.baymin.springboot.service.impl;

import com.baymin.springboot.service.IOrganizationService;
import com.baymin.springboot.store.entity.Organization;
import com.baymin.springboot.store.repository.IOrganizationRepository;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class OrganizationServiceImpl implements IOrganizationService {

    @Autowired
    private IOrganizationRepository organizationRepository;

    @Override
    public Page<Organization> queryOrgForPage(Pageable pageable) {
        return organizationRepository.findAll(pageable);
    }

    @Override
    public Organization getOrgById(String orgId) {
        return organizationRepository.findById(orgId).orElse(null);
    }

    @Override
    public List<Organization> getAllOrg() {
        Iterable<Organization> iterable = organizationRepository.findAll();
        List<Organization> list = Lists.newArrayList();
        iterable.forEach(list::add);
        return list;
    }

    @Override
    public void saveOrg(Organization organization) {
        if (StringUtils.isNotBlank(organization.getId())) {
            Organization oldData = organizationRepository.findById(organization.getId()).orElse(null);
            organization.setCreateTime(oldData.getCreateTime());
        } else {
            organization.setCreateTime(new Date());
        }
        organizationRepository.save(organization);
    }
}
