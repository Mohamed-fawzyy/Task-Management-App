package com.trading.task_management.tasks.repository;

import com.trading.task_management.tasks.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByUserId(UUID userId);
    Page<Task> findByUserId(UUID id, Pageable pageable);
    Page<Task> findByUserIdAndTitleContainingIgnoreCase(UUID userId, String title, Pageable pageable);
}