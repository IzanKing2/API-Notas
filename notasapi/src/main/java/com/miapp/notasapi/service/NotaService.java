package com.miapp.notasapi.service;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.miapp.notasapi.model.Nota;

public interface NotaService extends CrudService<Nota, Long> {
    /**
     * Busca notas por el ID del usuario y las ordena según el parámetro de ordenamiento.
     *
     * @param usuarioId el ID del usuario cuyas notas se desean buscar
     * @param sort      el criterio de ordenamiento
     * @return una lista de notas asociadas al usuario, ordenadas según el criterio especificado
     */
    List<Nota> findByUsuarioId(Long usuarioId, Sort sort);
}
