package com.ifba.edu.todolist.TodoList.repository;

import com.ifba.edu.todolist.TodoList.domain.entity.ToDo;
import com.ifba.edu.todolist.TodoList.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ToDoRepository extends JpaRepository<ToDo, Long> {
    List<ToDo> findByStatus(Status status);
}
