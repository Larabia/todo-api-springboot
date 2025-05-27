package com.larabia.todo_api.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;


import java.time.LocalDate;

public record TaskRequest(
        @NotBlank(message = "Title is required") String title,
        String description,
        @FutureOrPresent(message = "Due date must be today or in the future") LocalDate dueDate
) {}
