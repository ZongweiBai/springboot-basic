package com.baymin.springboot.service.impl;

import com.baymin.springboot.service.IBasicItemService;
import com.baymin.springboot.store.entity.BasicItem;
import com.baymin.springboot.store.entity.QBasicItem;
import com.baymin.springboot.store.enumconstant.BasicItemType;
import com.baymin.springboot.store.repository.IBasicItemRepository;
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
}
