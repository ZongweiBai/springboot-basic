package com.baymin.springboot.service;

import com.baymin.springboot.store.entity.CarePlan;
import com.baymin.springboot.store.enumconstant.CommonStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICarePlanService {
    Page<CarePlan> queryCarePlanForPage(String typeId, String caseId, Pageable pageable);

    void saveCarePlan(CarePlan carePlan);

    CarePlan getCarePlanById(String planId);

    void changeCarePlanStatus(String planId, CommonStatus status);
}
