package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.Question;
import com.baymin.springboot.store.enumconstant.CareType;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface IQuestionRepository extends PagingAndSortingRepository<Question, String>, JpaSpecificationExecutor<Question> {
    List<Question> findByCareTypeAndQuestionType(CareType careType, String questionType);
}
