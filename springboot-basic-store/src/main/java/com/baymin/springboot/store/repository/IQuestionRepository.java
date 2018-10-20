package com.baymin.springboot.store.repository;

import com.baymin.springboot.store.entity.Question;
import com.baymin.springboot.store.enumconstant.CareType;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IQuestionRepository extends PagingAndSortingRepository<Question, String>,
        JpaSpecificationExecutor<Question>,
        QuerydslPredicateExecutor<Question> {

    List<Question> findByCareTypeAndQuestionType(CareType careType, String questionType);

    List<Question> findByCareType(CareType careType);

    @Query("select vo from Question vo where vo.id in :ids")
    List<Question> findByIds(@Param("ids") List<String> questionIds);
}
