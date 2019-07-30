package com.baymin.springboot.service;

import com.baymin.springboot.store.entity.Hospital;
import com.baymin.springboot.store.entity.HospitalDepartment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IHospitalService {
    Page<Hospital> queryQuestionForPage(String hospitalName, Pageable pageable);

    void saveHospital(Hospital hospital);

    void deleteHospital(String hospitalId);

    Hospital getHospitalById(String hospitalId);

    List<Hospital> queryHospitalByName(String hospitalName);

    List<HospitalDepartment> queryHospitalDepartmentByHospital(String hospitalId);

    List<Hospital> getAllHospital();

    List<Hospital> getUserHospital(String adminId);

}
