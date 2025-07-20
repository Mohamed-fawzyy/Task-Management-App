package com.trading.task_management.util.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record PaginationResponse<T>(
        int currentPage,
        int totalPages,
        long totalElements,  // Changed from int to long
        int pageSize,
        List<T> data
) {
    public static <T> PaginationResponse<T> of(Page<T> page) {
        return new PaginationResponse<>(
                page.getNumber(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getSize(),
                page.getContent()
        );
    }
}