package com.trading.task_management.util.dto;

import java.time.Instant;

public record CustomApiResponse<T>(
        int code,
        Instant timestamp,
        String message,
        T response
) {
    public static <T> CustomApiResponse<T> of(int code, String message, T data) {
        return new CustomApiResponse<>(code, Instant.now(), message, data);
    }

    public static CustomApiResponse<Void> of(int code, String message) {
        return new CustomApiResponse<>(code, Instant.now(), message, null);
    }
}
