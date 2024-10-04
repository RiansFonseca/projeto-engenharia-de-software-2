package com.ifba.edu.todolist.TodoList.mapper;

import com.ifba.edu.todolist.TodoList.domain.dto.ToDoDTO;
import com.ifba.edu.todolist.TodoList.domain.entity.ToDo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class ToDoMapper {
    public abstract ToDoDTO toToDoDTO (ToDo toDo);
    public abstract ToDo toToDo (ToDoDTO toDoDTO);
}
