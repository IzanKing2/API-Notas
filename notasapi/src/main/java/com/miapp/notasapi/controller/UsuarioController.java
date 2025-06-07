package com.miapp.notasapi.controller;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.miapp.notasapi.interfaces.Utils;
import com.miapp.notasapi.model.Nota;
import com.miapp.notasapi.model.Usuario;
import com.miapp.notasapi.service.UsuarioService;

import ch.qos.logback.classic.Logger;

@RestController
@RequestMapping("/api/v1/usuarios")
@Validated
public class UsuarioController {
    private static final Logger log = (Logger) LoggerFactory.getLogger(UsuarioController.class);

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * GET /api/v1/usuarios →
     * Obtiene todos los usuarios.
     * 
     * @return ResponseEntity con lista de usuarios o 404 Not Found si no hay
     *         usuarios
     */
    @GetMapping
    public ResponseEntity<List<Usuario>> getAll() {
        log.info("Mostrando todos los usuarios.");
        log.debug("GET /api/v1/usuarios");
        return ResponseEntity.ok(usuarioService.getAll());
    }

    /**
     * GET /api/v1/usuarios/{id} →
     * Obtiene un usuario por su ID.
     * 
     * @param id ID del usuario a buscar
     * @return ResponseEntity con el usuario encontrado o 404 Not Found si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getById(@PathVariable Long id) {
        log.info("Obteniendo usuario con ID: {}", id);
        log.debug("GET /api/v1/usuarios/{}", id);
        return usuarioService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); // Si no lo encuentra se devuelve 404 Not Found
    }

    /**
     * GET /api/v1/usuarios/{id}/notas →
     * Obtiene las notas asociadas a un usuario por su ID.
     * 
     * @param id ID del usuario
     * @return ResponseEntity con la lista de notas del usuario o 404 Not Found si
     *         no existe
     */
    @GetMapping("/{id}/notas")
    public ResponseEntity<List<Nota>> getNotas(@PathVariable Long id) {
        log.info("Obteniendo notas del usuario con ID: {}", id);
        log.debug("GET /api/v1/usuarios/{}/notas", id);
        Usuario user = usuarioService.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Entrenador no encontrado con id " + id));
        // Devolver la lista de notas asociadas
        List<Nota> notas = user.getNotas();
        return ResponseEntity.ok(notas);
    }

    /**
     * POST /api/v1/usuarios →
     * crea un nuevo usuario y devuelve el usuario creado con HTTP 201 Created
     * 
     * @param user Usuario a crear
     * @return ResponseEntity con el usuario creado
     */
    @PostMapping
    public ResponseEntity<Usuario> create(@RequestBody Usuario user) {
        log.info("Creando nuevo usuario: {}", user);
        log.debug("POST /api/v1/usuarios");
        log.debug("Request body: {}", user);
        Usuario created = usuarioService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * PUT /api/v1/usuarios/{id} →
     * Actualiza un usuario existente por su ID.
     * 
     * @param id   ID del usuario a actualizar
     * @param user Usuario con los datos actualizados
     * @return ResponseEntity con el usuario actualizado o 404 Not Found si no
     *         existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        Usuario usuarioActualizado = usuarioService.update(id, usuario);
        if (usuarioActualizado != null) {
            return ResponseEntity.ok(usuarioActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * PUT /api/v1/usuarios/{id}/preserve →
     * Actualiza el nombre de un usuario y preserva sus notas.
     * 
     * @param id   ID del usuario a actualizar
     * @param user Usuario con el nuevo nombre
     * @return ResponseEntity con el usuario actualizado o 404 Not Found si no
     *         existe
     */
    @PutMapping("/{id}/preserve")
    public ResponseEntity<Usuario> updatePreserve(
            @PathVariable Long id,
            @RequestBody Usuario user) {
        log.info("Actualizando nombre del usuario con ID: {} y preservando notas.", id);
        log.debug("PUT /api/v1/usuarios/{}/preserve", id);
        log.debug("Request body: {}", user);
        return usuarioService.getById(id)
                .map(existing -> {
                    // Solo actualizamos el nombre; no tocamos getNotas()
                    existing.setNombre(user.getNombre());
                    Usuario updated = usuarioService.save(existing);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /api/v1/usuarios/{id} →
     * Elimina un usuario por su ID.
     * 
     * @param id ID del usuario a eliminar
     * @return ResponseEntity con estado 204 No Content si se eliminó correctamente
     *         o 404 Not Found si no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        try {
            usuarioService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
