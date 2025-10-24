package com.pinosoft.micita.dto;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class DisponibilidadEspecialDTO {
	
    private Long idDisponibilidadEspecial;
    
    @NotNull(message = "El ID del trabajador es obligatorio")
    private Long idTrabajador;
    
    @NotNull(message = "La fecha es obligatoria")
    @FutureOrPresent(message = "La fecha debe ser presente o futura")
    private LocalDate fecha;
    
    @NotNull(message = "La hora de inicio es obligatoria")
    @Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Formato de hora inválido (HH:mm)")
    private String horaInicio;
    
    @NotNull(message = "La hora de fin es obligatoria")
    @Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Formato de hora inválido (HH:mm)")
    private String horaFin;
    
    @NotNull(message = "El tipo es obligatorio")
    private TipoDisponibilidad tipo;
    
    private String descripcion;
    
    public enum TipoDisponibilidad {
        BLOQUEO,
        EXTRA
    }
}
