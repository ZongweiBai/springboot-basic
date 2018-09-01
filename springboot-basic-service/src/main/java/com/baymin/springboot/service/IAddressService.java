package com.baymin.springboot.service;

import com.baymin.springboot.store.entity.Address;

import java.util.List;

public interface IAddressService {

    List<Address> queryUserAddress(String userId);

    Address saveAddress(Address address);

    void deleteAddress(String userId, String addressId);

    Address updateAddress(Address address);

    Address queryAddressDetail(String addressId);
}
