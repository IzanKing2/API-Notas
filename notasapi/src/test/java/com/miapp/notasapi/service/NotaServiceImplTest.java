package com.miapp.notasapi.service;

import com.miapp.notasapi.model.Nota;
import com.miapp.notasapi.repository.NotaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotaServiceImplTest {

    @Mock
    private NotaRepository notaRepository;

    @InjectMocks
    private NotaServiceImpl notaService;

    @Test
    void findByUsuarioId_deberiaRetornarListaDeNotas() {
        Long usuarioId = 1L;
        Sort sort = Sort.by(Sort.Direction.DESC, "fechaCreacion");

        Nota nota = new Nota();
        nota.setTitulo("Prueba");

        when(notaRepository.findByUsuarioId(usuarioId, sort)).thenReturn(List.of(nota));

        List<Nota> resultado = notaService.findByUsuarioId(usuarioId, sort);

        assertEquals(1, resultado.size());
        assertEquals("Prueba", resultado.get(0).getTitulo());
        verify(notaRepository, times(1)).findByUsuarioId(usuarioId, sort);
    }
}
