package com.baymin.springboot.service;

import com.baymin.springboot.store.entity.Question;

import java.util.List;

public interface IQuestionService {

    List<Question> queryQuestionByType(String careType, String questionType);

}
