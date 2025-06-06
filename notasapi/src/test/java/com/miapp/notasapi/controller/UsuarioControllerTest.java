package com.miapp.notasapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miapp.notasapi.model.Usuario;
import com.miapp.notasapi.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Mario");
        usuario.setEmail("mario@example.com");
        usuario.setPasswordHash("123456");
    }

    @Test
    void getAll_deberiaRetornarListaDeUsuarios() throws Exception {
        when(usuarioService.getAll()).thenReturn(List.of(usuario));

        mockMvc.perform(get("/api/v1/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Mario"));
    }

    @Test
    void getById_deberiaRetornarUsuario() throws Exception {
        when(usuarioService.getById(1L)).thenReturn(Optional.of(usuario));

        mockMvc.perform(get("/api/v1/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("mario@example.com"));
    }

    @Test
    void getById_conIdInexistente_deberiaRetornar404() throws Exception {
        when(usuarioService.getById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/usuarios/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void postUsuario_deberiaCrearUsuario() throws Exception {
        when(usuarioService.save(any(Usuario.class))).thenReturn(usuario);

        mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Mario"));
    }

    @Test
    void putUsuario_deberiaActualizarUsuario() throws Exception {
        when(usuarioService.update(eq(1L), any(Usuario.class))).thenReturn(usuario);

        mockMvc.perform(put("/api/v1/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("mario@example.com"));
    }

    @Test
    void deleteUsuario_deberiaEliminarUsuario() throws Exception {
        doNothing().when(usuarioService).deleteById(1L);

        mockMvc.perform(delete("/api/v1/usuarios/1"))
                .andExpect(status().isNoContent());
    }
}

