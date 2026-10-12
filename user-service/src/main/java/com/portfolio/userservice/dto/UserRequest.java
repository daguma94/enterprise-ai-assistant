package com.portfolio.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequest(

        @NotBlank
        String name,

        @NotBlank
        @Email
        String email

) {
}