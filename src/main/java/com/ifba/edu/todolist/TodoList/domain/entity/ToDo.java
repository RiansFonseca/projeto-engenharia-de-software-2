package com.ifba.edu.todolist.TodoList.domain.entity;

import com.ifba.edu.todolist.TodoList.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CurrentTimestamp;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "toDoList")
@Builder
public class ToDo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String titulo;
    @Column
    private String descricao;
    @Column
    private Status status;
    @Column
    @CurrentTimestamp
    private Date data_criacao;
}
