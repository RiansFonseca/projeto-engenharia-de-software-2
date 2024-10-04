package com.ifba.edu.todolist.TodoList.service;

import com.ifba.edu.todolist.TodoList.domain.dto.ToDoDTO;
import com.ifba.edu.todolist.TodoList.enums.Status;

import java.util.List;

public interface ToDoService {
    ToDoDTO create(ToDoDTO toDoDTO);

    ToDoDTO update(ToDoDTO toDoDTO);

    void delete(Long id);

    List<ToDoDTO> findAll();

    ToDoDTO findById(Long id);

    List<ToDoDTO> findByStatus(Status status);
}
