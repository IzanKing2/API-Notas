package com.miapp.notasapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miapp.notasapi.model.Nota;
import com.miapp.notasapi.service.NotaService;
import com.miapp.notasapi.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotaController.class)
class NotaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotaService notaService;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAll_deberiaRetornarListaDeNotas() throws Exception {
        Nota nota = new Nota();
        nota.setId(1L);
        nota.setTitulo("Título de prueba");

        when(notaService.getAll()).thenReturn(List.of(nota));

        mockMvc.perform(get("/api/v1/notas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Título de prueba"));
    }

    @Test
    void getById_deberiaRetornarNota() throws Exception {
        Nota nota = new Nota();
        nota.setId(1L);
        nota.setTitulo("Título único");

        when(notaService.getById(1L)).thenReturn(Optional.of(nota));

        mockMvc.perform(get("/api/v1/notas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Título único"));
    }

    @Test
    void getById_conNotaInexistente_deberiaRetornar404() throws Exception {
        when(notaService.getById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/notas/999"))
                .andExpect(status().isNotFound());
    }
}
