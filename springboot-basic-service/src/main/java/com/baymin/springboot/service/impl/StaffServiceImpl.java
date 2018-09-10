package com.baymin.springboot.service.impl;

import com.baymin.springboot.service.IStaffService;
import com.baymin.springboot.store.entity.Order;
import com.baymin.springboot.store.entity.QServiceStaff;
import com.baymin.springboot.store.entity.ServiceStaff;
import com.baymin.springboot.store.entity.UserProfile;
import com.baymin.springboot.store.enumconstant.CommonStatusType;
import com.baymin.springboot.store.repository.IOrderRepository;
import com.baymin.springboot.store.repository.IServiceStaffRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class StaffServiceImpl implements IStaffService {

    @Autowired
    private IServiceStaffRepository serviceStaffRepository;

    @Autowired
    private IOrderRepository orderRepository;

    @Override
    public Page<UserProfile> queryStaffForPage(Pageable pageable, String userName, String account, String sex) {
        QServiceStaff qStaff = QServiceStaff.serviceStaff;

        BooleanExpression predicate = qStaff.staffStatus.eq(CommonStatusType.NORMAL);
        if (StringUtils.isNotBlank(userName)) {
            predicate.and(qStaff.userName.eq(userName));
        }
        if (StringUtils.isNotBlank(account)) {
            predicate.and(qStaff.mobile.eq(account));
        }
        if (StringUtils.isNotBlank(sex)) {
            predicate.and(qStaff.sex.eq(sex));
        }

        return serviceStaffRepository.findAll(predicate, pageable);
    }

    @Override
    public void saveStaff(ServiceStaff serviceStaff) {
        if (StringUtils.isNotBlank(serviceStaff.getId())) {
            ServiceStaff oldData = serviceStaffRepository.findById(serviceStaff.getId()).orElse(null);
            serviceStaff.setCreateTime(oldData.getCreateTime());
            serviceStaff.setServiceCount(oldData.getServiceCount());
            serviceStaff.setStaffStatus(oldData.getStaffStatus());
        } else {
            serviceStaff.setStaffStatus(CommonStatusType.NORMAL);
            serviceStaff.setCreateTime(new Date());
        }
        serviceStaffRepository.save(serviceStaff);
    }

    @Override
    public ServiceStaff findById(String staffId) {
        return serviceStaffRepository.findById(staffId).orElse(null);
    }

    @Override
    public Map<String, Object> getStaffDetail(String staffId) {
        ServiceStaff staff = serviceStaffRepository.findById(staffId).orElse(null);
        List<Order> orderList = orderRepository.findByServiceStaffIdOrderByOrderTimeDesc(staffId);

        Map<String, Object> detailMap = new HashMap<>();
        detailMap.put("staff", staff);
        detailMap.put("orderList", orderList);
        return detailMap;
    }

    @Override
    public void updateStaffStatus(String staffId, CommonStatusType statusType) {
        serviceStaffRepository.updateStaffStatus(staffId, statusType);
    }
}
