package com.pinosoft.micita.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequestDTO {

	 @NotBlank(message = "El correo es obligatorio")
	    @Email(message = "El correo debe ser válido")
	    private String correo;
	    
	    @NotBlank(message = "La contraseña es obligatoria")
	    private String contrasena;
}
