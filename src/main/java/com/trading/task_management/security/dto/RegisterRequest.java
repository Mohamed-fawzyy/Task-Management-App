package com.trading.task_management.security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @Pattern(regexp = "^[A-Za-z]+$", message = "First name must contain only letters")
        @NotBlank(message = "First name is required")
        String firstName,

        @Pattern(regexp = "^[A-Za-z]+$", message = "Last name must contain only letters")
        @NotBlank(message = "Last name is required")
        String lastName,

        @NotBlank(message = "Email is required")
        @Pattern(
                regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
                message = "Invalid email format. Email must contain a valid domain like .com or .net"
        )
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{6,}$",
                message = "Password must contain at least one letter, one number, and one special character"
        )
        String password
) {
}
