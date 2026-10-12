package com.portfolio.userservice.dto;

public record UserResponse(
        Long id,
        String name,
        String email
) {
}