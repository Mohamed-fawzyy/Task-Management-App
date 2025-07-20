package com.trading.task_management.util.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;

/**
 * Utility class for handling pagination and sorting across all endpoints
 */
@Slf4j
public class PaginationUtil {

    private PaginationUtil() {
        // Private constructor to prevent instantiation
        throw new AssertionError("Cannot instantiate utility class");
    }

    /**
     * Creates a Pageable object with validated and sanitized sorting
     * 
     * @param page page number (0-based)
     * @param size page size
     * @param sortBy sort field
     * @param allowedSortFields array of allowed sort fields for the entity
     * @param defaultSortField default sort field if invalid sortBy is provided
     * @param sortDirection sort direction (ASC or DESC)
     * @return Pageable object
     */
    public static Pageable createPageable(int page, int size, String sortBy, 
                                        String[] allowedSortFields, String defaultSortField, 
                                        Sort.Direction sortDirection) {
        
        String validSortBy = validateAndSanitizeSortField(sortBy, allowedSortFields, defaultSortField);
        Sort sort = Sort.by(new Sort.Order(sortDirection, validSortBy).nullsLast());
        
        log.debug("Creating pageable with page: {}, size: {}, sort: {}", page, size, validSortBy);
        
        return PageRequest.of(page, size, sort);
    }

    /**
     * Validates and sanitizes the sort field to prevent injection and ensure valid sorting
     * 
     * @param sortBy the sort field to validate
     * @param allowedSortFields array of allowed sort fields
     * @param defaultSortField default sort field if invalid
     * @return validated and sanitized sort field
     */
    public static String validateAndSanitizeSortField(String sortBy, String[] allowedSortFields, String defaultSortField) {
        if (sortBy == null || sortBy.trim().isEmpty()) {
            return defaultSortField;
        }
        
        // Remove any potential SQL injection attempts and normalize
        String sanitizedSortBy = sortBy.trim().toLowerCase();
        
        // Check if the sort field is allowed
        boolean isValidField = Arrays.asList(allowedSortFields).contains(sanitizedSortBy);
        
        if (!isValidField) {
            log.warn("Invalid sort field '{}' provided, using default '{}'", sortBy, defaultSortField);
            return defaultSortField;
        }
        
        return sanitizedSortBy;
    }

    /**
     * Predefined sort field configurations
     */
    public static final class SortFields {
        public static final String[] TASKS_FIELDS = {
                "id", "dueDate", "title", "description", "priority",
                "status"
        };
    }

    /**
     * Predefined default sort fields
     */
    public static final class DefaultSortFields {
        public static final String TASKS = "dueDate";
    }
} 