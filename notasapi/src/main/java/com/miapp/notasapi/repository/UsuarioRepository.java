package com.miapp.notasapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.miapp.notasapi.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {}
