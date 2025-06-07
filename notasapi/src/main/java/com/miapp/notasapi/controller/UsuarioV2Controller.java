package com.miapp.notasapi.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miapp.notasapi.model.Usuario;
import com.miapp.notasapi.service.UsuarioService;

@RestController
@RequestMapping("/api/v2/usuarios")
public class UsuarioV2Controller {
    
    @Autowired
    private final UsuarioService usuarioService;

    public UsuarioV2Controller(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<String> registrar(@RequestBody Usuario usuario) {
        // Validaciones básicas
        if (usuario.getEmail() == null || usuario.getEmail().isBlank() ||
            usuario.getPasswordHash() == null || usuario.getPasswordHash().isBlank()) {
            return ResponseEntity
                    .badRequest()
                    .body("Email y contraseña son obligatorios.");
        }

        // Verifica si ya existe el usuario

        Optional<Usuario> existente = usuarioService.getAll().stream()
                .filter(u -> u.getEmail().equals(usuario.getEmail()))
                .findFirst();
        if (existente.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("El usuario ya está registrado.");
        }

        // Crear usuario
        usuarioService.save(usuario);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Usuario registrado con éxito.");
    }
}
