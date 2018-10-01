package com.baymin.springboot.service.impl;

import com.baymin.springboot.service.IQuestionService;
import com.baymin.springboot.store.entity.QQuestion;
import com.baymin.springboot.store.entity.Question;
import com.baymin.springboot.store.enumconstant.CareType;
import com.baymin.springboot.store.repository.IQuestionRepository;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

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

    @Override
    public Page<Question> queryQuestionForPage(CareType careType, String questionType, Pageable pageable) {
        QQuestion qQuestion = QQuestion.question;
        BooleanBuilder builder = new BooleanBuilder();

        if (Objects.nonNull(careType)) {
            builder.and(qQuestion.careType.eq(careType));
        }
        if (StringUtils.isNotBlank(questionType)) {
            builder.and(qQuestion.questionType.eq(questionType));
        }

        return questionRepository.findAll(builder, pageable);
    }

    @Override
    public void saveQuestion(Question question) {
        if (StringUtils.isNotBlank(question.getId())) {
            Question oldData = questionRepository.findById(question.getId()).orElse(null);
            if (Objects.nonNull(oldData)) {
                question.setCreateTime(oldData.getCreateTime());
            } else {
                question.setCreateTime(new Date());
            }
        }
        question.setCreateTime(new Date());
        questionRepository.save(question);
    }

    @Override
    public void deleteQuestion(String questionId) {
        questionRepository.deleteById(questionId);
    }

    @Override
    public Question getQuestionById(String questionId) {
        return questionRepository.findById(questionId).orElse(null);
    }
}
