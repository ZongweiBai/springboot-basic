package com.baymin.springboot.service.impl;

import com.baymin.springboot.service.IStaffService;
import com.baymin.springboot.store.entity.*;
import com.baymin.springboot.store.enumconstant.CommonStatus;
import com.baymin.springboot.store.enumconstant.ServiceStaffType;
import com.baymin.springboot.store.enumconstant.ServiceStatus;
import com.baymin.springboot.store.repository.IAreaRepository;
import com.baymin.springboot.store.repository.IOrderRepository;
import com.baymin.springboot.store.repository.IServiceStaffRepository;
import com.baymin.springboot.store.repository.IUserWalletRepository;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class StaffServiceImpl implements IStaffService {

    @Autowired
    private IServiceStaffRepository serviceStaffRepository;

    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private IAreaRepository areaRepository;

    @Autowired
    private IUserWalletRepository userWalletRepository;

    @Override
    public Page<ServiceStaff> queryStaffForPage(Pageable pageable, String userName, String account, String sex) {
        BooleanBuilder builder = new BooleanBuilder();
        QServiceStaff qStaff = QServiceStaff.serviceStaff;

        builder.and(qStaff.staffStatus.eq(CommonStatus.NORMAL));
        if (StringUtils.isNotBlank(userName)) {
            builder.and(qStaff.userName.eq(userName));
        }
        if (StringUtils.isNotBlank(account)) {
            builder.and(qStaff.mobile.eq(account));
        }
        if (StringUtils.isNotBlank(sex)) {
            builder.and(qStaff.sex.eq(sex));
        }

        return serviceStaffRepository.findAll(builder, pageable);
    }

    @Override
    public void saveStaff(ServiceStaff serviceStaff) {
        boolean createUserWallet = false;
        if (StringUtils.isNotBlank(serviceStaff.getId())) {
            ServiceStaff oldData = serviceStaffRepository.findById(serviceStaff.getId()).orElse(null);
            serviceStaff.setCreateTime(oldData.getCreateTime());
            serviceStaff.setServiceCount(oldData.getServiceCount());
            serviceStaff.setServiceOrderCount(oldData.getServiceOrderCount());
            serviceStaff.setStaffStatus(oldData.getStaffStatus());
            serviceStaff.setServiceStatus(oldData.getServiceStatus());
            serviceStaff.setAssignOrderNotification(oldData.getAssignOrderNotification());
            serviceStaff.setIdpId(oldData.getIdpId());
            serviceStaff.setImgUrl(oldData.getImgUrl());
        } else {
            createUserWallet = true;
            serviceStaff.setStaffStatus(CommonStatus.NORMAL);
            serviceStaff.setCreateTime(new Date());
            serviceStaff.setServiceStatus(ServiceStatus.FREE);
            serviceStaff.setServiceCount(0);
            serviceStaff.setServiceOrderCount(0);
            serviceStaff.setAssignOrderNotification(true);
        }

        List<Area> areaList = areaRepository.findByParentId("0");
        Map<String, String> provinceMap = areaList.stream().collect(Collectors.toMap(Area::getAreaId, Area::getAreaName));
        if (StringUtils.isNotBlank(serviceStaff.getBirthplacePid())) {
            StringBuilder birthplace = new StringBuilder(provinceMap.get(serviceStaff.getBirthplacePid()));
            if (StringUtils.isNotBlank(serviceStaff.getBirthplaceCid())) {
                List<Area> subAreaList = areaRepository.findByParentId(serviceStaff.getBirthplacePid());
                Map<String, String> cityMap = subAreaList.stream().collect(Collectors.toMap(Area::getAreaId, Area::getAreaName));
                birthplace.append(" ").append(cityMap.get(serviceStaff.getBirthplaceCid()));
            }
            serviceStaff.setBirthplace(birthplace.toString());
        }
        if (StringUtils.isNotBlank(serviceStaff.getLocationPid())) {
            StringBuilder location = new StringBuilder(provinceMap.get(serviceStaff.getLocationPid()));
            if (StringUtils.isNotBlank(serviceStaff.getBirthplaceCid())) {
                List<Area> subAreaList = areaRepository.findByParentId(serviceStaff.getLocationPid());
                Map<String, String> cityMap = subAreaList.stream().collect(Collectors.toMap(Area::getAreaId, Area::getAreaName));
                location.append(" ").append(cityMap.get(serviceStaff.getLocationCid()));
            }
            serviceStaff.setLocaltion(location.toString());
        }

        serviceStaff = serviceStaffRepository.save(serviceStaff);

        if (createUserWallet) {
            UserWallet userWallet = new UserWallet();
            userWallet.setUserId(serviceStaff.getId());
            userWallet.setBalance(0.00D);
            userWallet.setTotalInWithdrawing(0.00D);
            userWallet.setTotalWithdraw(0.00D);
            userWallet.setTotalIncome(0.00D);
            userWallet.setUserType("S");
            userWalletRepository.save(userWallet);
        }
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
    public void updateStaffStatus(String staffId, CommonStatus statusType, String idCard) {
        serviceStaffRepository.updateStaffStatus(staffId, statusType, idCard);
    }

    @Override
    public List<ServiceStaff> queryStaffByType(ServiceStaffType serviceStaffType) {
//        return serviceStaffRepository.findFreeStaff(serviceStaffType, ServiceStatus.FREE, CommonStatus.NORMAL);
        return serviceStaffRepository.findByStaffType(serviceStaffType, CommonStatus.NORMAL);
    }

    @Override
    public ServiceStaff findByMobile(String userAccount) {
        return serviceStaffRepository.findByMobile(userAccount, CommonStatus.NORMAL);
    }

    @Override
    public void updateIdpId(String id, String openId) {
        serviceStaffRepository.updateIdpId(id, openId);
    }

    @Override
    public ServiceStaff findByIdpId(String openid) {
        return serviceStaffRepository.findByIdpId(openid);
    }

    @Override
    public void changeNotification(String staffId, Boolean enableNotification) {
        serviceStaffRepository.updateAssignOrderNotification(staffId, enableNotification);
    }

    @Override
    public void updateStaff(ServiceStaff staff) {
        serviceStaffRepository.save(staff);
    }
}
