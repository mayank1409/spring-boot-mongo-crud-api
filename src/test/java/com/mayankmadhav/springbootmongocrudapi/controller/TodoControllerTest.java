package com.mayankmadhav.springbootmongocrudapi.controller;

import com.mayankmadhav.springbootmongocrudapi.model.Todo;
import com.mayankmadhav.springbootmongocrudapi.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoControllerTest {

    @InjectMocks
    private TodoController todoController;
    @Mock
    private TodoService todoService;

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


    @Test
    void givenTodo_whenCreateTodo_ShouldSaveTodo() {
        when(todoService.save(any(Todo.class))).thenReturn(todo1);
        ResponseEntity<Todo> todoResponseEntity = todoController.createTodo(todo1);
        assertThat(todoResponseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
    }

    @Test
    void givenTodoList_WhenFindAllTodos_ShouldReturnAllTodo() {
        when(todoService.list()).thenReturn(List.of(todo1, todo2));
        ResponseEntity<List<Todo>> todoResponseEntity = todoController.findAllTodos();
        assertThat(todoResponseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(Objects.requireNonNull(todoResponseEntity.getBody()).size()).isEqualTo(2);
    }

    @Test
    void givenTodoId_WhenUpdateTodo_ShouldUpdateTodo() {
        when(todoService.update(any(Todo.class), any(String.class))).thenReturn(todo1);
        ResponseEntity<Todo> todoResponseEntity = todoController.updateTodo(todo1, todo1.getId());
        assertThat(todoResponseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(Objects.requireNonNull(todoResponseEntity.getBody()).getTitle()).isEqualTo(todo1.getTitle());
    }

    @Test
    void givenInvalidTodoId_WhenUpdateTodo_ShouldThrowRuntimeException() {
        Mockito.doThrow(RuntimeException.class).when(todoService).update(any(Todo.class), any(String.class));
        assertThrows(RuntimeException.class, () -> todoController.updateTodo(todo1, "Invalid Todo id"));
    }

    @Test
    void givenTodoId_WhenFindById_ShouldReturnTodo() {
        when(todoService.findById(any(String.class))).thenReturn(todo1);
        ResponseEntity<Todo> todoByIdResponseEntity = todoController.getTodoById(todo1.getId());
        assertThat(todoByIdResponseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
    }

    @Test
    void givenInvalidTodoId_WhenFindById_ShouldThrowException() {
        Mockito.doThrow(RuntimeException.class).when(todoService).findById(any(String.class));
        assertThrows(RuntimeException.class, () -> todoController.getTodoById("Invalid Todo id"));
    }

    @Test
    void givenTodoId_WhenDeleteTodo_ShouldDeleteTodo() {
        todoController.deleteTodo(todo1.getId());
        verify(todoService, times(1)).delete(any(String.class));
    }

    @Test
    void givenInvalidTodoId_WhenDeleteTodo_ShouldThrowRuntimeException() {
        Mockito.doThrow(RuntimeException.class).when(todoService).delete(any(String.class));
        assertThrows(RuntimeException.class, () -> todoController.deleteTodo("Invalid Todo Id"));
    }
}