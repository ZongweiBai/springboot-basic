package com.baymin.springboot.service.impl;

import com.baymin.springboot.service.IQuestionService;
import com.baymin.springboot.store.entity.Question;
import com.baymin.springboot.store.enumconstant.CareType;
import com.baymin.springboot.store.repository.IQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class QuestionServiceImpl implements IQuestionService {

    @Autowired
    private IQuestionRepository questionRepository;

    @Override
    public List<Question> queryQuestionByType(String careType, String questionType) {
        CareType careTypeEnum = CareType.valueOf(careType);
        return questionRepository.findByCareTypeAndQuestionType(careTypeEnum, questionType);
    }
}
