package com.portfolio.aiservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DocumentQuestionRequest(

        @NotNull(message = "Document ID cannot be null")
        @Positive(message = "Document ID must be positive")
        Long documentId,

        @NotBlank(message = "Question cannot be empty")
        String question

) {
}