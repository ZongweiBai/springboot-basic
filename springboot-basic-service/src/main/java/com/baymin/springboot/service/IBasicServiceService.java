package com.baymin.springboot.service;

import com.baymin.springboot.store.entity.BasicServiceFee;
import com.baymin.springboot.store.enumconstant.BasicServiceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IBasicServiceService {

    Page<BasicServiceFee> queryServiceForPage(Pageable pageable, BasicServiceType serviceType);

    void saveService(BasicServiceFee basicServiceFee);

    BasicServiceFee getServiceFeeById(String serviceId);
}
