package com.example.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackDto {
    @NotBlank
    private String feedbackType;
    @NotBlank
    private String feedbackMsg;
}
