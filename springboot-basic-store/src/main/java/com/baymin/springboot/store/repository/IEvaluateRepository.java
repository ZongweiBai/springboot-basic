package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.Evaluate;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IEvaluateRepository extends PagingAndSortingRepository<Evaluate, String>, JpaSpecificationExecutor<Evaluate> {
}
