package com.pinosoft.micita.model;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name ="reprogramaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reprogramacion {
	
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "id_reprogramacion")
	    private Long idReprogramacion;
	    
	    @Column(name = "id_cita", nullable = false)
	    private Long idCita;
	    
	    @Column(name = "nueva_fecha", nullable = false)
	    private LocalDate nuevaFecha;
	    
	    @Column(name = "nueva_hora_inicio", nullable = false)
	    private LocalTime nuevaHoraInicio;
	    
	    @Column(name = "nueva_hora_fin", nullable = false)
	    private LocalTime nuevaHoraFin;
	    
	    @Column(name = "motivo")
	    private String motivo;
	    
	    @Enumerated(EnumType.STRING)
	    @Column(name = "estado")
	    private EstadoReprogramacion estado;
	    
	    @Column(name = "create_at", nullable = false)
	    private LocalDateTime createAt;
	    
	    @PrePersist
	    protected void onCreate() {
	        this.createAt = LocalDateTime.now();
	    }
	    
	    public enum EstadoReprogramacion {
	        PENDIENTE,
	        APROBADA,
	        RECHAZADA
	    }

}
