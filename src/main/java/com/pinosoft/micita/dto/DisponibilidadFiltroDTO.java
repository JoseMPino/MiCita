package com.pinosoft.micita.dto;
import lombok.Data;
import java.time.LocalDate;
@Data
public class DisponibilidadFiltroDTO {
	   private Long idTrabajador;
	    private LocalDate fecha;
	    private Integer diaSemana;

}
