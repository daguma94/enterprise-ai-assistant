package com.portfolio.aiservice.dto;

import jakarta.validation.constraints.NotBlank;

public record AiRequest(

        @NotBlank(message = "Question cannot be empty")
        String question

) {}