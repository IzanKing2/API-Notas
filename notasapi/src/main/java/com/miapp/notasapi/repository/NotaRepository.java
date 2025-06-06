package com.miapp.notasapi.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import com.miapp.notasapi.model.Nota;

public interface NotaRepository extends JpaRepository<Nota, Long> {
    /**
     * Lista todos las notas de un usuario ordenadamente.
     * 
     * @param usuarioId representa el usuario
     * @param sort representa el orden
     */
    List<Nota> findByUsuarioId(Long usuarioId, Sort sort);
}
