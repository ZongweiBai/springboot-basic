package com.baymin.springboot.service.impl;

import com.baymin.springboot.service.ICarePlanService;
import com.baymin.springboot.store.entity.CarePlan;
import com.baymin.springboot.store.entity.QCarePlan;
import com.baymin.springboot.store.enumconstant.CommonStatus;
import com.baymin.springboot.store.repository.ICarePlanRepository;
import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class CarePlanServiceImpl implements ICarePlanService {

    @Autowired
    private ICarePlanRepository carePlanRepository;

    @Override
    public Page<CarePlan> queryCarePlanForPage(String typeId, String caseId, String planDesc, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        QCarePlan qCarePlan = QCarePlan.carePlan;

        if (StringUtils.isNotBlank(typeId)) {
            builder.and(qCarePlan.typeId.eq(typeId));
        }
        if (StringUtils.isNotBlank(caseId)) {
            builder.and(qCarePlan.caseId.eq(caseId));
        }
        if (StringUtils.isNotBlank(planDesc)) {
            builder.and(qCarePlan.planDesc.likeIgnoreCase(Expressions.asString("%").concat(planDesc).concat("%")));
        }
        builder.and(qCarePlan.status.eq(CommonStatus.NORMAL));
        return carePlanRepository.findAll(builder, pageable);
    }

    @Override
    public List<CarePlan> queryCarePlan(String typeId, String caseId, String key) {
        BooleanBuilder builder = new BooleanBuilder();
        QCarePlan qCarePlan = QCarePlan.carePlan;

        if (StringUtils.isNotBlank(typeId)) {
            builder.and(qCarePlan.typeId.eq(typeId));
        }
        if (StringUtils.isNotBlank(caseId)) {
            builder.and(qCarePlan.caseId.eq(caseId));
        }
        if (StringUtils.isNotBlank(key)) {
            builder.and(qCarePlan.planDesc.likeIgnoreCase(Expressions.asString("%").concat(key).concat("%")));
        }
        builder.and(qCarePlan.status.eq(CommonStatus.NORMAL));

        Iterable<CarePlan> iterable = carePlanRepository.findAll(builder);

        List<CarePlan> list = Lists.newArrayList();
        iterable.forEach(list::add);
        return list;
    }

    @Override
    public void saveCarePlan(CarePlan carePlan) {
        if (StringUtils.isNotBlank(carePlan.getId())) {
            CarePlan oldData = getCarePlanById(carePlan.getId());
            if (Objects.nonNull(oldData)) {
                carePlan.setCreateTime(oldData.getCreateTime());
                carePlan.setStatus(oldData.getStatus());
            } else {
                carePlan.setCreateTime(new Date());
                carePlan.setStatus(CommonStatus.NORMAL);
            }
        } else {
            carePlan.setCreateTime(new Date());
            carePlan.setStatus(CommonStatus.NORMAL);
        }

        carePlanRepository.save(carePlan);
    }

    @Override
    public CarePlan getCarePlanById(String planId) {
        return carePlanRepository.findById(planId).orElse(null);
    }

    @Override
    public void changeCarePlanStatus(String planId, CommonStatus status) {
        carePlanRepository.changeStatus(planId, status);
    }
}
