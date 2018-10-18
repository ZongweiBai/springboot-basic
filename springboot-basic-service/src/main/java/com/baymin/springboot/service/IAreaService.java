package com.baymin.springboot.service;

import com.baymin.springboot.store.entity.Area;

import java.util.List;

public interface IAreaService {

    List<Area> getAreaByParentId(String parentId);

}
