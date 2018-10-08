package com.baymin.springboot.store.dao.impl;

import com.baymin.springboot.store.dao.IAddressDao;
import com.baymin.springboot.store.entity.Address;
import com.baymin.springboot.store.repository.IAddressRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Repository
public class AddressDaoImpl implements IAddressDao {

    @Autowired
    @PersistenceContext
    private EntityManager entityManager;

    private JPAQueryFactory queryFactory;

    @PostConstruct
    public void init() {
        queryFactory = new JPAQueryFactory(entityManager);
    }

    @Autowired
    private IAddressRepository addressRepository;

    @Override
    public Address save(Address address) {
        address.setCreateTime(new Date());
        return addressRepository.save(address);
    }

    @Override
    public void delete(Address address) {
        addressRepository.deleteById(address.getId());
    }

    @Override
    public Address update(Address address) {
        return addressRepository.save(address);
    }

    @Override
    public Address queryDetail(String addressId) {
        return addressRepository.findById(addressId).orElse(null);
    }

    @Override
    public List<Address> queryUserAddress(String userId) {
        return addressRepository.findByUserId(userId);
    }

}
