package com.portfolio.aiservice.controller;

import com.portfolio.aiservice.dto.AiRequest;
import com.portfolio.aiservice.dto.AiResponse;
import com.portfolio.aiservice.dto.DocumentQuestionRequest;
import com.portfolio.aiservice.service.AiService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @GetMapping("/test")
    public String test() {
        return "AI Service OK";
    }

    @PostMapping("/ask")
    public AiResponse askQuestion(@Valid @RequestBody AiRequest request) {
        return aiService.askQuestion(request.question());
    }

    @PostMapping("/ask-document")
    public AiResponse askDocument(
            @Valid @RequestBody DocumentQuestionRequest request) {

        return aiService.askDocument(
                request.documentId(),
                request.question()
        );
    }
}