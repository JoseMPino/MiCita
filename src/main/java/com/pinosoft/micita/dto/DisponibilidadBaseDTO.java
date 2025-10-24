package com.pinosoft.micita.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;

@Data
public class DisponibilidadBaseDTO {

	  private Long idDisponibilidadBase;
	    
	    @NotNull(message = "El ID del trabajador es obligatorio")
	    private Long idTrabajador;
	    
	    @NotNull(message = "El día de la semana es obligatorio")
	    @Min(value = 1, message = "El día de la semana debe ser entre 1 y 7")
	    @Max(value = 7, message = "El día de la semana debe ser entre 1 y 7")
	    private Integer diaSemana; // 1=Lunes, 7=Domingo
	    
	    @NotNull(message = "La hora de inicio es obligatoria")
	    @Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Formato de hora inválido (HH:mm)")
	    private String horaInicio;
	    
	    @NotNull(message = "La hora de fin es obligatoria")
	    @Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Formato de hora inválido (HH:mm)")
	    private String horaFin;
	    
	    @NotNull(message = "El estado es obligatorio")
	    private Boolean estado;
}
