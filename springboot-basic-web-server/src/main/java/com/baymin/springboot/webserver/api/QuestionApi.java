package com.baymin.springboot.webserver.api;

import com.baymin.springboot.common.exception.ErrorCode;
import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.common.exception.WebServerException;
import com.baymin.springboot.service.IQuestionService;
import com.baymin.springboot.store.entity.Question;
import com.baymin.springboot.store.payload.QuestionVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.baymin.springboot.common.exception.ErrorDescription.INVALID_REQUEST;

@Api(value = "陪护问题", tags = "陪护问题")
@RestController
@RequestMapping(path = "/api/question")
public class QuestionApi {

    @Autowired
    private IQuestionService questionService;

    @ApiOperation(value = "根据类型查询陪护问题")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer access_token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "careType", value = "HOSPITAL_CARE：医院陪护  HOME_CARE：居家照护 REHABILITATION：康复护理"),
            @ApiImplicitParam(name = "questionType", value = "DISEASES:问题1 SELF_CARE：问题2 EATING：问题3 CATHETER_CARE：问题4 ASSIST_WITH_MEDICATION：问题5")
    })
    @GetMapping("/{careType}/{questionType}")
    @ResponseBody
    public List<QuestionVo> queryQuestions(@PathVariable String careType,
                                           @PathVariable String questionType) {
        if (Objects.isNull(careType) || Objects.isNull(questionType)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        List<Question> questions = questionService.queryQuestionByType(careType, questionType);
        if (CollectionUtils.isEmpty(questions)) {
            return null;
        }
        List<QuestionVo> questionVos = new ArrayList<>();
        for (Question question : questions) {
            questionVos.add(new QuestionVo(question));
        }
        return questionVos;
    }

}
