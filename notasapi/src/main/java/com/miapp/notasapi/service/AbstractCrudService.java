package com.miapp.notasapi.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Optional;

@Transactional
/**
 * Implementación del servicio CRUD genérico.
 * Proporciona métodos para gestionar entidades en la base de datos.
 *
 * @param <T>  el tipo de la entidad
 * @param <ID> el tipo del identificador de la entidad
 */
public abstract class AbstractCrudService<T, ID> implements CrudService<T, ID> {
    protected final JpaRepository<T, ID> repo;

    protected AbstractCrudService(JpaRepository<T, ID> repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    @Override
    public List<T> getAll() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<T> getById(ID id) {
        return repo.findById(id);
    }

    @Override
    public T save(T ent) {
        return repo.save(ent);
    }

    @Transactional
    @Override
    public T update(ID id, T ent) {
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("La entidad con ID " + id + " no existe.");
        }
        // Obtiene la entidad existente
        T existing = repo.findById(id).orElseThrow();
        // Copia automáticamente todas las propiedades del objeto entrante 'ent'
        // al objeto existente 'existing', excluyendo el campo 'id' para conservar
        // el identificador original de la entidad.
        BeanUtils.copyProperties(ent, existing, "id");
        return repo.save(existing);
    }

    @Override
    public void deleteById(ID id) {
        repo.deleteById(id);
    }
}