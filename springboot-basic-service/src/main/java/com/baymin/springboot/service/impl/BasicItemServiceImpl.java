package com.baymin.springboot.service.impl;

import com.baymin.springboot.service.IBasicItemService;
import com.baymin.springboot.store.entity.BasicItem;
import com.baymin.springboot.store.entity.QBasicItem;
import com.baymin.springboot.store.entity.ServiceType;
import com.baymin.springboot.store.enumconstant.BasicItemType;
import com.baymin.springboot.store.repository.IBasicItemRepository;
import com.baymin.springboot.store.repository.IServiceTypeRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

@Service
@Transactional
public class BasicItemServiceImpl implements IBasicItemService {

    @Autowired
    private IBasicItemRepository basicItemRepository;

    @Autowired
    private IServiceTypeRepository serviceTypeRepository;

    @Override
    public Page<BasicItem> queryItemForPage(Pageable pageable, BasicItemType itemType) {
        QBasicItem qBasicItem = QBasicItem.basicItem;

        BooleanExpression booleanExpression = null;
        if (Objects.nonNull(itemType)) {
            booleanExpression = qBasicItem.basicItemType.eq(itemType);
        }
        return basicItemRepository.findAll(booleanExpression, pageable);
    }

    @Override
    public void saveItem(BasicItem basicItem) {
        if (StringUtils.isBlank(basicItem.getId())) {
            basicItem.setCreateTime(new Date());
        } else {
            BasicItem oldData = basicItemRepository.findById(basicItem.getId()).orElse(null);
            basicItem.setCreateTime(oldData.getCreateTime());
        }
        basicItemRepository.save(basicItem);
    }

    @Override
    public BasicItem getItemById(String itemId) {
        return basicItemRepository.findById(itemId).orElse(null);
    }

    @Override
    public Page<ServiceType> queryServiceTypeForPage(Pageable pageable) {
        return serviceTypeRepository.findAll(pageable);
    }

    @Override
    public void saveServiceType(ServiceType serviceType) {
        if (StringUtils.isNotBlank(serviceType.getId())) {
            ServiceType oldData = serviceTypeRepository.findById(serviceType.getId()).orElse(null);
            serviceType.setCreateTime(oldData.getCreateTime());
        } else {
            serviceType.setCreateTime(new Date());
        }
        serviceTypeRepository.save(serviceType);
    }

    @Override
    public ServiceType getServiceTypeById(String typeId) {
        return serviceTypeRepository.findById(typeId).orElse(null);
    }
}
