package com.apress.todo.repository;

import com.apress.todo.domain.ToDo;
import org.springframework.data.repository.CrudRepository;

// http://localhost:8080/api/todo
public interface ToDoRepository extends CrudRepository<ToDo,String> {

}
