package com.portfolio.aiservice.client.dto;

public record DocumentContentResponse(
        Long id,
        String filename,
        String extractedText
) {
}