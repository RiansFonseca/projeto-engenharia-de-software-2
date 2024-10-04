package com.ifba.edu.todolist.TodoList.builder;

import com.ifba.edu.todolist.TodoList.domain.dto.ToDoDTO;
import com.ifba.edu.todolist.TodoList.enums.Status;

import java.util.Date;

public class ToDoDTOBuilder {

    public static ToDoDTO buildRequest() {
        return ToDoDTO.builder()
                .id(1L)
                .titulo("Projeto de Engenharia de Software II")
                .descricao("Finalizar o desenvolvimento do projeto de Engenharia de Software II")
                .status(Status.PARA_FAZER)
                .data_criacao(new Date())
                .build();
    }

    public static ToDoDTO buildResponse() {
        return ToDoDTO.builder()
                .titulo("Projeto de Engenharia de Software II")
                .descricao("Finalizar o desenvolvimento do projeto de Engenharia de Software II")
                .status(Status.PARA_FAZER)
                .data_criacao(new Date())
                .build();
    }
}
