package com.ifba.edu.todolist.TodoList.controller.impl;

import com.ifba.edu.todolist.TodoList.controller.ToDoController;
import com.ifba.edu.todolist.TodoList.domain.dto.ToDoDTO;
import com.ifba.edu.todolist.TodoList.enums.Status;
import com.ifba.edu.todolist.TodoList.service.ToDoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ToDoControllerImpl implements ToDoController {

    private final ToDoService toDoService;

    @Override
    public ToDoDTO create(@RequestBody @Valid ToDoDTO toDoDTO) {
        return toDoService.create(toDoDTO);
    }

    @Override
    public ToDoDTO update(Long id, @RequestBody @Valid ToDoDTO toDoDTO) {
        toDoDTO.setId(id);
        return toDoService.update(toDoDTO);
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        toDoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public List<ToDoDTO> findAll() {
        return toDoService.findAll();
    }

    @Override
    public ToDoDTO findById(Long id) {
        return toDoService.findById(id);
    }

    @Override
    public List<ToDoDTO> findByStatus(Status status) {
        return toDoService.findByStatus(status);
    }
}
