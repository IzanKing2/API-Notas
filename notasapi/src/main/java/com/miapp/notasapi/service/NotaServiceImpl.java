package com.miapp.notasapi.service;

import com.miapp.notasapi.model.Nota;
import com.miapp.notasapi.repository.NotaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;

/**
 * Implementación del servicio de Notas.
 * Proporciona métodos para gestionar notas en la base de datos.
 */
@Service
@Transactional
public class NotaServiceImpl extends AbstractCrudService<Nota, Long> implements NotaService {

    private final NotaRepository notaRepo;

    public NotaServiceImpl(NotaRepository notaRepo) {
        super(notaRepo);
        this.notaRepo = notaRepo;
    }

    @Override
    public List<Nota> findByUsuarioId(Long usuarioId, Sort sort) {
        return notaRepo.findByUsuarioId(usuarioId, sort);
    }
}
