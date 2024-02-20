package com.mayankmadhav.springbootmongocrudapi.intergartion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mayankmadhav.springbootmongocrudapi.model.Todo;
import com.mayankmadhav.springbootmongocrudapi.repository.TodoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TodoControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TodoRepository todoRepository;

    private Todo todo1;
    private Todo todo2;

    @BeforeEach
    void setUp() {
        todo1 = new Todo();
        todo1.setId(UUID.randomUUID().toString().split("-")[0]);
        todo1.setTitle("Test Todo");
        todo1.setDescription("Test Todo Description");
        todo1.setCompleted(false);
        todo1.setDueDate(LocalDateTime.now().plusYears(1));

        todo2 = new Todo();
        todo2.setId(UUID.randomUUID().toString().split("-")[0]);
        todo2.setTitle("Test Todo 2");
        todo2.setDescription("Test Todo Description 2");
        todo2.setCompleted(false);
        todo2.setDueDate(LocalDateTime.now().plusYears(2));
    }

    @AfterEach
    public void tearDown() {
        todoRepository.deleteAll();
    }

    @Test
    void givenNewTodo_whenCreateTodo_ShouldSaveTodo() throws Exception {
        mockMvc.perform(post("/api/v1/todos").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todo1)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void givenSavedTodos_whenFindAll_ShouldReturnAllTodos() throws Exception {
        mockMvc.perform(get("/api/v1/todos").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void givenExistingTodoAndId_WhenUpdate_ShouldUpdateTodo() throws Exception {
        todoRepository.save(todo1);
        Todo todoById = todoRepository.findById(todo1.getId()).get();

        String updatedTitle = "Test Todo - Done";
        String updatedDescription = "Test Todo Description - Done";

        todoById.setTitle(updatedTitle);
        todoById.setDescription(updatedDescription);
        todoById.setCompleted(true);

        mockMvc.perform(put("/api/v1/todos/{id}", todoById.getId())
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(todoById)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void givenTodoId_whenFindById_ShouldReturnTodo() throws Exception {
        Todo savedTodo = todoRepository.save(todo1);

        mockMvc.perform(get("/api/v1/todos/{id}", savedTodo.getId()).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void givenTodoId_whenDeleteTodo_ShouldDeleteTodo() throws Exception {
        Todo savedTodo = todoRepository.save(todo1);

        mockMvc.perform(delete("/api/v1/todos/{id}", savedTodo.getId()).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
