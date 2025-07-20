package com.trading.task_management.tasks.dto;

import java.time.LocalDate;
import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;

public record TaskResponse(
    @Schema(description = "Unique identifier of the task", example = "b3b7c8e2-8c2a-4e2a-9b2a-1a2b3c4d5e6f")
    UUID id,
    @Schema(description = "Title of the task", example = "Finish report")
    String title,
    @Schema(description = "Description of the task", example = "Complete the quarterly financial report")
    String description,
    @Schema(description = "Due date for the task", example = "2024-07-31")
    LocalDate dueDate,
    @Schema(description = "Priority of the task", example = "HIGH", allowableValues = {"LOW", "MEDIUM", "HIGH"})
    String priority,
    @Schema(description = "Status of the task", example = "PENDING", allowableValues = {"PENDING", "IN_PROGRESS", "COMPLETED"})
    String status
) {} 