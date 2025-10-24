package com.pinosoft.micita.dto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AsignacionServicioDTO {
	 @NotNull(message = "El ID del trabajador es obligatorio")
	    private Long trabajadorId;
	    
	    @NotNull(message = "El ID del servicio es obligatorio")
	    private Long servicioId;

}
