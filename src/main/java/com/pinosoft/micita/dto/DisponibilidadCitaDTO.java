package com.pinosoft.micita.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DisponibilidadCitaDTO {
	private Long trabajadorId;
    private Long servicioId;
    private LocalDate fecha;
    private List<IntervaloDisponibleDTO> intervalosDisponibles;

}
