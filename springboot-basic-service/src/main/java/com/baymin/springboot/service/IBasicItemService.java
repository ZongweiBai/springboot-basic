package com.baymin.springboot.service;

import com.baymin.springboot.store.entity.BasicItem;
import com.baymin.springboot.store.entity.ServiceProduct;
import com.baymin.springboot.store.entity.ServiceType;
import com.baymin.springboot.store.enumconstant.BasicItemType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IBasicItemService {

    Page<BasicItem> queryItemForPage(Pageable pageable, BasicItemType serviceType);

    void saveItem(BasicItem basicItem);

    BasicItem getItemById(String serviceId);

    Page<ServiceType> queryServiceTypeForPage(Pageable pageable);

    void saveServiceType(ServiceType serviceType);

    ServiceType getServiceTypeById(String typeId);

    Page<ServiceProduct> queryServiceProductForPage(Pageable pageable);

    void saveServiceProduct(ServiceProduct serviceProduct);

    ServiceProduct getServiceProductById(String productId);

    List<BasicItem> getAllUpcartItems();

    List<ServiceType> getAllServiceType();

    List<ServiceProduct> getServiceProductByTypeId(String serviceTypeId);
}
