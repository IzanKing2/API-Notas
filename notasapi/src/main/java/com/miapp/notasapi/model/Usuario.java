package com.miapp.notasapi.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Clase que representa un Usuario.
 * Un usuario puede tener múltiples notas.
 */
@Entity
@Table(name="usuarios")
public class Usuario {
    // Atributos ——————————————————————————————————————
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;

    // 1 Usuario → N Notas
    //@OneToMany(mappedBy = "usuario", // atributo en la otra clase
    //    cascade = CascadeType.ALL, // borra en cascada
    //    orphanRemoval = true // elimina huérfanos
    //)
    //@JsonIgnore
    //private List<Nota> notas = new ArrayList<>();

    @Column(unique = true)
    private String email;

    private String passwordHash;
    // ———————————————————————————————————————————————

    // Constructores —————————————————————————————————
    public Usuario() {
    }

    public Usuario(String nombre, String email, String passwordHash) {
        this.nombre = nombre;
        this.email = email;
        this.passwordHash = passwordHash;
    }
    // ———————————————————————————————————————————————

    // Add/Remove ————————————————————————————————————
    /**
     * Método para añadir una nota
     * @param n representa la nota
     */
    //public void addNota(Nota n) {
    //    notas.add(n);
    //    //n.setUsuario(this);
    //}

    /**
     * Método para eliminar una nota
     * @param n representa la nota
     */
    //public void removeNota(Nota n) {
    //    notas.remove(n);
    //    //n.setUsuario(null);
    //}
    // ——————————————————————————————————————————————

    // Equals y HasCode —————————————————————————————
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario that = (Usuario) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    // —————————————————————————————————————————————

    // Getters y Setters ———————————————————————————
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    // —————————————————————————————————————————————
}
