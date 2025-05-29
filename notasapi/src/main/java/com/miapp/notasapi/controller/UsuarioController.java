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

    /** GET /api/v1/usuarios → lista todos los usuarios */
    @GetMapping
    public ResponseEntity<List<Usuario>> getAll() {
        log.info("Mostrando todos los usuarios");
        log.debug("GET /api/v1/usuarios");
        return ResponseEntity.ok(usuarioService.getAll());
    }

    /** GET /api/v1/usuarios/{id} → obtiene un entrenador por ID o 404 */
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getById(@PathVariable Long id) {
        log.info("Mostrando usuario con id: {}", id);
        log.debug("GET /api/v1/usuarios/{}", id);
        return usuarioService.getById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build()); // Si no lo encuentra se devuelve 404 Not Found
    }

    /**
     * GET /api/v1/usuarios/{id}/notas
     * Lista todos las notas de un usuario dado.
     */
    @GetMapping("/{id}/notas")
    public ResponseEntity<List<Nota>> getNotas(@PathVariable Long id) {
        log.info("Mostrando notas del usuario con el id: {}", id);
        log.debug("GET /api/v1/usuarios/{}/notas", id);
        Usuario user = usuarioService.getById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Entrenador no encontrado con id " + id));
        // Devolver la lista de notas asociadas
        //List<Nota> notas = user.getNotas();
        return null; //ResponseEntity.ok(notas);
    }
    
    /** POST /api/v1/usuarios → crea un nuevo usuario (201 Created) */
    @PostMapping
    public ResponseEntity<Usuario> create(@RequestBody Usuario user) {
        log.info("Creando nuevo usuario: {}", user);
        log.debug("POST /api/v1/usuarios");
        log.debug("Request body: {}", user);
        Usuario created = usuarioService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /** PUT /api/v1/usuarios/{id} → actualiza un usuario existente o 404 */
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> update(@PathVariable Long id, @RequestBody Usuario user) {
        log.info("Modificando entrenador con id: {}", id);
        log.debug("PUT /api/v1/usuarios/{id}", id);
        log.debug("Request body: {}", user);
        return usuarioService.getById(id)
            .map(existing -> { 
                Usuario updated = usuarioService.update(id, user);
                return ResponseEntity.ok(updated);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * PUT /api/v1/usuarios/{id}/preserve
     * Actualiza únicamente el nombre del Usuario y conserva su lista de notas.
     */
    @PutMapping("/{id}/preserve")
    public ResponseEntity<Usuario> updatePreserve(
            @PathVariable Long id,
            @RequestBody Usuario user) {
        log.info("Modificando nombre de usuario, manteniendo notas, id: {}", id);
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

    /** DELETE /api/v1/usuarios/{id} → elimina un usuario o 404 */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Eliminando usuario con id: {}", id);
        log.debug("DELETE /api/v1/usuarios/{}", id);
        return usuarioService.getById(id)
                .map(existing -> {
                    usuarioService.deleteById(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
