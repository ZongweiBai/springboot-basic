package com.baymin.springboot.service;

import com.baymin.springboot.store.entity.Question;
import com.baymin.springboot.store.enumconstant.CareType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IQuestionService {

    List<Question> queryQuestionByType(String careType, String questionType);

    void saveQuestion(Question question);

    void deleteQuestion(String questionId);

    Question getQuestionById(String questionId);

    Page<Question> queryQuestionForPage(CareType careType, String questionType, Pageable pageable);
}
