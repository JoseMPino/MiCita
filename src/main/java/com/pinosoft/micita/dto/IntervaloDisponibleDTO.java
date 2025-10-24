package com.pinosoft.micita.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IntervaloDisponibleDTO {
	private LocalTime horaInicio;
	private LocalTime horaFin;
	private LocalDate fecha;

}
