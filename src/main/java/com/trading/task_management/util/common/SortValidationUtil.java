package com.trading.task_management.util.common;

import com.trading.task_management.exceptions.ResourceBadRequestException;
import java.lang.reflect.Field;

public class SortValidationUtil {

    private SortValidationUtil() {
        // Private constructor to prevent instantiation
        throw new AssertionError("Cannot instantiate utility class");
    }

    public static void validateSortField(Class<?> entityClass, String sortBy) {
        try {
            Field field = entityClass.getDeclaredField(sortBy);
            // Just check if the field exists, don't try to access it
            if (field == null) {
                throw new ResourceBadRequestException("Invalid sort field: '" + sortBy +
                        "' for entity: " + entityClass.getSimpleName());
            }
        } catch (NoSuchFieldException | SecurityException e) {
            throw new ResourceBadRequestException("Invalid sort field: '" + sortBy +
                    "' for entity: " + entityClass.getSimpleName());
        }
    }
}
