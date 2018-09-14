package com.baymin.springboot.service.impl;

import com.baymin.springboot.service.IFeedbackService;
import com.baymin.springboot.store.entity.Feedback;
import com.baymin.springboot.store.repository.IFeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class FeedbackServiceImpl implements IFeedbackService {

    @Autowired
    private IFeedbackRepository feedbackRepository;

    @Override
    public void saveFeedback(Feedback feedback) {
        feedback.setCreateTime(new Date());
        feedbackRepository.save(feedback);
    }
}
