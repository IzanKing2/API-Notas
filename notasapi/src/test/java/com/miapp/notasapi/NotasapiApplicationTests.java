package com.miapp.notasapi;

import com.miapp.notasapi.model.Usuario;
import com.miapp.notasapi.repository.UsuarioRepository;
import com.miapp.notasapi.service.UsuarioServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetById() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("John Doe");
        usuario.setEmail("john.doe@example.com");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Optional<Usuario> result = usuarioService.getById(1L);

        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getNombre());
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    void testSave() {
        Usuario usuario = new Usuario();
        usuario.setNombre("Jane Doe");
        usuario.setEmail("jane.doe@example.com");

        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario result = usuarioService.save(usuario);

        assertNotNull(result);
        assertEquals("Jane Doe", result.getNombre());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void testDeleteById() {
        doNothing().when(usuarioRepository).deleteById(1L);

        usuarioService.deleteById(1L);

        verify(usuarioRepository, times(1)).deleteById(1L);
    }
}
