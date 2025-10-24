package com.pinosoft.micita.model;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "categorias_servicio")
@Data
public class CategoriaServicio {
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    
	    @Column(nullable = false)
	    private String nombre;
	    
	    private String descripcion;
	    
	    @CreationTimestamp
	    @Column(name = "creado_en", updatable = false)
	    private LocalDateTime creadoEn;
	    
	   
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "categoria_negocio_id", nullable = false)
	    @JsonIgnoreProperties({"categoriasServicio", "negocios"}) // Previene ciclos infinitos en JSON
	    private CategoriaNegocio categoriaNegocio;


	    @OneToMany(mappedBy = "categoriaServicio")
	    @JsonIgnoreProperties({"categoriaServicio"})
	    private Set<Servicio> servicios;
}
