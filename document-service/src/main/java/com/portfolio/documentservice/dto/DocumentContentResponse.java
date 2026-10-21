package com.portfolio.documentservice.dto;

public record DocumentContentResponse(
        Long id,
        String filename,
        String extractedText
) {
}