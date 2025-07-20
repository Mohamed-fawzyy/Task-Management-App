package com.trading.task_management.security.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record AuthenticationResponse(
        int status,
        String message,
        LocalDateTime localDateTime,
        String accessToken,
        String refreshToken
) {
    public AuthenticationResponse(String accessToken, String refreshToken) {
        this(HttpStatus.OK.value(), "User Authenticated Successfully", LocalDateTime.now(), accessToken, refreshToken);
    }

    public AuthenticationResponse(int status, String message) {
        this(status, message, LocalDateTime.now(), null, null);
    }
}

