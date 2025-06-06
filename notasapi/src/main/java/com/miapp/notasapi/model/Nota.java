package com.miapp.notasapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "notas")
@Data // Incluye getters, setters, toString, equals, hashCode
@NoArgsConstructor
@AllArgsConstructor
public class Nota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título no puede estar vacío")
    @Column(nullable = false)
    private String titulo;

    @Lob
    private String contenido;

    @CreationTimestamp
    private LocalDateTime fechaCreacion;

    @ManyToOne(optional = false)
    @JsonIgnore
    @ToString.Exclude
    private Usuario usuario;
}
