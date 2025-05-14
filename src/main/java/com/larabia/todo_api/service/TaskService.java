package com.larabia.todo_api.service;


import com.larabia.todo_api.dto.TaskRequest;
import com.larabia.todo_api.entity.Task;
import com.larabia.todo_api.repository.TaskRepository;
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
}
