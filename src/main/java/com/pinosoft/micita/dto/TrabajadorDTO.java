package com.pinosoft.micita.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class TrabajadorDTO {
	
	@NotBlank(message = "Los nombres son obligatorios")
    private String nombres;
    
    @NotBlank(message = "Los apellidos son obligatorios")
    private String apellidos;
    
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe ser válido")
    private String correo;
    
    @NotBlank(message = "El teléfono es obligatorio")
    @NotNull(message = "El teléfono no puede ser nulo")
    private String telefono;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 4, message = "La contraseña debe tener al menos 4 caracteres")
    @NotNull(message = "La contraseña no puede ser nula")
    private String contrasena;
    
    @NotNull(message = "El ID del negocio es obligatorio")
    private Long negocioId;

}
