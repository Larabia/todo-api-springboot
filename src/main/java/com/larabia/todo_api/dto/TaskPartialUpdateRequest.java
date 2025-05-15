package com.larabia.todo_api.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
@Data
public class TaskPartialUpdateRequest {

    @Size(min = 1, message = "Title must not be empty")
    private String title;

    @FutureOrPresent(message = "Due date must be today or in the future")
    private LocalDate dueDate;
}
