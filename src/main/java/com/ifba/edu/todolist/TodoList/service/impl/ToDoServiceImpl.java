package com.ifba.edu.todolist.TodoList.service.impl;

import com.ifba.edu.todolist.TodoList.domain.dto.ToDoDTO;
import com.ifba.edu.todolist.TodoList.domain.entity.ToDo;
import com.ifba.edu.todolist.TodoList.enums.Status;
import com.ifba.edu.todolist.TodoList.exceptions.NotFoundException;
import com.ifba.edu.todolist.TodoList.mapper.ToDoMapper;
import com.ifba.edu.todolist.TodoList.repository.ToDoRepository;
import com.ifba.edu.todolist.TodoList.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ToDoServiceImpl implements ToDoService {

    private final ToDoMapper toDoMapper;
    private final ToDoRepository toDoRepository;


    @Override
    public ToDoDTO create(ToDoDTO toDoDTO) {
        ToDo toDo =toDoRepository.save(toDoMapper.toToDo(toDoDTO));
        return toDoMapper.toToDoDTO(toDo);
    }

    @Override
    public ToDoDTO update(ToDoDTO toDoDTO) {
        if (!toDoRepository.existsById(toDoDTO.getId())){
            throw new NotFoundException("Tarefa não com id "+ toDoDTO.getId()+" não encontrada");
        };
        return create(toDoDTO);
    }

    @Override
    public void delete(Long id) {
        if (!toDoRepository.existsById(id)){
            throw new NotFoundException("Tarefa não com id "+id+" não encontrada");
        };
        toDoRepository.deleteById(id);
    }

    @Override
    public List<ToDoDTO> findAll() {
        List<ToDo> toDo = toDoRepository.findAll();
        List<ToDoDTO> result = new ArrayList<>();

        toDo.forEach(t -> result.add(toDoMapper.toToDoDTO(t)));
        return result;
    }

    @Override
    public ToDoDTO findById(Long id) {
        Optional<ToDo> toDo = toDoRepository.findById(id);

        if (!toDo.isPresent()) {
            throw new NotFoundException("Tarefa não com id " + id + " não encontrada");
        }

        return toDoMapper.toToDoDTO(toDo.get());
    }

    @Override
    public List<ToDoDTO> findByStatus(Status status) {
        List<ToDo> toDo = toDoRepository.findByStatus(status);
        List<ToDoDTO> response = new ArrayList<>();

        toDo.forEach(t -> response.add(toDoMapper.toToDoDTO(t)));
        return response;
    }
}
