package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.Address;
import com.baymin.springboot.store.entity.Feedback;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IFeedbackRepository extends PagingAndSortingRepository<Feedback, String>, JpaSpecificationExecutor<Feedback> {
}
