package com.miapp.notasapi.controller;

import com.miapp.notasapi.model.Nota;
import com.miapp.notasapi.model.Usuario;
import com.miapp.notasapi.service.NotaService;
import com.miapp.notasapi.service.UsuarioService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notas")
@Validated
public class NotaController {

    private static final Logger log = (Logger) LoggerFactory.getLogger(NotaController.class);

    private final NotaService notaService;
    private final UsuarioService usuarioService;

    public NotaController(NotaService notaService, UsuarioService usuarioService) {
        this.notaService = notaService;
        this.usuarioService = usuarioService;
    }

    // GET /notas
    // GET /notas?usuarioId=1&order=asc
    @GetMapping
    public List<Nota> getAll(
            @RequestParam(required = false) Long usuarioId,
            @RequestParam(defaultValue = "desc") String order) {
        log.info("Obteniendo todas las notas. UsuarioId: {}, Orden: {}", usuarioId, order);
        Sort sort = Sort.by(order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, "fechaCreacion");

        return usuarioId != null
                ? notaService.findByUsuarioId(usuarioId, sort)
                : notaService.getAll();
    }

    // GET /notas/{id}
    @GetMapping("/{id}")
    public Nota getById(@PathVariable @Positive Long id) {
        log.info("Obteniendo nota con ID: {}", id);
        return notaService.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nota no encontrada"));
    }

    // POST /notas?usuarioId=1
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Nota create(
            @RequestParam @Positive Long usuarioId,
            @Valid @RequestBody Nota nota) {
        log.info("Creando nueva nota para usuario con ID: {}", usuarioId);
        log.debug("POST /api/v1/notas?usuarioId={}", usuarioId);
        log.debug("Request body: {}", nota);
        Usuario usuario = usuarioService.getById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        nota.setUsuario(usuario);
        return notaService.save(nota);
    }

    // PUT /notas/{id}
    @PutMapping("/{id}")
    public Nota update(
            @PathVariable @Positive Long id,
            @Valid @RequestBody Nota nota) {
        log.info("Actualizando nota con ID: {}", id);
        log.debug("PUT /api/v1/notas/{}", id);
        log.debug("Request body: {}", nota);
        Nota existente = notaService.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nota no encontrada"));

        nota.setId(id);
        nota.setUsuario(existente.getUsuario());
        return notaService.update(id, nota);
    }

    // DELETE /notas/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive Long id) {
        log.info("Eliminando nota con ID: {}", id);
        if (!notaService.getById(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nota no encontrada");
        }
        notaService.deleteById(id);
    }
}
