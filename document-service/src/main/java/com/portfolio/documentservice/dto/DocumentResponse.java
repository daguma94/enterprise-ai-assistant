package com.portfolio.documentservice.dto;

import java.time.LocalDateTime;

public record DocumentResponse(
        Long id,
        String filename,
        String contentType,
        Long size,
        LocalDateTime uploadedAt
) {
}