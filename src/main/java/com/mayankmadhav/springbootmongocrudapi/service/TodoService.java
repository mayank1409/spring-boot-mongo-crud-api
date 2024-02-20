package com.mayankmadhav.springbootmongocrudapi.service;

import com.mayankmadhav.springbootmongocrudapi.model.Todo;

import java.util.List;

public interface TodoService {

    Todo save(Todo todo);

    List<Todo> list();

    Todo findById(String id);

    Todo update(Todo todo, String id);

    void delete(String id);

}
