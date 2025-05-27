package com.larabia.todo_api.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;


import java.time.LocalDate;

public record TaskPartialUpdateRequest(

    @Size(min = 1, message = "Title must not be empty") String title,

    @FutureOrPresent(message = "Due date must be today or in the future") LocalDate dueDate ) { }
