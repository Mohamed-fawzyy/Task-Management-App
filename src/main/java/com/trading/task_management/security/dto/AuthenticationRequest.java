package com.trading.task_management.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthenticationRequest(
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format") String email,

        @NotBlank(message = "Password is required")
        String password
) {
}
