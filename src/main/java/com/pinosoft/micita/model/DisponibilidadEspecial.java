package com.pinosoft.micita.model;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "disponibilidad_especial")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisponibilidadEspecial {
	
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "id_disponibilidad_especial")
	    private Long idDisponibilidadEspecial;
	    
	    @Column(name = "id_trabajador", nullable = false)
	    private Long idTrabajador;
	    
	    @Column(name = "fecha", nullable = false)
	    private LocalDate fecha;
	    
	    @Column(name = "hora_inicio", nullable = false)
	    private String horaInicio;
	    
	    @Column(name = "hora_fin", nullable = false)
	    private String horaFin;
	    
	    @Enumerated(EnumType.STRING)
	    @Column(name = "tipo", nullable = false)
	    private TipoDisponibilidad tipo;
	    
	    @Column(name = "descripcion")
	    private String descripcion;
	    
	    public enum TipoDisponibilidad {
	        BLOQUEO,
	        EXTRA
	    }
}
