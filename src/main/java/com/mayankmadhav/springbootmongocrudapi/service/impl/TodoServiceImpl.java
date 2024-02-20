package com.mayankmadhav.springbootmongocrudapi.service.impl;

import com.mayankmadhav.springbootmongocrudapi.model.Todo;
import com.mayankmadhav.springbootmongocrudapi.repository.TodoRepository;
import com.mayankmadhav.springbootmongocrudapi.service.TodoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TodoServiceImpl implements TodoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TodoServiceImpl.class);

    private final TodoRepository todoRepository;

    public TodoServiceImpl(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public Todo save(Todo todo) {
        LOGGER.info("TodoServiceImpl.save called with {} ", todo);
        todo.setId(UUID.randomUUID().toString().split("-")[0]);
        Todo savedTodo = todoRepository.save(todo);
        LOGGER.info("todo saved successfully");
        return savedTodo;
    }

    @Override
    public List<Todo> list() {
        LOGGER.info("TodoServiceImpl.list called");
        List<Todo> todos = todoRepository.findAll();
        LOGGER.info("Returning todos: {}", todos);
        return todos;
    }

    @Override
    public Todo findById(String id) {
        LOGGER.info("TodoServiceImpl.findById called");
        Todo todo = todoRepository.findById(id).orElseThrow(() -> {
            LOGGER.error("Todo not exists with Id {} ", id);
            return new RuntimeException("Todo not exists");
        });
        LOGGER.info("returning todo {}", todo);
        return todo;
    }

    @Override
    public Todo update(Todo todo, String id) {
        LOGGER.info("TodoServiceImpl.update called");
        Todo todoById = todoRepository.findById(id).orElseThrow(() -> {
            LOGGER.error("Todo not exists with Id {} ", id);
            return new RuntimeException("Todo not exists");
        });
        LOGGER.info("updating todo");
        todoById.setTitle(todo.getTitle());
        todoById.setCompleted(todo.isCompleted());
        todoById.setDescription(todo.getDescription());
        todoById.setDueDate(todo.getDueDate());
        Todo updatedTodo = todoRepository.save(todoById);
        LOGGER.info("Todo updated successfully {}", updatedTodo);
        return updatedTodo;
    }

    @Override
    public void delete(String id) {
        LOGGER.info("TodoServiceImpl.delete called with id {} ", id);
        todoRepository.deleteById(id);
        LOGGER.info("Todo deleted successfully");
    }
}
