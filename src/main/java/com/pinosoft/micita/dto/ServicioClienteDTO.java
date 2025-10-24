package com.pinosoft.micita.dto;


import lombok.Data;

@Data
public class ServicioClienteDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer duracionMinutos;

    
    // se puede agregar más campos relevantes para el cliente si es necesario
    
    
}
