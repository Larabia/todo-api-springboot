package com.larabia.todo_api.service;


import com.larabia.todo_api.dto.TaskPartialUpdateRequest;
import com.larabia.todo_api.dto.TaskRequest;
import com.larabia.todo_api.entity.Task;
import com.larabia.todo_api.exception.BusinessException;
import com.larabia.todo_api.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public List<Task> getAllTasks() {
       return taskRepository.findAll();
    }

    public Task createTask(TaskRequest request) {
        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .dueDate(request.getDueDate())
                .completed(false) // default
                .build();

        return taskRepository.save(task);
    }

    public Task updateTask(Long id, TaskRequest request) {
        Task existing = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task with id " + id + " not found"));

        if (existing.isCompleted()) {
            throw new BusinessException("Completed tasks cannot be edited.");
        }

        existing.setTitle(request.getTitle());
        existing.setDescription(request.getDescription());
        existing.setDueDate(request.getDueDate());

        return taskRepository.save(existing);
    }

    public Task markAsCompleted(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task with id " + id + " not found"));

        if (task.isCompleted()) {
            throw new BusinessException("Task is already completed.");
        }

        task.setCompleted(true);
        return taskRepository.save(task);
    }

    public Task updateTitleAndDueDate(Long id, TaskPartialUpdateRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task with id " + id + " not found"));

        if (task.isCompleted()) {
            throw new BusinessException("Cannot modify a completed task.");
        }

        if (request.getTitle() != null) {
            task.setTitle(request.getTitle());
        }

        if (request.getDueDate() != null) {
            task.setDueDate(request.getDueDate());
        }

        return taskRepository.save(task);
    }
}
