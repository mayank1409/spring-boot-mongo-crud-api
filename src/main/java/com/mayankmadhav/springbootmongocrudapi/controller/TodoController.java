package com.mayankmadhav.springbootmongocrudapi.controller;

import com.mayankmadhav.springbootmongocrudapi.model.Todo;
import com.mayankmadhav.springbootmongocrudapi.service.TodoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class TodoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TodoController.class);

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    /*
        CRUD OPERATIONS => CREATE, READ, UPDATE, DELETE
     */

    @PostMapping("/todos")
    public ResponseEntity<Todo> createTodo(@RequestBody Todo todo) {
        LOGGER.info("TodoController.createTodo called {} ", todo);
        Todo savedTodo = todoService.save(todo);
        return new ResponseEntity<>(savedTodo, HttpStatus.CREATED);
    }

    @GetMapping("/todos")
    public ResponseEntity<List<Todo>> findAllTodos() {
        LOGGER.info("TodoController.getAllTodos called");
        List<Todo> todos = todoService.list();
        return new ResponseEntity<>(todos, HttpStatus.OK);
    }

    @PutMapping("/todos/{id}")
    public ResponseEntity<Todo> updateTodo(@RequestBody Todo todo, @PathVariable String id) {
        LOGGER.info("TodoController.updateTodo called with id - {}, todo - {}", id, todo);
        Todo updatedTodo = todoService.update(todo, id);
        return new ResponseEntity<>(updatedTodo, HttpStatus.OK);
    }

    @GetMapping("/todos/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable String id) {
        LOGGER.info("TodoController.getTodoById called with id - {} ", id);
        Todo todo = todoService.findById(id);
        return new ResponseEntity<>(todo, HttpStatus.OK);
    }

    @DeleteMapping("/todos/{id}")
    public ResponseEntity<?> deleteTodo(@PathVariable String id) {
        LOGGER.info("TodoController.deleteTodo called with id - {} ", id);
        todoService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
