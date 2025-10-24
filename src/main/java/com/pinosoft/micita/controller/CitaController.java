package com.pinosoft.micita.controller;

import com.pinosoft.micita.dto.CitaDTO;
import com.pinosoft.micita.dto.CitaFiltroDTO;
import com.pinosoft.micita.model.Cita;
import com.pinosoft.micita.service.CitaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
public class CitaController {
	private final CitaService citaService;

	// Crear nueva cita
	@PostMapping
	public ResponseEntity<CitaDTO> crearCita(@Valid @RequestBody CitaDTO citaDTO) {
		try {
			CitaDTO creado = citaService.crearCita(citaDTO);
			return new ResponseEntity<>(creado, HttpStatus.CREATED);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	// Obtener cita por ID
	@GetMapping("/{id}")
	public ResponseEntity<CitaDTO> obtenerPorId(@PathVariable Long id) {
		try {
			CitaDTO dto = citaService.obtenerPorId(id);
			return ResponseEntity.ok(dto);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	// Obtener citas por trabajador
	@GetMapping("/trabajador/{trabajadorId}")
	public ResponseEntity<List<CitaDTO>> obtenerPorTrabajador(@PathVariable Long trabajadorId) {
		List<CitaDTO> citas = citaService.obtenerPorTrabajador(trabajadorId);
		return ResponseEntity.ok(citas);
	}

	// Obtener citas por cliente
	@GetMapping("/cliente/{clienteId}")
	public ResponseEntity<List<CitaDTO>> obtenerPorCliente(@PathVariable Long clienteId) {
		List<CitaDTO> citas = citaService.obtenerPorCliente(clienteId);
		return ResponseEntity.ok(citas);
	}

	// Obtener citas por servicio
	@GetMapping("/servicio/{servicioId}")
	public ResponseEntity<List<CitaDTO>> obtenerPorServicio(@PathVariable Long servicioId) {
		List<CitaDTO> citas = citaService.obtenerPorServicio(servicioId);
		return ResponseEntity.ok(citas);
	}

	// Obtener citas por estado
	@GetMapping("/estado/{estado}")
	public ResponseEntity<List<CitaDTO>> obtenerPorEstado(@PathVariable String estado) {
		try {
			Cita.EstadoCita estadoEnum = Cita.EstadoCita.valueOf(estado.toUpperCase());
			List<CitaDTO> citas = citaService.obtenerPorEstado(estadoEnum);
			return ResponseEntity.ok(citas);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().build();
		}
	}

	// Obtener citas por trabajador y fecha
	@GetMapping("/trabajador/{trabajadorId}/fecha/{fecha}")
	public ResponseEntity<List<CitaDTO>> obtenerPorTrabajadorYFecha(@PathVariable Long trabajadorId,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
		List<CitaDTO> citas = citaService.obtenerPorTrabajadorYFecha(trabajadorId, fecha);
		return ResponseEntity.ok(citas);
	}

	// Obtener citas por trabajador y estado
	@GetMapping("/trabajador/{trabajadorId}/estado/{estado}")
	public ResponseEntity<List<CitaDTO>> obtenerPorTrabajadorYEstado(@PathVariable Long trabajadorId,
			@PathVariable String estado) {
		try {
			Cita.EstadoCita estadoEnum = Cita.EstadoCita.valueOf(estado.toUpperCase());
			List<CitaDTO> citas = citaService.obtenerPorTrabajadorYEstado(trabajadorId, estadoEnum);
			return ResponseEntity.ok(citas);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().build();
		}
	}

	// Obtener citas por fecha
	@GetMapping("/fecha/{fecha}")
	public ResponseEntity<List<CitaDTO>> obtenerPorFecha(
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
		List<CitaDTO> citas = citaService.obtenerPorFecha(fecha);
		return ResponseEntity.ok(citas);
	}

	// Obtener citas por rango de fechas
	@GetMapping("/rango-fechas")
	public ResponseEntity<List<CitaDTO>> obtenerPorRangoFechas(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
		List<CitaDTO> citas = citaService.obtenerPorRangoFechas(fechaInicio, fechaFin);
		return ResponseEntity.ok(citas);
	}

	// Obtener citas por trabajador y rango de fechas
	@GetMapping("/trabajador/{trabajadorId}/rango-fechas")
	public ResponseEntity<List<CitaDTO>> obtenerPorTrabajadorYRangoFechas(@PathVariable Long trabajadorId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
		List<CitaDTO> citas = citaService.obtenerPorTrabajadorYRangoFechas(trabajadorId, fechaInicio, fechaFin);
		return ResponseEntity.ok(citas);
	}

	// Actualizar cita
	@PutMapping("/{id}")
	public ResponseEntity<CitaDTO> actualizarCita(@PathVariable Long id, @Valid @RequestBody CitaDTO citaDTO) {
		try {
			CitaDTO actualizado = citaService.actualizarCita(id, citaDTO);
			return ResponseEntity.ok(actualizado);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	// Cambiar estado de la cita
	@PatchMapping("/{id}/estado")
	public ResponseEntity<CitaDTO> cambiarEstado(@PathVariable Long id, @RequestParam String estado) {
		try {
			Cita.EstadoCita estadoEnum = Cita.EstadoCita.valueOf(estado.toUpperCase());
			CitaDTO actualizado = citaService.cambiarEstado(id, estadoEnum);
			return ResponseEntity.ok(actualizado);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().build();
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	// Confirmar cita
	@PatchMapping("/{id}/confirmar")
	public ResponseEntity<CitaDTO> confirmarCita(@PathVariable Long id) {
		try {
			CitaDTO confirmada = citaService.confirmarCita(id);
			return ResponseEntity.ok(confirmada);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	// Cancelar cita
	@PatchMapping("/{id}/cancelar")
	public ResponseEntity<CitaDTO> cancelarCita(@PathVariable Long id) {
		try {
			CitaDTO cancelada = citaService.cancelarCita(id);
			return ResponseEntity.ok(cancelada);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	// Finalizar cita
	@PatchMapping("/{id}/finalizar")
	public ResponseEntity<CitaDTO> finalizarCita(@PathVariable Long id) {
		try {
			CitaDTO finalizada = citaService.finalizarCita(id);
			return ResponseEntity.ok(finalizada);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	// Eliminar cita
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminarCita(@PathVariable Long id) {
		try {
			citaService.eliminarCita(id);
			return ResponseEntity.noContent().build();
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	// Verificar conflicto de horario
	@GetMapping("/verificar-conflicto")
	public ResponseEntity<Boolean> verificarConflicto(@RequestParam Long trabajadorId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
			@RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime horaInicio,
			@RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime horaFin) {
		boolean conflicto = citaService.verificarConflictoHorario(trabajadorId, fecha, horaInicio, horaFin);
		return ResponseEntity.ok(conflicto);
	}

	// Contar citas por estado y trabajador
	@GetMapping("/contar")
	public ResponseEntity<Long> contarCitasPorEstadoYTrabajador(@RequestParam Long trabajadorId,
			@RequestParam String estado) {
		try {
			Cita.EstadoCita estadoEnum = Cita.EstadoCita.valueOf(estado.toUpperCase());
			long count = citaService.contarCitasPorEstadoYTrabajador(trabajadorId, estadoEnum);
			return ResponseEntity.ok(count);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().build();
		}
	}

	// Obtener citas pendientes de confirmación automática
	@GetMapping("/pendientes-confirmacion-automatica")
	public ResponseEntity<List<CitaDTO>> obtenerCitasPendientesConfirmacionAutomatica() {
		List<CitaDTO> citas = citaService.obtenerCitasPendientesConfirmacionAutomatica();
		return ResponseEntity.ok(citas);
	}
}
