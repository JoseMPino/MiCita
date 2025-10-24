package com.pinosoft.micita.model;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

@Entity
@Table(name = "negocios")
@Data
public class Negocio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false)
    private String direccion;
    
    @Column(nullable = false)
    private String telefono;
    
    @CreationTimestamp
    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "administrador_id", nullable = false)
    @JsonIgnoreProperties({"contrasena", "negocioId", "creadoEn"}) // Ignora campos sensibles
    private Usuario administrador;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_negocio_id" ,  nullable = false)
    @JsonIgnoreProperties({"negocios", "categoriasServicio"})
    private CategoriaNegocio categoriaNegocio;
}
