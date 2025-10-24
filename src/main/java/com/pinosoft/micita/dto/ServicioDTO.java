package com.pinosoft.micita.dto;


import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ServicioDTO {
	
	 	private Long id;
	
	 	@NotBlank(message = "El nombre es obligatorio")
	    private String nombre;
	    
	    private String descripcion;
	    
	    @NotNull(message = "El precio es obligatorio")
	    @Positive(message = "El precio debe ser mayor a cero")
	    private Double precio;
	    
	    @NotNull(message = "La duración es obligatoria")
	    @Positive(message = "La duración debe ser mayor a cero")
	    private Integer duracionMinutos;
	    
	    @NotNull(message = "El ID del negocio es obligatorio")
	    private Long negocioId;
	    
	    @NotNull(message = "El ID de la categoría de servicio es obligatorio")
	    private Long categoriaServicioId;
	    
	    @NotNull(message = "El estado es obligatorio")
	    private Boolean estado;
	    

}
