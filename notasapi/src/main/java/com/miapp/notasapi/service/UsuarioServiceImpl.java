package com.miapp.notasapi.service;

import org.springframework.stereotype.Service;

import com.miapp.notasapi.model.Usuario;
import com.miapp.notasapi.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
/**
 * Implementación del servicio de Usuario.
 * Proporciona métodos para gestionar entrenadores en la base de datos.
 */
public class UsuarioServiceImpl extends AbstractCrudService<Usuario, Long> implements UsuarioService {
    private static final Logger log = LoggerFactory.getLogger(UsuarioServiceImpl.class);

    /**
     * Constructor que inicializa el repositorio de usuarios.
     * 
     * @param repo el repositorio de usuarios
     */
    public UsuarioServiceImpl(UsuarioRepository repo) {
        super(repo);
    }

    /**
     * Actualiza completamente un Usuario. Al gestionar la colección de notas, se asegura que Hibernate aplique correctamente la eliminación de huérfanos (orphanRemoval). 
     * Si la colección 'notas' no se incluye en la actualización, Hibernate eliminará las notas que ya no estén asociados al usuario.
     */
    @Override
    @Transactional
    public Usuario update(Long id, Usuario user) {
        log.info("Actualizando usuario con ID: {}", id);
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("Usuario con ID " + id + " no existe.");
        }
        user.setId(id);
        log.info("Usuario actualizado: {}", user);
        return repo.save(user);
    }
}
