package com.trading.task_management.tasks.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import io.swagger.v3.oas.annotations.media.Schema;

public record TaskRequest(
//    @Schema(description = "Title of the task", example = "Finish report")
//    @NotBlank
    String title,

//    @Schema(description = "Description of the task", example = "Complete the quarterly financial report")
    String description,

//    @Schema(description = "Due date for the task", example = "2024-07-31")
//    @FutureOrPresent
    LocalDate dueDate,

//    @Schema(description = "Priority of the task", example = "HIGH")
//    @NotNull
    String priority
) {} 