package com.example.ecommerce.service;

import com.example.ecommerce.dto.FeedbackDto;
import com.example.ecommerce.entity.Feedback;
import com.example.ecommerce.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {

    @Autowired
    FeedbackRepository feedbackRepository;

    public void addFeedback(FeedbackDto feedbackDto){
        Feedback feedback = new Feedback();
        feedback.setFeedbackMsg(feedbackDto.getFeedbackMsg());
        feedback.setFeedbackType(feedbackDto.getFeedbackType());
        feedbackRepository.save(feedback);
    }
}
