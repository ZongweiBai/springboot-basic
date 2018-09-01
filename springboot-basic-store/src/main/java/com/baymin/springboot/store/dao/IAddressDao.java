package com.baymin.springboot.store.dao;

import com.baymin.springboot.store.entity.Address;

import java.util.List;

public interface IAddressDao {

    Address save(Address address);

    void delete(Address address);

    Address update(Address address);

    Address queryDetail(String addressId);

    List<Address> queryUserAddress(String userId);

}
