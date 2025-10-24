package com.pinosoft.micita.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Data
public class ReprogramacionDTO {
	

private Long idReprogramacion;
    
    @NotNull(message = "El ID de la cita es obligatorio")
    private Long idCita;
    
    @NotNull(message = "La nueva fecha es obligatoria")
    @FutureOrPresent(message = "La nueva fecha debe ser presente o futura")
    private LocalDate nuevaFecha;
    
    @NotNull(message = "La nueva hora de inicio es obligatoria")
    private LocalTime nuevaHoraInicio;
    
    @NotNull(message = "La nueva hora de fin es obligatoria")
    private LocalTime nuevaHoraFin;
    
    @NotBlank(message = "El motivo es obligatorio")
    private String motivo;
    
    private EstadoReprogramacion estado;
    
    private LocalDateTime createAt;
    
    public enum EstadoReprogramacion {
        PENDIENTE,
        APROBADA,
        RECHAZADA
    }

}
