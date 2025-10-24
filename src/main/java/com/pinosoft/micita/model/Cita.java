package com.pinosoft.micita.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "citas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cita {
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "id_cita")
	    private Long idCita;
	    
	    @Column(name = "id_cliente", nullable = false)
	    private Long idCliente;
	    
	    @Column(name = "id_trabajador", nullable = false)
	    private Long idTrabajador;
	    
	    @ManyToOne
	    @JoinColumn(name = "id_servicio")
	    private Servicio servicio;
	    
	    @Column(name = "fecha", nullable = false)
	    private LocalDate fecha;
	    
	    @Column(name = "hora_inicio", nullable = false)
	    private LocalTime horaInicio;
	    
	    @Column(name = "hora_fin", nullable = false)
	    private LocalTime horaFin;
	    
	    @Enumerated(EnumType.STRING)
	    @Column(name = "estado", nullable = false)
	    private EstadoCita estado;
	    
	    @Column(name = "created_at", nullable = false)
	    private LocalDateTime createdAt;
	    
	    @PrePersist
	    protected void onCreate() {
	        this.createdAt = LocalDateTime.now();
	        if (this.estado == null) {
	            this.estado = EstadoCita.PENDIENTE;
	        }
	    }
	    
	    public enum EstadoCita {
	        PENDIENTE,
	        CONFIRMADA,
	        CANCELADA,
	        FINALIZADA
	    }
}
