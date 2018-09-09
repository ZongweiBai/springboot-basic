package com.baymin.springboot.service.impl;

import com.baymin.springboot.service.IBasicServiceService;
import com.baymin.springboot.store.entity.BasicServiceFee;
import com.baymin.springboot.store.entity.QBasicServiceFee;
import com.baymin.springboot.store.enumconstant.BasicServiceType;
import com.baymin.springboot.store.repository.IBasicServiceRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Objects;

@Service
@Transactional
public class BasicServiceServiceImpl implements IBasicServiceService {

    @Autowired
    private IBasicServiceRepository basicServiceRepository;

    @Override
    public Page<BasicServiceFee> queryServiceForPage(Pageable pageable, BasicServiceType serviceType) {
        QBasicServiceFee qBasicServiceFee = QBasicServiceFee.basicServiceFee;

        BooleanExpression booleanExpression = null;
        if (Objects.nonNull(serviceType)) {
            booleanExpression = qBasicServiceFee.basicServiceType.eq(serviceType);
        }
        return basicServiceRepository.findAll(booleanExpression, pageable);
    }

    @Override
    public void saveService(BasicServiceFee basicServiceFee) {
        if (StringUtils.isBlank(basicServiceFee.getId())) {
            basicServiceFee.setCreateTime(new Date());
        } else {
            BasicServiceFee oldData = basicServiceRepository.findById(basicServiceFee.getId()).orElse(null);
            basicServiceFee.setCreateTime(oldData.getCreateTime());
        }
        basicServiceRepository.save(basicServiceFee);
    }

    @Override
    public BasicServiceFee getServiceFeeById(String serviceId) {
        return basicServiceRepository.findById(serviceId).orElse(null);
    }
}
