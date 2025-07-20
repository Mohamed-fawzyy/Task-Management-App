package com.trading.task_management.util.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record PaginationRequest(
        @Schema(description = "Page number (0-based)", example = "0")
        @Min(value = 0, message = "Page must be greater than or equal to 0")
        Integer page,

        @Schema(description = "Number of items per page", example = "10")
        @Min(value = 1, message = "Size must be at least 1")
        @Max(value = 100, message = "Size must not exceed 100")
        Integer size,

        @Schema(description = "Field to sort by. For tasks: id, dueDate, title, description, priority, status", example = "dueDate")
        String sortBy
) {
    
    /**
     * Creates a pagination request with custom values, using defaults for null values
     */
    public static PaginationRequest of(Integer page, Integer size, String sortBy) {
        return new PaginationRequest(
                page != null ? page : 0,
                size != null ? size : 10,
                sortBy != null && !sortBy.trim().isEmpty() ? sortBy.trim() : "dueDate"
        );
    }
    
    /**
     * Get page with default value
     */
    public int getPage() {
        return page != null ? page : 0;
    }
    
    /**
     * Get size with default value
     */
    public int getSize() {
        return size != null ? size : 10;
    }
    
    /**
     * Get sortBy with default value
     */
    public String getSortBy() {
        return sortBy != null && !sortBy.trim().isEmpty() ? sortBy.trim() : "dueDate";
    }
}
