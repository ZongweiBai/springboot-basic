package com.baymin.springboot.service.impl;

import com.baymin.springboot.service.IAdminService;
import com.baymin.springboot.service.IHospitalService;
import com.baymin.springboot.service.IOrganizationService;
import com.baymin.springboot.store.entity.*;
import com.baymin.springboot.store.repository.IAdminHospitalRelationRepository;
import com.baymin.springboot.store.repository.IAdminRepository;
import com.baymin.springboot.store.repository.IHospitalRepository;
import com.baymin.springboot.store.repository.ISysRoleRepository;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminServiceImpl implements IAdminService {

    @Autowired
    private IAdminRepository adminRepository;

    @Autowired
    private IOrganizationService organizationService;

    @Autowired
    private ISysRoleRepository sysRoleRepository;

    @Autowired
    private IHospitalRepository hospitalRepository;

    @Autowired
    private IAdminHospitalRelationRepository adminHospitalRelationRepository;

    @Autowired
    private IHospitalService hospitalService;

    @Override
    public Admin getAdminByAccount(String account) {
        return adminRepository.findByAccount(account);
    }

    @Override
    public void updateAdmin(Admin admin) {
        adminRepository.save(admin);
    }

    @Override
    public void updateAdminForManage(Admin admin) {
        adminRepository.save(admin);

        // 删除医院信息
        adminHospitalRelationRepository.deleteByAdminId(admin.getId());
        saveHospitalRelations(admin);
    }

    @Override
    public Page<Admin> queryAdminForPage(Pageable pageable, String mobile, String account, String adminName, String hospitalName) {
        BooleanBuilder builder = new BooleanBuilder();

        QAdmin qAdmin = QAdmin.admin;
        if (StringUtils.isNotBlank(mobile)) {
            builder.and(qAdmin.mobile.likeIgnoreCase("%" + mobile + "%"));
        }
        if (StringUtils.isNotBlank(account)) {
            builder.and(qAdmin.account.likeIgnoreCase("%" + account + "%"));
        }
        if (StringUtils.isNotBlank(adminName)) {
            builder.and(qAdmin.adminName.likeIgnoreCase("%" + adminName + "%"));
        }
        if (StringUtils.isNotBlank(hospitalName)) {
            List<Hospital> hospitalList = hospitalService.queryHospitalByName(hospitalName);
            if (CollectionUtils.isEmpty(hospitalList)) {
                builder.and(qAdmin.id.eq("-1"));
            } else {
                List<AdminHospitalRelation> relationList = adminHospitalRelationRepository.findByHospitalIds(hospitalList.stream().map(Hospital::getId).collect(Collectors.toList()));
                if (CollectionUtils.isNotEmpty(relationList)) {
                    builder.and(qAdmin.id.in(relationList.stream().map(AdminHospitalRelation::getAdminId).collect(Collectors.toList())));
                } else {
                    builder.and(qAdmin.id.eq("-1"));
                }
            }
        }
        Page<Admin> adminPage = adminRepository.findAll(builder, pageable);
        if (CollectionUtils.isNotEmpty(adminPage.getContent())) {
            List<Organization> organizationList = organizationService.getAllOrg();
            List<SysRole> roleList = sysRoleRepository.findAllRoles();

            Map<String, Organization> organizationMap = organizationList.stream().collect(Collectors.toMap(Organization::getId, Function.identity()));
            Map<String, SysRole> roleMap = roleList.stream().collect(Collectors.toMap(SysRole::getId, Function.identity()));

            for (Admin admin : adminPage.getContent()) {
                admin.setSysRole(roleMap.get(admin.getRoleId()));
                admin.setOrganization(organizationMap.get(admin.getOrgId()));
            }
        }
        return adminPage;
    }

    @Override
    public Admin getAdminById(String userId) {
        Admin admin = adminRepository.findById(userId).orElse(null);
        if (Objects.isNull(admin)) {
            return null;
        }

        // 查询所有的医院列表
        Iterable<Hospital> hospitalIterable = hospitalRepository.findAll();
        List<Hospital> hospitalList = new ArrayList<>();
        hospitalIterable.forEach(hospitalList::add);

        // 查询用户关联的医院列表
        List<String> checkedHospitalIds = new ArrayList<>();
        List<AdminHospitalRelation> relationList = adminHospitalRelationRepository.findByAdminId(admin.getId());
        if (CollectionUtils.isNotEmpty(relationList)) {
            checkedHospitalIds = relationList.stream().map(AdminHospitalRelation::getHospitalId).collect(Collectors.toList());
        }

        for (Hospital hospital : hospitalList) {
            hospital.setChecked(checkedHospitalIds.contains(hospital.getId()));
        }

        admin.setHospitalList(hospitalList);
        return admin;
    }

    @Override
    public void saveAdmin(Admin admin, Admin sysUser) {
        admin.setCreateTime(new Date());
        admin.setPassword("888888");
        adminRepository.save(admin);

        // 保存医院关联信息
        saveHospitalRelations(admin);
    }

    @Override
    public void deleteAdminById(String userId) {
        adminRepository.deleteById(userId);
    }

    @Override
    public List<Admin> queryAdminByRoleType(String roleType) {
        List<SysRole> roleList = sysRoleRepository.findByRoleType(roleType);
        if (CollectionUtils.isEmpty(roleList)) {
            return new ArrayList<>();
        }

        List<String> roleIds = roleList.stream().map(SysRole::getId).collect(Collectors.toList());

        return adminRepository.findByRoleIds(roleIds);
    }

    private void saveHospitalRelations(Admin admin) {
        // 保存医院关联信息
        if (CollectionUtils.isNotEmpty(admin.getHospitalList())) {
            admin.getHospitalList().stream()
                    .filter(hospital -> StringUtils.isNoneBlank(hospital.getId()))
                    .forEach(hospital -> {
                        AdminHospitalRelation relation = new AdminHospitalRelation();
                        relation.setAdminId(admin.getId());
                        relation.setHospitalId(hospital.getId());
                        adminHospitalRelationRepository.save(relation);
                    });
        }
    }
}
