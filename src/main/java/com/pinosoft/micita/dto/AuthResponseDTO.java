package com.pinosoft.micita.dto;

import lombok.Data;

@Data
public class AuthResponseDTO {
	
	private String token;
	
	private String correo;
	
	private String rol;

	public AuthResponseDTO(String token, String correo, String rol) {
		this.token = token;
		this.correo = correo;
		this.rol = rol;
	}
}
