package com.ifba.edu.todolist.TodoList.builder;

import com.ifba.edu.todolist.TodoList.domain.entity.ToDo;
import com.ifba.edu.todolist.TodoList.enums.Status;

import java.util.Date;

public final class ToDoBuilder {

    private ToDoBuilder() {}

    public static ToDo buildToDo() {
        return ToDo.builder()
                .id(1L)
                .titulo("Projeto de Engenharia de Software II")
                .descricao("Finalizar o desenvolvimento do projeto de Engenharia de Software II")
                .status(Status.PARA_FAZER)
                .data_criacao(new Date())
                .build();
    }
}
