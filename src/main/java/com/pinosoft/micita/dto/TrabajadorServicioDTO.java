package com.pinosoft.micita.dto;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Data
public class TrabajadorServicioDTO {

	 private Long id;
	    
	    @NotNull(message = "El ID del trabajador es obligatorio")
	    private Long trabajadorId;
	    
	    @NotNull(message = "El ID del servicio es obligatorio")
	    private Long servicioId;
	    
	    private LocalDateTime creadoEn;
	    
	    @NotNull(message = "La confirmación automática es obligatoria")
	    private Boolean confirmacionAutomatica;
    

}
