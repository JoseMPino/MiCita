package com.pinosoft.micita.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NegocioDTO {

	 @NotBlank(message = "El nombre del negocio es obligatorio")
	    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
	    private String nombre;
	    
	    @NotBlank(message = "La dirección es obligatoria")
	    @Size(max = 200, message = "La dirección no puede exceder los 200 caracteres")
	    private String direccion;
	    
	    @NotBlank(message = "El teléfono es obligatorio")
	    @Pattern(regexp = "^[+]?[(]?[0-9]{1,4}[)]?[-\\s.]?[0-9]{3}[-\\s.]?[0-9]{4,6}$", 
	             message = "Formato de teléfono inválido")
	    private String telefono;
	    
	    @NotNull(message = "La categoría del negocio es obligatoria")
	    private Long categoriaNegocioId;
	
}
