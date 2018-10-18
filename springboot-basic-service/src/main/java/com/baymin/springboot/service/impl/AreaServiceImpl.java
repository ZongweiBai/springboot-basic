package com.baymin.springboot.service.impl;

import com.baymin.springboot.service.IAreaService;
import com.baymin.springboot.store.entity.Area;
import com.baymin.springboot.store.repository.IAreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AreaServiceImpl implements IAreaService {

    @Autowired
    private IAreaRepository areaRepository;

    @Override
    public List<Area> getAreaByParentId(String parentId) {
        return areaRepository.findByParentId(parentId);
    }
}
