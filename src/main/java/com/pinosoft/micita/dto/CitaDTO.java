package com.pinosoft.micita.dto;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.FutureOrPresent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Data
public class CitaDTO {
	
    private Long idCita;
    
    @NotNull(message = "El ID del cliente es obligatorio")
    private Long idCliente;
    
    @NotNull(message = "El ID del trabajador es obligatorio")
    private Long idTrabajador;
    
    @NotNull(message = "El ID del servicio es obligatorio")
    private Long idServicio;
    
    @NotNull(message = "La fecha es obligatoria")
    @FutureOrPresent(message = "La fecha debe ser presente o futura")
    private LocalDate fecha;
    
    @NotNull(message = "La hora de inicio es obligatoria")
    private LocalTime horaInicio;
    
    @NotNull(message = "La hora de fin es obligatoria")
    private LocalTime horaFin;
    
    private EstadoCita estado;
    
    private LocalDateTime createdAt;
    
    public enum EstadoCita {
        PENDIENTE,
        CONFIRMADA,
        CANCELADA,
        FINALIZADA
    }
}
