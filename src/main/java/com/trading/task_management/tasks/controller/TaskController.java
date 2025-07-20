package com.trading.task_management.tasks.controller;

import com.trading.task_management.tasks.dto.TaskRequest;
import com.trading.task_management.tasks.dto.TaskUpdateRequest;
import com.trading.task_management.tasks.dto.TaskResponse;
import com.trading.task_management.security.config.AuthenticatedUserUtil;
import com.trading.task_management.security.entity.User;
import com.trading.task_management.tasks.service.TaskService;
import com.trading.task_management.util.dto.CustomApiResponse;
import com.trading.task_management.util.dto.PaginationRequest;
import com.trading.task_management.util.dto.PaginationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import org.springframework.data.domain.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Tag(name = "Task Management", description = "Endpoints for managing tasks")
@RestController
@RequestMapping("/api/task-management")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final AuthenticatedUserUtil authenticatedUserUtil;

    @Operation(
            summary = "Get paginated list of tasks for the authenticated user",
            description = "Returns a paginated and sorted list of tasks. Defaults: page=0, size=10, sortBy=dueDate.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Paginated list of tasks returned successfully")
            }
    )
    @GetMapping("/v1/tasks")
    public ResponseEntity<CustomApiResponse<PaginationResponse<TaskResponse>>> getTasks(
            @Parameter(description = "Pagination and sorting parameters", required = true)
            @Valid PaginationRequest pageReq
    ) {
        User user = authenticatedUserUtil.getCurrentUser();
        int page = pageReq.getPage();
        int size = pageReq.getSize();
        pageReq.getSortBy();
        String sortBy = !pageReq.getSortBy().isBlank() ? pageReq.getSortBy() : "dueDate";
        Page<TaskResponse> taskPages = taskService.getTasks(user.getId(), page, size, sortBy);

        String message = taskPages.isEmpty() ?
                "No tasks found for this user." :
                String.format("Successfully retrieved %d tasks (sorted by: %s)",
                        taskPages.getTotalElements(),
                        sortBy
                );

        return ResponseEntity.ok(
                CustomApiResponse.of(
                        HttpStatus.OK.value(),
                        message,
                        PaginationResponse.of(taskPages)
                )
        );
    }

    @Operation(
            summary = "Search tasks by title for the authenticated user",
            description = "Returns a paginated and sorted list of tasks that match the search term in their title. Defaults: page=0, size=10, sortBy=dueDate.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Search results returned successfully")
            }
    )
    @GetMapping("/v1/tasks/search")
    public ResponseEntity<CustomApiResponse<PaginationResponse<TaskResponse>>> searchTasksByTitle(
            @Parameter(description = "Search term for task title", required = true)
            @RequestParam String title,

            @Parameter(description = "Pagination and sorting parameters", required = true)
            @Valid PaginationRequest pageReq
    ) {
        User user = authenticatedUserUtil.getCurrentUser();
        int page = pageReq.getPage();
        int size = pageReq.getSize();
        pageReq.getSortBy();
        String sortBy = !pageReq.getSortBy().isBlank() ? pageReq.getSortBy() : "dueDate";
        Page<TaskResponse> taskPages = taskService.searchTasksByTitle(user.getId(), title, page, size, sortBy);

        String message = taskPages.isEmpty() ?
                String.format("No tasks found matching '%s'.", title) :
                String.format("Found %d tasks matching '%s' (sorted by: %s)",
                        taskPages.getTotalElements(),
                        title,
                        sortBy
                );

        return ResponseEntity.ok(
                CustomApiResponse.of(
                        HttpStatus.OK.value(),
                        message,
                        PaginationResponse.of(taskPages)
                )
        );
    }

    @Operation(
            summary = "Create a new task for the authenticated user",
            description = "Creates a new task and returns the created task.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Task created successfully")
            }
    )
    @PostMapping("/v1/new-task")
    public ResponseEntity<CustomApiResponse<TaskResponse>> createTask(@Valid @RequestBody TaskRequest request) {
        User user = authenticatedUserUtil.getCurrentUser();
        TaskResponse createdTask = taskService.createTaskForUser(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                CustomApiResponse.of(
                        HttpStatus.CREATED.value(),
                        "Task created successfully",
                        createdTask
                )
        );
    }

    @Operation(
            summary = "Update an existing task for the authenticated user",
            description = "Updates the specified task and returns the updated task.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Task updated successfully")
            }
    )
    @PutMapping("/v1/tasks/{id}")
    public ResponseEntity<CustomApiResponse<TaskResponse>> updateTask(
            @Parameter(description = "Task ID", required = true) @PathVariable UUID id,
            @Valid @RequestBody TaskUpdateRequest request) {
        User user = authenticatedUserUtil.getCurrentUser();
        TaskResponse updatedTask = taskService.updateTaskForUser(id, request, user);
        return ResponseEntity.ok(
                CustomApiResponse.of(
                        HttpStatus.OK.value(),
                        "Task updated successfully",
                        updatedTask
                )
        );
    }

    @Operation(
            summary = "Delete a task by ID",
            description = "Deletes the specified task.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Task deleted successfully")
            }
    )
    @DeleteMapping("/v1/tasks/{id}")
    public ResponseEntity<CustomApiResponse<Void>> deleteTask(
            @Parameter(description = "Task ID", required = true) @PathVariable UUID id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok(
                CustomApiResponse.of(
                        HttpStatus.OK.value(),
                        "Task deleted successfully"
                )
        );
    }
}