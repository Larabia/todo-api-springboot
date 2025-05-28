package com.larabia.todo_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.larabia.todo_api.dto.TaskPartialUpdateRequest;
import com.larabia.todo_api.dto.TaskRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateTaskSuccessfully() throws Exception {
        TaskRequest request = new TaskRequest(
                "Tarea de integración",
                "Test con MockMvc",
                LocalDate.of(2025, 6, 10)
        );

        mockMvc.perform(post("/api/tasks") //herramienta de Spring que simula un cliente HTTP.
                        .contentType(MediaType.APPLICATION_JSON) //informa que va a enviar un json Content-Type: application/json
                        .content(objectMapper.writeValueAsString(request))) //objectMapper convierte el objeto Java (TaskRequest) a un JSON string.
                .andExpect(status().isCreated()) //Verifica que el código de estado HTTP devuelto sea 201 Created.
                .andExpect(jsonPath("$.id").exists())//Revisa que el body de la respuesta contenga el campo id.
                .andExpect(jsonPath("$.title").value("Tarea de integración")); //Revisa que el campo title tenga exactamente el valor esperado.
    }

    @Test
    void shouldReturnBadRequestWhenTitleIsMissing() throws Exception {

        TaskRequest request = new TaskRequest(
                "",
                "Descripción de prueba",
                LocalDate.of(2023, 1, 1)
        );

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[?(@.field=='title')]").exists())
                .andExpect(jsonPath("$.errors[?(@.field=='dueDate')]").exists());
    }

    @Test
    void shouldGetTaskById() throws Exception {
        // Crear tarea para obtenerla luego
        TaskRequest request = new TaskRequest(
                "Consulta por ID",
                null,
                LocalDate.now().plusDays(1)
        );

        String response = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        long taskId = objectMapper.readTree(response).get("id").asLong(); //Usa el ObjectMapper de Jackson para parsear un String JSON a un árbol de nodos (JsonNode).

        mockMvc.perform(get("/api/tasks/" + taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Consulta por ID"));
    }

    @Test
    void shouldReturn404WhenTaskNotFound() throws Exception {
        mockMvc.perform(get("/api/tasks/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Task with id 99999 not found"));
    }

    @Test
    void shouldDeleteTaskSuccessfully() throws Exception {

        TaskRequest request = new TaskRequest(
                "Tarea para eliminar",
                null,
                LocalDate.now().plusDays(1)
        );

        String response = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        long taskId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/api/tasks/" + taskId))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldMarkTaskAsCompleted() throws Exception {
        // Crear tarea primero
        TaskRequest request = new TaskRequest(
                "Tarea a completar",
                null,
                LocalDate.now().plusDays(1)
        );

        String response = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(patch("/api/tasks/" + id + "/complete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    void shouldReturnBadRequestOnInvalidPartialUpdate() throws Exception {
        // Crear tarea válida
        TaskRequest request = new TaskRequest(
                "Tarea a actualizar",
                null,
                LocalDate.now().plusDays(1)
        );

        String response = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        long id = objectMapper.readTree(response).get("id").asLong();

        // Actualización parcial inválida
        TaskPartialUpdateRequest invalidUpdate = new TaskPartialUpdateRequest("", LocalDate.of(2020, 1, 1));

        mockMvc.perform(patch("/api/tasks/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUpdate)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[?(@.field=='title')]").exists())
                .andExpect(jsonPath("$.errors[?(@.field=='dueDate')]").exists());
    }

    @Test
    void shouldFilterTasksByCompletedStatus() throws Exception {
        // Crear tarea completada
        TaskRequest request = new TaskRequest(
                "Tarea completada",
                null,
                LocalDate.now().plusDays(1)
        );

        String response = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        long id = objectMapper.readTree(response).get("id").asLong();

        // Marcar como completada
        mockMvc.perform(patch("/api/tasks/" + id + "/complete"))
                .andExpect(status().isOk());

        // Filtro por completed = true
        mockMvc.perform(get("/api/tasks?completed=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].id").isNotEmpty());
    }

    @Test
    void shouldGroupTasksByCompletion() throws Exception {
        // Crear tareas: una completada y una no completada
        TaskRequest task1 = new TaskRequest(
                "Tarea completada",
                "Para agrupar",
                LocalDate.now().plusDays(1)
        );

        String response1 = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task1)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        long id1 = objectMapper.readTree(response1).get("id").asLong();

        // Marcarla como completada
        mockMvc.perform(patch("/api/tasks/" + id1 + "/complete"))
                .andExpect(status().isOk());

        // Crear otra tarea no completada
        TaskRequest task2 = new TaskRequest(
                "Tarea no completada",
                "Para agrupar también",
                LocalDate.now().plusDays(1)
        );

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task2)))
                .andExpect(status().isCreated());

        // Llamar al nuevo endpoint de agrupación
        mockMvc.perform(get("/api/tasks/grouped-by-completion"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.true").isArray())
                .andExpect(jsonPath("$.false").isArray())
                .andExpect(jsonPath("$.true[0].title").value("Tarea completada"))
                .andExpect(jsonPath("$.false[0].title").value("Tarea no completada"));
    }
}
