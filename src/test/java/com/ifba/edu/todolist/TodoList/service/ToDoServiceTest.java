package com.ifba.edu.todolist.TodoList.service;

import com.ifba.edu.todolist.TodoList.builder.ToDoBuilder;
import com.ifba.edu.todolist.TodoList.builder.ToDoDTOBuilder;
import com.ifba.edu.todolist.TodoList.domain.dto.ToDoDTO;
import com.ifba.edu.todolist.TodoList.domain.entity.ToDo;
import com.ifba.edu.todolist.TodoList.enums.Status;
import com.ifba.edu.todolist.TodoList.exceptions.NotFoundException;
import com.ifba.edu.todolist.TodoList.mapper.ToDoMapper;
import com.ifba.edu.todolist.TodoList.mapper.ToDoMapperImpl;
import com.ifba.edu.todolist.TodoList.repository.ToDoRepository;
import com.ifba.edu.todolist.TodoList.service.impl.ToDoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {ToDoServiceImpl.class, ToDoMapperImpl.class})
@ExtendWith(SpringExtension.class)
public class ToDoServiceTest {

    @Autowired
    private ToDoService service;

    @MockBean
    private ToDoRepository repository;

    @MockBean
    private ToDoMapper mapper;

    void comparaTask(ToDoDTO taskEsperada, ToDo taskMock) {
        assertEquals(taskEsperada.getId(), taskMock.getId());
        assertEquals(taskEsperada.getTitulo(), taskMock.getTitulo());
        assertEquals(taskEsperada.getDescricao(), taskMock.getDescricao());
        assertEquals(taskEsperada.getStatus(), taskMock.getStatus());
    }

    @Test
    void deveBuscarTodasTasks() {
        var toDo = ToDoBuilder.buildToDo();
        var toDoDTO = ToDoDTOBuilder.buildRequest();

        given(repository.findAll()).willReturn(List.of(toDo));
        given(mapper.toToDoDTO(toDo)).willReturn(toDoDTO);

        List<ToDoDTO> response = service.findAll();
        var taskEsperada = response.get(0);

        comparaTask(taskEsperada, toDo);
    }

    @Test
    void deveBuscarUmaTaskPorId() throws Exception {
        var toDo = ToDoBuilder.buildToDo();
        var expectedToDoDTO = ToDoDTOBuilder.buildRequest();
        expectedToDoDTO.setId(toDo.getId());

        given(repository.findById(1L)).willReturn(Optional.of(toDo));
        given(mapper.toToDoDTO(toDo)).willReturn(expectedToDoDTO);

        var actualToDoDTO = service.findById(1L);
        comparaTask(actualToDoDTO, toDo);
    }

    @Test
    void deveNaoAcharTaskPorId() {
        given(repository.findById(1L)).willReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.findById(1L));
    }

    @Test
    void deveBuscarTaskPorStatus() {
        var toDo = ToDoBuilder.buildToDo();
        var toDo2 = ToDoBuilder.buildToDo();
        toDo2.setId(2L);
        toDo2.setStatus(Status.PARA_FAZER);

        var expectedToDoDTO1 = ToDoDTOBuilder.buildRequest();
        var expectedToDoDTO2 = ToDoDTOBuilder.buildRequest();
        expectedToDoDTO2.setId(2L);
        expectedToDoDTO2.setStatus(Status.PARA_FAZER);

        var responseEsperada = List.of(toDo, toDo2);
        var expectedDTOList = List.of(expectedToDoDTO1, expectedToDoDTO2);

        given(repository.findByStatus(Status.PARA_FAZER)).willReturn(responseEsperada);
        given(mapper.toToDoDTO(toDo)).willReturn(expectedToDoDTO1);
        given(mapper.toToDoDTO(toDo2)).willReturn(expectedToDoDTO2);

        List<ToDoDTO> response = service.findByStatus(Status.PARA_FAZER);

        assertEquals(response.size(), 2);
        comparaTask(response.get(0), toDo);
        comparaTask(response.get(1), toDo2);
    }

    @Test
    void deveNaoAcharTaskPorStatus() {
        given(repository.findByStatus(Status.PARA_FAZER)).willReturn(List.of());
        List<ToDoDTO> response = service.findByStatus(Status.PARA_FAZER);

        assertEquals(response.size(), 0);
    }

    @Test
    void deveRemoverTask() {
        Long id = 1L;

        doNothing().when(repository).deleteById(id);
        given(repository.existsById(id)).willReturn(true);

        service.delete(id);

        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void deveGerarErroAoTentarRemoverTask() {
        Long id = 1L;
        given(repository.existsById(id)).willReturn(false);
        assertThrows(NotFoundException.class, () -> service.delete(id));
    }

    @Test
    void deveCriarTask() {
        var toDoEsperada = ToDoBuilder.buildToDo();
        var toDoDTO = ToDoDTOBuilder.buildRequest();

        given(mapper.toToDo(toDoDTO)).willReturn(toDoEsperada);
        given(repository.save(any(ToDo.class))).willReturn(toDoEsperada);

        var taskResponseEsperada = ToDoDTOBuilder.buildRequest();
        taskResponseEsperada.setId(toDoEsperada.getId());

        given(mapper.toToDoDTO(toDoEsperada)).willReturn(taskResponseEsperada);

        var taskResponse = service.create(toDoDTO);

        comparaTask(taskResponse, toDoEsperada);
    }

    @Test
    void deveAtualizarTask() {
        var toDoEsperada = ToDoBuilder.buildToDo();
        var toDoDTO = ToDoDTOBuilder.buildRequest();

        given(repository.existsById(toDoDTO.getId())).willReturn(true);
        given(mapper.toToDo(toDoDTO)).willReturn(toDoEsperada);
        given(repository.save(any(ToDo.class))).willReturn(toDoEsperada);

        var taskResponseEsperada = ToDoDTOBuilder.buildRequest();
        taskResponseEsperada.setId(toDoEsperada.getId());

        given(mapper.toToDoDTO(toDoEsperada)).willReturn(taskResponseEsperada);

        var taskResponse = service.update(toDoDTO);
        comparaTask(taskResponse, toDoEsperada);
    }

    @Test
    void deveGerarErroAoAtualizarTask() {
        var toDoDTO = new ToDoDTO();

        given(repository.existsById(toDoDTO.getId())).willReturn(false);
        assertThrows(NotFoundException.class, () -> service.update(toDoDTO));
    }
}