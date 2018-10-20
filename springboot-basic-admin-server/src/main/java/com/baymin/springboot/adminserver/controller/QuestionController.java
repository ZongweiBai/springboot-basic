package com.baymin.springboot.adminserver.controller;

import com.baymin.springboot.adminserver.constant.WebConstant;
import com.baymin.springboot.service.IQuestionService;
import com.baymin.springboot.store.entity.Admin;
import com.baymin.springboot.store.entity.Question;
import com.baymin.springboot.store.enumconstant.CareType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("question")
public class QuestionController {

    @Autowired
    private IQuestionService questionService;

    @ResponseBody
    @PostMapping(value = "queryQuestionForPage")
    public Map<String, Object> queryQuestionForPage(CareType careType, String questionType,
                                                    Pageable pageable, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();

        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                new Sort(Sort.Direction.DESC, "createTime"));
        Page<Question> queryResult = questionService.queryQuestionForPage(careType, questionType, pageRequest);

        resultMap.put(WebConstant.TOTAL, queryResult.getTotalElements());
        resultMap.put(WebConstant.ROWS, queryResult.getContent());
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "saveQuestion", method = RequestMethod.POST)
    public Map<String, Object> saveQuestion(Question question, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        Admin sysUser = (Admin) request.getSession().getAttribute(WebConstant.ADMIN_USER_SESSION);
        try {
            questionService.saveQuestion(question);
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "保存失败");
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "deleteQuestion", method = RequestMethod.POST)
    public Map<String, Object> deleteQuestion(String questionId, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        Admin sysUser = (Admin) request.getSession().getAttribute(WebConstant.ADMIN_USER_SESSION);
        try {
            questionService.deleteQuestion(questionId);
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "删除失败");
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "getQuestionById", method = RequestMethod.GET)
    public Map<String, Object> getQuestionById(String questionId, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            Question question = questionService.getQuestionById(questionId);
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
            resultMap.put(WebConstant.INFO, question);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "加载出错：" + e.getMessage());
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "queryQuestions", method = RequestMethod.GET)
    public Map<String, Object> queryQuestions(String careType) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            List<Question> questions = questionService.queryQuestionByType(careType);
            Map<String, List<Question>> questionMap = questions.stream().collect(Collectors.groupingBy(Question::getQuestionType));
            resultMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
            resultMap.put(WebConstant.ROWS, questionMap);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put(WebConstant.RESULT, WebConstant.FAULT);
            resultMap.put(WebConstant.MESSAGE, "加载出错：" + e.getMessage());
        }
        return resultMap;
    }

}
