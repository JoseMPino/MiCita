package com.pinosoft.micita.model;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "categorias_negocio")
@Data
public class CategoriaNegocio {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    
	    @Column(nullable = false, unique = true)
	    private String nombre;
	    
	    private String descripcion;
	    
	    @CreationTimestamp
	    @Column(name = "creado_en", updatable = false)
	    private LocalDateTime creadoEn;
	    

	    @OneToMany(mappedBy = "categoriaNegocio")
	    @JsonIgnore
	    private Set<Negocio> negocios;

	    @OneToMany(mappedBy = "categoriaNegocio")
	    @JsonIgnore
	    private Set<CategoriaServicio> categoriasServicio;

}
