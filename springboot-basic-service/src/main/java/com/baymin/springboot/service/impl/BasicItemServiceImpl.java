package com.baymin.springboot.service.impl;

import com.baymin.springboot.service.IBasicItemService;
import com.baymin.springboot.store.entity.BasicItem;
import com.baymin.springboot.store.entity.QBasicItem;
import com.baymin.springboot.store.entity.ServiceProduct;
import com.baymin.springboot.store.entity.ServiceType;
import com.baymin.springboot.store.enumconstant.BasicItemType;
import com.baymin.springboot.store.enumconstant.CareType;
import com.baymin.springboot.store.enumconstant.CommonStatus;
import com.baymin.springboot.store.repository.IBasicItemRepository;
import com.baymin.springboot.store.repository.IServiceProductRepository;
import com.baymin.springboot.store.repository.IServiceTypeRepository;
import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class BasicItemServiceImpl implements IBasicItemService {

    @Autowired
    private IBasicItemRepository basicItemRepository;

    @Autowired
    private IServiceTypeRepository serviceTypeRepository;

    @Autowired
    private IServiceProductRepository serviceProductRepository;

    @Override
    public Page<BasicItem> queryItemForPage(Pageable pageable, BasicItemType itemType) {
        QBasicItem qBasicItem = QBasicItem.basicItem;

        BooleanBuilder predicate = new BooleanBuilder();
        if (Objects.nonNull(itemType)) {
            predicate.and(qBasicItem.basicItemType.eq(itemType));
        }
        return basicItemRepository.findAll(predicate, pageable);
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

    @Override
    public List<ServiceType> getServiceTypeByType(String careType) {
        CareType careTypeEnum = CareType.valueOf(careType);
        return serviceTypeRepository.findByCareType(careTypeEnum);
    }

    @Override
    public Page<ServiceProduct> queryServiceProductForPage(Pageable pageable) {
        return serviceProductRepository.findAll(pageable);
    }

    @Override
    public void saveServiceProduct(ServiceProduct serviceProduct) {
        if (StringUtils.isBlank(serviceProduct.getId())) {
            serviceProduct.setCreateTime(new Date());
            serviceProduct.setProductStatus(CommonStatus.UPCART);
        } else {
            ServiceProduct oldData = serviceProductRepository.findById(serviceProduct.getId()).orElse(null);
            if (Objects.nonNull(oldData)) {
                serviceProduct.setCreateTime(oldData.getCreateTime());
            } else {
                serviceProduct.setCreateTime(new Date());
            }
        }

        ServiceType serviceType = getServiceTypeById(serviceProduct.getServiceTypeId());
        if (Objects.nonNull(serviceType)) {
            if (serviceType.getCareType() == CareType.REHABILITATION) {
                serviceProduct.setBasicItems(null);
            } else if (serviceType.getCareType() == CareType.HOME_CARE || serviceType.getCareType() == CareType.HOSPITAL_CARE) {
                serviceProduct.setAppointmentNotice(null);
                serviceProduct.setServiceTips(null);
                if (CollectionUtils.isNotEmpty(serviceProduct.getItemList())) {
                    List<String> itemIds = serviceProduct.getItemList().stream().map(BasicItem::getId).collect(Collectors.toList());
                    serviceProduct.setBasicItems(StringUtils.join(itemIds, ","));
                } else {
                    serviceProduct.setBasicItems(null);
                }
            }
        }
        serviceProductRepository.save(serviceProduct);
    }

    @Override
    public ServiceProduct getServiceProductById(String productId) {
        return serviceProductRepository.findById(productId).orElse(null);
    }

    @Override
    public List<BasicItem> getAllUpcartItems() {
        return basicItemRepository.findAllByStatus(CommonStatus.UPCART);
    }

    @Override
    public List<ServiceType> getAllServiceType() {
        Iterable<ServiceType> iterable = serviceTypeRepository.findAll();
        List<ServiceType> list = Lists.newArrayList();
        iterable.forEach(list::add);
        return list;
    }

    @Override
    public List<ServiceProduct> getServiceProductByTypeId(String serviceTypeId) {
        return serviceProductRepository.findByTypeId(serviceTypeId);
    }

    @Override
    public List<ServiceProduct> getServiceProductByTypeIds(List<String> typeIds) {
        return serviceProductRepository.findByTypeIds(typeIds);
    }
}
