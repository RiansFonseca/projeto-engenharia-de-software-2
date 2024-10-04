package com.ifba.edu.todolist.TodoList.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class DefaultControllerAdvice {

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<ErroResponse> handleNotFoundException (NotFoundException ex){
        ErroResponse erroResponse = new ErroResponse("Recurso não encontrado", ex.getMessage(), null);
        return new ResponseEntity<>(erroResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ErroResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> camposErros = new ArrayList<>();
        for (var error : ex.getFieldErrors()) {
            camposErros.add(String.format("%s: %s", error.getField(), error.getDefaultMessage()));
        }
        ErroResponse error = new ErroResponse("Erro de validação", ex.getMessage(), camposErros);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ErroResponse> handleException(Exception ex) {
        ErroResponse error = new ErroResponse("Erro genérico", ex.getMessage(), null);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
