package com.miapp.notasapi.service;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz genérica para operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * sobre entidades.
 *
 * @param <T>  el tipo de la entidad
 * @param <ID> el tipo del identificador de la entidad
 */
public interface CrudService<T, ID> {
    /**
     * Obtiene todos los elementos de la entidad.
     *
     * @return una lista de entidades
     */
    List<T> getAll();

    /**
     * Obtiene un elemento de la entidad por su ID.
     *
     * @param id el ID de la entidad
     * @return un objeto Optional que contiene la entidad si se encuentra, o
     *         vacío si no
     */
    Optional<T> getById(ID id);

    /**
     * Guarda una nueva entidad o actualiza una existente.
     *
     * @param entity la entidad a guardar
     * @return la entidad guardada
     */
    T save(T entity);

    /**
     * Actualiza una entidad existente.
     *
     * @param id     el ID de la entidad a actualizar
     * @param entity la entidad con los nuevos datos
     * @return la entidad actualizada
     */
    T update(ID id, T entity);

    /**
     * Elimina una entidad por su ID.
     *
     * @param id el ID de la entidad a eliminar
     */
    void deleteById(ID id);
}
