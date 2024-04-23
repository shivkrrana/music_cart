package com.example.ecommerce.controller;

import com.example.ecommerce.dto.FeedbackDto;
import com.example.ecommerce.entity.Feedback;
import com.example.ecommerce.repository.FeedbackRepository;
import com.example.ecommerce.service.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("feedback")
public class FeedbackController {
    @Autowired
    FeedbackService feedbackService;

    @PostMapping("add")
    @ResponseStatus(HttpStatus.CREATED)
    public void addFeedback(@RequestBody @Valid FeedbackDto feedback){
        feedbackService.addFeedback(feedback);
    }
}
