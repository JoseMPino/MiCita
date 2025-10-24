package com.pinosoft.micita.dto;
import lombok.Data;
import java.time.LocalDate;
@Data
public class CitaFiltroDTO {
    private Long idTrabajador;
    private Long idCliente;
    private Long idServicio;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private CitaDTO.EstadoCita estado;
}
