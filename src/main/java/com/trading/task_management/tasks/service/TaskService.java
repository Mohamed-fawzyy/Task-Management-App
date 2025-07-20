package com.trading.task_management.tasks.service;

import com.trading.task_management.tasks.dto.TaskRequest;
import com.trading.task_management.tasks.dto.TaskUpdateRequest;
import com.trading.task_management.tasks.dto.TaskResponse;
import com.trading.task_management.tasks.entity.Task;
import com.trading.task_management.security.entity.User;
import com.trading.task_management.util.dto.PaginationRequest;
import com.trading.task_management.util.dto.PaginationResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface TaskService {

    Task getTaskById(UUID id);
    Task createTask(Task task);
    Task updateTask(UUID id, Task task);
    void deleteTask(UUID id);


    TaskResponse createTaskForUser(TaskRequest request, User user);
    TaskResponse updateTaskForUser(UUID id, TaskUpdateRequest request, User user);
    Page<TaskResponse> getTasks(UUID userId, int page, int size, String sortBy);
}