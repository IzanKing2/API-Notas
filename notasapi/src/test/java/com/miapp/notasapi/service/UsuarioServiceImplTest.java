package com.miapp.notasapi.service;

import com.miapp.notasapi.model.Usuario;
import com.miapp.notasapi.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Mario");
        usuario.setEmail("mario@example.com");
        usuario.setPasswordHash("hash123");
    }

    @Test
    void getAll_deberiaRetornarUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

        List<Usuario> resultado = usuarioService.getAll();

        assertEquals(1, resultado.size());
        assertEquals("Mario", resultado.get(0).getNombre());
    }

    @Test
    void getById_conIdValido_deberiaRetornarUsuario() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Optional<Usuario> resultado = usuarioService.getById(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Mario", resultado.get().getNombre());
    }

    @Test
    void getById_conIdInvalido_deberiaRetornarEmpty() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Usuario> resultado = usuarioService.getById(99L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void save_deberiaGuardarUsuario() {
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario guardado = usuarioService.save(usuario);

        assertNotNull(guardado);
        assertEquals("Mario", guardado.getNombre());
    }

    @Test
    void update_conIdExistente_deberiaActualizarUsuario() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario actualizado = usuarioService.update(1L, usuario);

        assertEquals("Mario", actualizado.getNombre());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void update_conIdInexistente_deberiaLanzarExcepcion() {
        when(usuarioRepository.existsById(99L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> usuarioService.update(99L, usuario));
    }

    @Test
    void deleteById_deberiaEliminarUsuario() {
        doNothing().when(usuarioRepository).deleteById(1L);

        usuarioService.deleteById(1L);

        verify(usuarioRepository).deleteById(1L);
    }
}
