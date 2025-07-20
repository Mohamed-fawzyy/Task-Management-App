package com.trading.task_management.security.dto;

public record LogoutRequest(
        String refreshToken
) {
}
