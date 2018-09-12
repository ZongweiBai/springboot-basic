package com.baymin.springboot.service;

import com.baymin.springboot.store.entity.BasicItem;
import com.baymin.springboot.store.enumconstant.BasicItemType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IBasicItemService {

    Page<BasicItem> queryItemForPage(Pageable pageable, BasicItemType serviceType);

    void saveItem(BasicItem basicItem);

    BasicItem getItemById(String serviceId);
}
