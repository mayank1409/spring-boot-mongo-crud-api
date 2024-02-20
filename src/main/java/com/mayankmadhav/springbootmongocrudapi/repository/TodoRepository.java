package com.mayankmadhav.springbootmongocrudapi.repository;

import com.mayankmadhav.springbootmongocrudapi.model.Todo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends MongoRepository<Todo, String> {
}
