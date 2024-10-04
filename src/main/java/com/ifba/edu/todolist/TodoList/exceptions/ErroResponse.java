package com.ifba.edu.todolist.TodoList.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErroResponse {
    private String titulo;
    private String descricao;
    private List<String> campos;
}
