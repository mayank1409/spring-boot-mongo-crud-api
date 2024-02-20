package com.mayankmadhav.springbootmongocrudapi.service.impl;

import com.mayankmadhav.springbootmongocrudapi.model.Todo;
import com.mayankmadhav.springbootmongocrudapi.repository.TodoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TodoServiceImplTest {

    @InjectMocks
    private TodoServiceImpl todoService;

    @Mock
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

    @DisplayName("Test Save")
    @Test
    void givenATodo_WhenSave_ShouldSaveTodo() {
        Mockito.when(todoRepository.save(any(Todo.class))).thenReturn(todo1);
        Todo savedTodo = todoService.save(todo1);
        assertThat(savedTodo).isNotNull();
        assertThat(savedTodo).isEqualTo(todo1);
    }

    @DisplayName("Test List")
    @Test
    void givenTodos_WhenList_ShouldReturnAllTodos() {
        Mockito.when(todoRepository.findAll()).thenReturn(List.of(todo1, todo2));
        List<Todo> todos = todoService.list();
        assertThat(todos).isNotEmpty();
        assertThat(todos.size()).isEqualTo(2);
    }

    @DisplayName("Test Find by Id")
    @Test
    void givenTodoId_whenFindById_ShouldReturnTodo() {
        Mockito.when(todoRepository.findById(any(String.class))).thenReturn(Optional.of(todo1));
        Todo todo = todoService.findById(todo1.getId());
        assertThat(todo).isNotNull();
        assertThat(todo).isEqualTo(todo1);
    }

    @DisplayName("Test Find by Invalid Id")
    @Test
    void givenInvalidTodoId_whenFindById_ShouldThrowRuntimeException() {
        Mockito.when(todoRepository.findById(any(String.class))).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> todoService.findById("Invalid Todo Id"));
    }

    @DisplayName("Test update by Id")
    @Test
    void givenTodoId_whenFindAndSave_ShouldUpdateTodo() {
        Mockito.when(todoRepository.findById(any(String.class))).thenReturn(Optional.of(todo1));
        Mockito.when(todoRepository.save(any(Todo.class))).thenReturn(todo1);
        Todo updatedTodo = todoService.update(todo1, todo1.getId());
        assertThat(updatedTodo).isNotNull();
        assertThat(updatedTodo).isEqualTo(todo1);
    }

    @DisplayName("Test update by Invalid Id")
    @Test
    void givenInvalidTodoId_whenFindAndSave_ShouldThrowRuntimeException() {
        Mockito.when(todoRepository.findById(any(String.class))).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, ()->todoService.update(todo1, todo1.getId()));
    }

    @DisplayName("Test delete by Id")
    @Test
    void givenTodoId_whenDeleteById_ShouldDeleteTodo() {
        todoService.delete(todo1.getId());
        verify(todoRepository, times(1)).deleteById(any(String.class));
    }
}
