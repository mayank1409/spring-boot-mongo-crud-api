package com.mayankmadhav.springbootmongocrudapi.repository;

import com.mayankmadhav.springbootmongocrudapi.model.Todo;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class TodoRepositoryTests {

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

    @DisplayName("Test Save Todo")
    @Test
    public void givenATodo_WhenSave_ShouldSaveTodo() {
        Todo savedTodo = todoRepository.save(todo1);
        assertThat(savedTodo).isNotNull();
        assertThat(savedTodo.getId()).isNotNull();
    }

    @DisplayName("Test Update Todo")
    @Test
    public void givenATodo_WhenUpdate_ShouldUpdateTodo() {
        Todo savedTodo = todoRepository.save(todo1);

        Todo todoById = todoRepository.findById(savedTodo.getId()).get();

        String updatedTitle = "Test Todo - done";
        String updatedDescription = "Test Todo Description - Done";

        // Updating the data
        todoById.setTitle(updatedTitle);
        todoById.setDescription(updatedDescription);
        todoById.setCompleted(true);

        Todo updatedTodo = todoRepository.save(todoById);

        assertThat(updatedTodo.getTitle()).isEqualTo(updatedTitle);
        assertThat(updatedTodo.getDescription()).isEqualTo(updatedDescription);
        assertThat(updatedTodo.isCompleted()).isTrue();
    }

    @DisplayName("Test List Todos")
    @Test
    public void givenTodos_WhenFindAll_ShouldReturnAllTodos() {
        todoRepository.saveAll(List.of(todo1, todo2));

        List<Todo> todos = todoRepository.findAll();

        assertThat(todos).isNotEmpty();
        assertThat(todos.size()).isEqualTo(2);
    }

    @DisplayName("Test Get Todo By Id")
    @Test
    public void givenTodoId_WhenFindById_ShouldReturnTodo() {
        Todo savedTodo1 = todoRepository.save(todo1);

        Todo todoById = todoRepository.findById(savedTodo1.getId()).get();

        assertThat(todoById).isNotNull();
        assertThat(todoById.getId()).isEqualTo(savedTodo1.getId());
    }

    @DisplayName("Test delete Todo")
    @Test
    public void givenTodo_whenDeleteById_ShouldDeleteTodo() {
        Todo savedTodo = todoRepository.save(todo1);

        todoRepository.deleteById(savedTodo.getId());

        Optional<Todo> todoById = todoRepository.findById(savedTodo.getId());
        assertThat(todoById).isEmpty();
    }

}