package com.pinosoft.micita.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "trabajador_servicios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrabajadorServicio {

	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    
	    @Column(name = "trabajador_id", nullable = false)
	    private Long trabajadorId;
	    
	    @Column(name = "servicio_id", nullable = false)
	    private Long servicioId;
	    
	    @Column(name = "creado_en", nullable = false)
	    private LocalDateTime creadoEn;
	    
	    @Column(name = "confirmacion_automatica", nullable = false)
	    private Boolean confirmacionAutomatica;
	    
	    @PrePersist
	    protected void onCreate() {
	        this.creadoEn = LocalDateTime.now();
	    }
	
}
