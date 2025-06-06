package com.miapp.notasapi.service;

import com.miapp.notasapi.model.Nota;
import com.miapp.notasapi.repository.NotaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Implementación del servicio de Notas.
 * Proporciona métodos para gestionar notas en la base de datos.
 */
@Service
@Transactional
public class NotaServiceImpl extends AbstractCrudService<Nota, Long> implements NotaService {

    // Logger para registrar eventos en el servicio de Notas.
    private static final Logger log = LoggerFactory.getLogger(NotaServiceImpl.class);
    private final NotaRepository notaRepo;

    public NotaServiceImpl(NotaRepository notaRepo) {
        super(notaRepo);
        this.notaRepo = notaRepo;
    }

    @Override
    public List<Nota> findByUsuarioId(Long usuarioId, Sort sort) {
        log.info("Buscando notas del usuario con ID: {} usando orden: {}", usuarioId, sort);
        return notaRepo.findByUsuarioId(usuarioId, sort);
    }
}
