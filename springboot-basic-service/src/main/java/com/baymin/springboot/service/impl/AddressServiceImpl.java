package com.baymin.springboot.service.impl;

import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.common.exception.WebServerException;
import com.baymin.springboot.service.IAddressService;
import com.baymin.springboot.store.entity.Address;
import com.baymin.springboot.store.repository.IAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.baymin.springboot.common.constant.RequestConstant.ADDR_HOME;
import static com.baymin.springboot.common.constant.RequestConstant.ADDR_HOSPITAL;
import static com.baymin.springboot.common.exception.ErrorCode.not_found;
import static com.baymin.springboot.common.exception.ErrorDescription.RECORD_NOT_EXIST;

@Service
@Transactional
public class AddressServiceImpl implements IAddressService {

    @Autowired
    private IAddressRepository addressRepository;

    @Override
    public List<Address> queryUserAddress(String userId) {
        return addressRepository.findByUserId(userId);
    }

    @Override
    public Address saveAddress(Address address) {
        address.setCreateTime(new Date());
        return addressRepository.save(address);
    }

    @Override
    public void deleteAddress(String userId, String addressId) {
        addressRepository.deleteById(addressId);
    }

    @Override
    public Address updateAddress(Address address) {
        Address addressInDB = addressRepository.findById(address.getId()).orElse(null);
        if (Objects.isNull(addressInDB)) {
            throw new WebServerException(HttpStatus.NOT_FOUND, new ErrorInfo(not_found.name(), RECORD_NOT_EXIST));
        }
        addressInDB.setAddressType(address.getAddressType());
        if (ADDR_HOME.equals(addressInDB.getAddressType())) {
            addressInDB.setHospital(null);
            addressInDB.setDepartment(null);
            addressInDB.setHospitalDetail(null);
            addressInDB.setCommunity(address.getCommunity());
            addressInDB.setCommunityDetail(address.getCommunityDetail());
        } else if (ADDR_HOSPITAL.equals(addressInDB.getAddressType())) {
            addressInDB.setCommunity(null);
            addressInDB.setCommunityDetail(null);
            addressInDB.setHospital(address.getHospital());
            addressInDB.setHospitalDetail(address.getHospitalDetail());
            addressInDB.setDepartment(address.getDepartment());
        }
        addressInDB.setCity(address.getCity());
        addressInDB.setProvince(address.getProvince());
        return addressRepository.save(addressInDB);
    }

    @Override
    public Address queryAddressDetail(String addressId) {
        return addressRepository.findById(addressId).orElse(null);
    }
}
