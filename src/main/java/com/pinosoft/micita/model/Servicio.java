package com.pinosoft.micita.model;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

@Entity
@Table(name = "servicios")
@Data
public class Servicio {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(nullable = false)
    private Double precio;
    
    @Column(name = "duracion_minutos", nullable = false)
    private Integer duracionMinutos;
    
 
    @ManyToOne
    @JoinColumn(name = "negocio_id", nullable = false)
    private Negocio negocio;
    
    @CreationTimestamp
    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;
    @Column(nullable = false)
    private Boolean estado = true; // Valor por defecto true (activo)
    
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_servicio_id", nullable = false)
    @JsonIgnoreProperties({"servicios"})
    private CategoriaServicio categoriaServicio;

}
