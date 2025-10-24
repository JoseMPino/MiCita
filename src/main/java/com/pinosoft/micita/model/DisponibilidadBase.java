package com.pinosoft.micita.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "disponibilidad_base")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisponibilidadBase {
	
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "id_disponibilidad_base")
	    private Long idDisponibilidadBase;
	    
	    @Column(name = "id_trabajador", nullable = false)
	    private Long idTrabajador;
	    
	    @Column(name = "dia_semana", nullable = false)
	    private Integer diaSemana; // 1=Lunes, 7=Domingo
	    
	    @Column(name = "hora_inicio", nullable = false)
	    private String horaInicio; // formato HH:mm
	    
	    @Column(name = "hora_fin", nullable = false)
	    private String horaFin; // formato HH:mm
	    
	    @Column(name = "estado", nullable = false)
	    private Boolean estado;

}
