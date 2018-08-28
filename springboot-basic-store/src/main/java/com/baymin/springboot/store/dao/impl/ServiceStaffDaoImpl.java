package com.baymin.springboot.store.dao.impl;

import com.baymin.springboot.store.dao.IServiceStaffDao;
import com.baymin.springboot.store.repository.IServiceStaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ServiceStaffDaoImpl implements IServiceStaffDao {

    @Autowired
    private IServiceStaffRepository serviceStaffRepository;

}
