package com.miapp.notasapi.service;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.miapp.notasapi.model.Nota;

public interface NotaService extends CrudService<Nota, Long> {
    List<Nota> findByUsuarioId(Long usuarioId, Sort sort);
}
