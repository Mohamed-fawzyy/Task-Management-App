package com.trading.task_management.tasks.service;

import com.trading.task_management.tasks.dto.TaskRequest;
import com.trading.task_management.tasks.dto.TaskUpdateRequest;
import com.trading.task_management.tasks.dto.TaskResponse;
import com.trading.task_management.tasks.entity.Task;
import com.trading.task_management.tasks.repository.TaskRepository;
import com.trading.task_management.security.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.trading.task_management.util.dto.PaginationRequest;
import com.trading.task_management.util.dto.PaginationResponse;
import com.trading.task_management.util.common.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    /**
     * Retrieves a task by its unique identifier.
     *
     * @param id The UUID of the task to retrieve.
     * @return The Task entity.
     * @throws RuntimeException if the task is not found.
     */
    @Override
    public Task getTaskById(UUID id) {
        return taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
    }

    /**
     * Creates and saves a new task entity.
     *
     * @param task The Task entity to be created.
     * @return The saved Task entity.
     */
    @Override
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    /**
     * Updates an existing task with new values.
     *
     * @param id The ID of the task to update.
     * @param updatedTask The Task entity containing updated values.
     * @return The updated Task entity.
     */
    @Override
    public Task updateTask(UUID id, Task updatedTask) {
        Task task = getTaskById(id);
        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setDueDate(updatedTask.getDueDate());
        task.setPriority(updatedTask.getPriority());
        task.setStatus(updatedTask.getStatus());
        return taskRepository.save(task);
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id The UUID of the task to delete.
     */
    @Override
    public void deleteTask(UUID id) {
        taskRepository.deleteById(id);
    }

    /**
     * Retrieves a paginated and sorted list of tasks for the specified user.
     *
     * @param userId The ID of the user whose tasks are to be retrieved.
     * @param page The page number (0-based).
     * @param size The number of items per page.
     * @param sortBy The field to sort by (e.g., "dueDate").
     * @return A Page of TaskResponse DTOs.
     */
    @Override
    public Page<TaskResponse> getTasks(UUID userId, int page, int size, String sortBy) {

        Pageable pageable = PaginationUtil.createPageable(
                page, size, sortBy,
                PaginationUtil.SortFields.TASKS_FIELDS,
                PaginationUtil.DefaultSortFields.TASKS,
                Sort.Direction.ASC
        );
        Page<Task> pageResult = taskRepository.findByUserId(userId, pageable);
        return pageResult.map(this::toResponse);
    }

    /**
     * Searches for tasks by title for the specified user.
     *
     * @param userId The ID of the user whose tasks are to be searched.
     * @param title The search term for the task title.
     * @param page The page number (0-based).
     * @param size The number of items per page.
     * @param sortBy The field to sort by (e.g., "dueDate").
     * @return A Page of TaskResponse DTOs matching the search criteria.
     */
    @Override
    public Page<TaskResponse> searchTasksByTitle(UUID userId, String title, int page, int size, String sortBy) {
        Pageable pageable = PaginationUtil.createPageable(
                page, size, sortBy,
                PaginationUtil.SortFields.TASKS_FIELDS,
                PaginationUtil.DefaultSortFields.TASKS,
                Sort.Direction.ASC
        );
        Page<Task> pageResult = taskRepository.findByUserIdAndTitleContainingIgnoreCase(userId, title, pageable);
        return pageResult.map(this::toResponse);
    }

    /**
     * Creates a new task for the specified user.
     *
     * @param request The task creation request DTO.
     * @param user The user for whom the task is created.
     * @return The created TaskResponse DTO.
     */
    @Override
    public TaskResponse createTaskForUser(TaskRequest request, User user) {
        Task task = toEntity(request);
        task.setUser(user);
        task.setStatus(Task.Status.PENDING);
        return toResponse(createTask(task));
    }

    /**
     * Updates an existing task for the specified user.
     *
     * @param id The ID of the task to update.
     * @param request The task update request DTO.
     * @param user The user who owns the task.
     * @return The updated TaskResponse DTO.
     */
    @Override
    public TaskResponse updateTaskForUser(UUID id, TaskUpdateRequest request, User user) {
        Task task = toEntity(request);
        task.setUser(user);
        return toResponse(updateTask(id, task));
    }

    /**
     * Maps a Task entity to a TaskResponse DTO.
     *
     * @param task The Task entity to convert.
     * @return The corresponding TaskResponse DTO.
     */
    private TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                task.getPriority().name(),
                task.getStatus().name()
        );
    }

    /**
     * Converts a TaskRequest DTO to a Task entity.
     *
     * @param request The TaskRequest DTO.
     * @return The corresponding Task entity.
     */
    private Task toEntity(TaskRequest request) {
        return Task.builder()
                .title(request.title())
                .description(request.description())
                .dueDate(request.dueDate())
                .priority(Task.Priority.valueOf(request.priority()))
                .build();
    }

    /**
     * Converts a TaskUpdateRequest DTO to a Task entity.
     *
     * @param request The TaskUpdateRequest DTO.
     * @return The corresponding Task entity.
     */
    private Task toEntity(TaskUpdateRequest request) {
        return Task.builder()
                .title(request.title())
                .description(request.description())
                .dueDate(request.dueDate())
                .priority(Task.Priority.valueOf(request.priority()))
                .status(Task.Status.valueOf(request.status()))
                .build();
    }
}
