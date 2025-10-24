package com.pinosoft.micita.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
@Data
public class DisponibilidadResponseDTO {
	
	   private Long id;
	    private Long idTrabajador;
	    private String nombreTrabajador;
	    private LocalDate fecha;
	    private String horaInicio;
	    private String horaFin;
	    private String tipo; // "BASE" o "ESPECIAL"
	    private DisponibilidadEspecialDTO.TipoDisponibilidad tipoEspecial;
	    private String descripcion;
	    private Boolean disponible;

}
