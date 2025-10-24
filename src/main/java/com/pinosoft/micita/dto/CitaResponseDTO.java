package com.pinosoft.micita.dto;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

// Para respuestas de citas con información adicional
@Data
public class CitaResponseDTO {
	  private Long idCita;
	    private Long idCliente;
	    private Long idTrabajador;
	    private Long idServicio;
	    private String nombreCliente;
	    private String nombreTrabajador;
	    private String nombreServicio;
	    private LocalDate fecha;
	    private LocalTime horaInicio;
	    private LocalTime horaFin;
	    private CitaDTO.EstadoCita estado;
	    private LocalDateTime createdAt;
	    private Integer duracionMinutos;
	    private Double precio;

}
