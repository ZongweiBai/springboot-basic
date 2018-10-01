package com.baymin.springboot.webserver.api;

import com.baymin.springboot.service.IHospitalService;
import com.baymin.springboot.store.entity.Hospital;
import com.baymin.springboot.store.entity.HospitalDepartment;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "医院接口", tags = "医院接口")
@RestController
@RequestMapping(path = "/api/hospital")
public class HospitalApi {

    @Autowired
    private IHospitalService hospitalService;

    @ApiOperation(value = "根据名称查询医院列表")
    @GetMapping
    @ResponseBody
    public List<Hospital> queryHospitals(@RequestParam(required = false) String hospitalName) {
        List<Hospital> hospitalList = hospitalService.queryHospitalByName(hospitalName);
        return hospitalList;
    }

    @ApiOperation(value = "根据医院ID查询科室列表")
    @GetMapping("/department")
    @ResponseBody
    public List<HospitalDepartment> queryQuestions(@RequestParam String hospitalId) {
        List<HospitalDepartment> departments = hospitalService.queryHospitalDepartmentByHospital(hospitalId);
        return departments;
    }

}
