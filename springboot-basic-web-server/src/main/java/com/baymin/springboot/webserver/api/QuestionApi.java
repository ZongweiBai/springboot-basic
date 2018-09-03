package com.baymin.springboot.webserver.api;

import com.baymin.springboot.common.exception.ErrorCode;
import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.common.exception.WebServerException;
import com.baymin.springboot.service.IQuestionService;
import com.baymin.springboot.store.entity.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static com.baymin.springboot.common.exception.ErrorDescription.INVALID_REQUEST;

@RestController
@RequestMapping(path = "/api/question")
public class QuestionApi {

    @Autowired
    private IQuestionService questionService;

    @GetMapping("/{careType}/{questionType}")
    @ResponseBody
    public List<Question> queryQuestions(@PathVariable String careType,
                                             @PathVariable String questionType) {
        if (Objects.isNull(careType) || Objects.isNull(questionType)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        return questionService.queryQuestionByType(careType, questionType);
    }

}
