package com.ifba.edu.todolist.TodoList.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifba.edu.todolist.TodoList.builder.ToDoDTOBuilder;
import com.ifba.edu.todolist.TodoList.controller.impl.ToDoControllerImpl;
import com.ifba.edu.todolist.TodoList.domain.dto.ToDoDTO;
import com.ifba.edu.todolist.TodoList.enums.Status;
import com.ifba.edu.todolist.TodoList.exceptions.NotFoundException;
import com.ifba.edu.todolist.TodoList.service.ToDoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.ifba.edu.todolist.TodoList.constants.ApiPathConstants.API_TODOLIST;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ToDoControllerImpl.class) // Atualizado para referenciar a implementação do controlador
@AutoConfigureMockMvc
public class ToDoControllerTest {

    @MockBean
    private ToDoService service;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void deveCriarTaskComSucesso() throws Exception {
        var request = ToDoDTOBuilder.buildRequest();
        var responseEsperada = ToDoDTOBuilder.buildResponse();

        given(service.create(Mockito.any(ToDoDTO.class)))
                .willReturn(responseEsperada);

        mockMvc.perform(
                        post(API_TODOLIST)
                                .content(mapper.writeValueAsString(request))
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(responseEsperada)));
    }

    @Test
    void deveRetornarErroComRequestInvalido() throws Exception {
        var request = new ToDoDTO(); // DTO vazio para simular erro de validação

        mockMvc.perform(
                        post(API_TODOLIST)
                                .content(mapper.writeValueAsString(request))
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveAtualizarTaskComSucesso() throws Exception {
        var request = ToDoDTOBuilder.buildRequest();
        var responseEsperada = ToDoDTOBuilder.buildResponse();
        responseEsperada.setId(1L); // Definindo o ID para que a atualização funcione corretamente

        given(service.update(Mockito.any(ToDoDTO.class))).willReturn(responseEsperada);

        mockMvc.perform(
                        put(API_TODOLIST + "/1")  // Atualização correta para usar o ID
                                .content(mapper.writeValueAsString(request))
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(responseEsperada)));
    }

    @Test
    void deveFalharAoTentarAtualizarTask() throws Exception {
        var request = ToDoDTOBuilder.buildRequest();
        given(service.update(any(ToDoDTO.class))).willThrow(new NotFoundException(""));

        mockMvc.perform(
                        put(API_TODOLIST + "/1")
                                .content(mapper.writeValueAsString(request))
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveBuscarTodasAsTasks() throws Exception {
        var t1 = ToDoDTOBuilder.buildResponse();
        t1.setId(1L);
        var t2 = ToDoDTOBuilder.buildResponse();
        t2.setId(2L);
        t2.setTitulo("Projeto de Engenharia de Software II");

        var responseEsperada = List.of(t1, t2);

        given(service.findAll()).willReturn(responseEsperada);

        mockMvc.perform(
                        get(API_TODOLIST)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].titulo").value("Projeto de Engenharia de Software II"));
    }

    @Test
    void deveAcharUmaTask() throws Exception {
        var task = ToDoDTOBuilder.buildResponse(); // Altere para 'buildResponse' se necessário
        task.setId(1L); // Defina o ID esperado
        given(service.findById(1L)).willReturn(task);

        mockMvc.perform(
                        get(API_TODOLIST + "/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.titulo").value("Projeto de Engenharia de Software II"));
    }

    @Test
    void deveFalharAoTentarAcharTask() throws Exception {
        // Muda o comportamento do mock para lançar a exceção quando findById for chamado
        given(service.findById(any(Long.class))).willThrow(new NotFoundException("Tarefa não com id 1 não encontrada"));

        mockMvc.perform(
                        get(API_TODOLIST + "/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Espera um status 404
    }

    @Test
    void deveRemoverTaskComSucesso() throws Exception {
        mockMvc.perform(
                        delete(API_TODOLIST + "/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Mockito.verify(service, Mockito.times(1)).delete(1L);
    }

    @Test
    void deveRemoverSemSucesso() throws Exception {
        Mockito.doThrow(new NotFoundException(null)).when(service).delete(1L);

        mockMvc.perform(
                        delete(API_TODOLIST + "/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveBuscarTasksPorStatus() throws Exception {
        var t1 = ToDoDTOBuilder.buildResponse();
        t1.setId(1L);
        var t2 = ToDoDTOBuilder.buildResponse();
        t2.setId(2L);
        t2.setStatus(Status.PARA_FAZER);

        var responseEsperada = List.of(t1, t2);

        given(service.findByStatus(Status.PARA_FAZER)).willReturn(responseEsperada);

        mockMvc.perform(
                        get(API_TODOLIST + "/status/{status}", "PARA_FAZER")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].status").value("PARA_FAZER")); // Mude para uma string
    }
}