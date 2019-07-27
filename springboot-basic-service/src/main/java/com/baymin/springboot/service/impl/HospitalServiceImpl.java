package com.baymin.springboot.service.impl;

import com.baymin.springboot.service.IHospitalService;
import com.baymin.springboot.store.entity.*;
import com.baymin.springboot.store.repository.IAdminHospitalRelationRepository;
import com.baymin.springboot.store.repository.IHospitalDepartmentRepository;
import com.baymin.springboot.store.repository.IHospitalRepository;
import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class HospitalServiceImpl implements IHospitalService {

    @Autowired
    private IHospitalRepository hospitalRepository;

    @Autowired
    private IHospitalDepartmentRepository hospitalDepartmentRepository;

    @Autowired
    private IAdminHospitalRelationRepository adminHospitalRelationRepository;

    @Override
    public Page<Hospital> queryQuestionForPage(String hospitalName, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        QHospital qHospital = QHospital.hospital;
        if (StringUtils.isNotBlank(hospitalName)) {
            builder.and(qHospital.hospitalName.likeIgnoreCase("%" + hospitalName + "%"));
        }
        Page<Hospital> pageResult = hospitalRepository.findAll(builder, pageable);
        if (CollectionUtils.isNotEmpty(pageResult.getContent())) {
            for (Hospital hospital : pageResult.getContent()) {
                List<HospitalDepartment> departments = hospitalDepartmentRepository.findByHospitalId(hospital.getId());
                if (CollectionUtils.isNotEmpty(departments)) {
                    List<String> names = departments.stream().map(HospitalDepartment::getDepartmentName).collect(Collectors.toList());
                    hospital.setDepartmentNames(StringUtils.join(names.toArray(), System.getProperty("line.separator")));
                }
            }
        }

        return pageResult;
    }

    @Override
    public List<Hospital> queryHospitalByName(String hospitalName) {
        BooleanBuilder builder = new BooleanBuilder();

        QHospital qHospital = QHospital.hospital;
        if (StringUtils.isNotBlank(hospitalName)) {
            builder.and(qHospital.hospitalName.likeIgnoreCase("%" + hospitalName + "%"));
        }
        Iterable<Hospital> iterable = hospitalRepository.findAll(builder);
        List<Hospital> list = Lists.newArrayList();
        iterable.forEach(list::add);
        return list;
    }

    @Override
    public List<HospitalDepartment> queryHospitalDepartmentByHospital(String hospitalId) {
        return hospitalDepartmentRepository.findByHospitalId(hospitalId);
    }

    @Override
    public List<Hospital> getAllHospital() {
        Iterable<Hospital> hospitalIterable = hospitalRepository.findAll();
        List<Hospital> hospitalList = new ArrayList<>();
        hospitalIterable.forEach(hospitalList::add);
        return hospitalList;
    }

    @Override
    public List<Hospital> getUserHospital(String adminId) {
        // 查询用户关联的医院列表
        List<String> checkedHospitalIds = new ArrayList<>();
        List<AdminHospitalRelation> relationList = adminHospitalRelationRepository.findByAdminId(adminId);
        if (CollectionUtils.isNotEmpty(relationList)) {
            checkedHospitalIds = relationList.stream().map(AdminHospitalRelation::getHospitalId).collect(Collectors.toList());
        }

        Iterable<Hospital> hospitalIterable = hospitalRepository.findAll();
        List<Hospital> hospitalList = new ArrayList<>();
        for (Hospital hospital : hospitalIterable) {
            // admin用户可以查看所有的
            if (checkedHospitalIds.contains(hospital.getId()) || StringUtils.equals("1", adminId)) {
                hospitalList.add(hospital);
            }
        }
        return hospitalList;
    }

    @Override
    public void saveHospital(Hospital hospital) {
        String departmentNames = hospital.getDepartmentNames();
        if (StringUtils.isNotBlank(hospital.getId())) {
            hospitalDepartmentRepository.deleteByHospitalId(hospital.getId());
        }
        hospital = hospitalRepository.save(hospital);
        String[] departmentArr = StringUtils.split(departmentNames, "\r\n");
        List<HospitalDepartment> departments = new ArrayList<>();
        for (String departmentName : departmentArr) {
            HospitalDepartment hospitalDepartment = new HospitalDepartment();
            hospitalDepartment.setDepartmentName(departmentName);
            hospitalDepartment.setHospitalId(hospital.getId());
            departments.add(hospitalDepartment);
        }
        hospitalDepartmentRepository.saveAll(departments);
    }

    @Override
    public void deleteHospital(String hospitalId) {
        hospitalRepository.deleteById(hospitalId);
        hospitalDepartmentRepository.deleteByHospitalId(hospitalId);
    }

    @Override
    public Hospital getHospitalById(String hospitalId) {
        Hospital hospital = hospitalRepository.findById(hospitalId).orElse(null);
        if (Objects.isNull(hospital)) {
            return null;
        }

        List<HospitalDepartment> departments = hospitalDepartmentRepository.findByHospitalId(hospitalId);
        if (CollectionUtils.isNotEmpty(departments)) {
            List<String> names = departments.stream().map(HospitalDepartment::getDepartmentName).collect(Collectors.toList());
            hospital.setDepartmentNames(StringUtils.join(names.toArray(), "\r\n"));
        }
        return hospital;
    }
}
