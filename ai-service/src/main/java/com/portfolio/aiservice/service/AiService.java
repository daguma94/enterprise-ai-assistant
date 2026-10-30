package com.portfolio.aiservice.service;

import com.portfolio.aiservice.dto.AiResponse;

public interface AiService {

    AiResponse askQuestion(String question);

    AiResponse askDocument(Long documentId, String question);

}