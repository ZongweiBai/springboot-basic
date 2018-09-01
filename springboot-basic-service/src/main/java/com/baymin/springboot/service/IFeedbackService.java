package com.baymin.springboot.service;

import com.baymin.springboot.store.entity.Feedback;

public interface IFeedbackService {
    void saveFeedback(Feedback feedback);
}
