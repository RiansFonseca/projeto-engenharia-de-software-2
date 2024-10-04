package com.ifba.edu.todolist.TodoList.domain.dto;

import com.ifba.edu.todolist.TodoList.enums.Status;
import jakarta.validation.constraints.NotEmpty;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ToDoDTO {

    private Long id;
    @NotEmpty
    @Size(max = 100)
    private String titulo;

    @NotNull
    @NotEmpty
    private String descricao;

    @NotNull
    private Status status;

    private Date data_criacao;
}
