package com.pinosoft.micita.service;

import com.pinosoft.micita.dto.CitaDTO;
import com.pinosoft.micita.model.Cita;
import com.pinosoft.micita.model.Servicio;
import com.pinosoft.micita.repository.CitaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CitaService {
	private final CitaRepository citaRepository;

	// Convertir Entity a DTO
	private CitaDTO convertToDTO(Cita cita) {
		CitaDTO dto = new CitaDTO();
		dto.setIdCita(cita.getIdCita());
		dto.setIdCliente(cita.getIdCliente());
		dto.setIdTrabajador(cita.getIdTrabajador());
		if (cita.getServicio() != null) {
			dto.setIdServicio(cita.getServicio().getId());
		}
		dto.setFecha(cita.getFecha());
		dto.setHoraInicio(cita.getHoraInicio());
		dto.setHoraFin(cita.getHoraFin());
		dto.setEstado(CitaDTO.EstadoCita.valueOf(cita.getEstado().name()));
		dto.setCreatedAt(cita.getCreatedAt());
		return dto;
	}

	// Convertir DTO a Entity
	private Cita convertToEntity(CitaDTO dto) {
		Cita cita = new Cita();
		cita.setIdCita(dto.getIdCita());
		cita.setIdCliente(dto.getIdCliente());
		cita.setIdTrabajador(dto.getIdTrabajador());
		if (dto.getIdServicio() != null) {
			Servicio servicio = new Servicio();
			servicio.setId(dto.getIdServicio());
			cita.setServicio(servicio);
		}
		cita.setFecha(dto.getFecha());
		cita.setHoraInicio(dto.getHoraInicio());
		cita.setHoraFin(dto.getHoraFin());

		if (dto.getEstado() != null) {
			cita.setEstado(Cita.EstadoCita.valueOf(dto.getEstado().name()));
		}

		if (dto.getCreatedAt() != null) {
			cita.setCreatedAt(dto.getCreatedAt());
		}

		return cita;
	}

	// Validar cita
	private void validarCita(CitaDTO dto) {
		if (dto.getFecha().isBefore(LocalDate.now())) {
			throw new RuntimeException("La fecha de la cita debe ser presente o futura");
		}

		if (dto.getHoraInicio().isAfter(dto.getHoraFin()) || dto.getHoraInicio().equals(dto.getHoraFin())) {
			throw new RuntimeException("La hora de inicio debe ser menor que la hora de fin");
		}

		if (dto.getFecha().equals(LocalDate.now()) && dto.getHoraInicio().isBefore(LocalTime.now())) {
			throw new RuntimeException("La hora de inicio no puede ser en el pasado para citas de hoy");
		}
	}

	// Crear nueva cita
	@Transactional
	public CitaDTO crearCita(CitaDTO dto) {
		validarCita(dto);

		// Verificar conflicto de horario
		if (citaRepository.existsConflictoHorario(dto.getIdTrabajador(), dto.getFecha(), dto.getHoraInicio(),
				dto.getHoraFin())) {
			throw new RuntimeException("Ya existe una cita programada en ese horario para el trabajador");
		}

		Cita cita = convertToEntity(dto);
		Cita saved = citaRepository.save(cita);
		return convertToDTO(saved);
	}

	// Obtener cita por ID
	public CitaDTO obtenerPorId(Long id) {
		Cita cita = citaRepository.findById(id).orElseThrow(() -> new RuntimeException("Cita no encontrada"));
		return convertToDTO(cita);
	}

	// Obtener todas las citas de un trabajador
	public List<CitaDTO> obtenerPorTrabajador(Long trabajadorId) {
		return citaRepository.findByIdTrabajador(trabajadorId).stream().map(this::convertToDTO)
				.collect(Collectors.toList());
	}

	// Obtener todas las citas de un cliente
	public List<CitaDTO> obtenerPorCliente(Long clienteId) {
		return citaRepository.findByIdCliente(clienteId).stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	// Obtener citas por servicio
	public List<CitaDTO> obtenerPorServicio(Long servicioId) {
		return citaRepository.findByServicio_Id(servicioId).stream().map(this::convertToDTO)
				.collect(Collectors.toList());
	}

	// Obtener citas por estado
	public List<CitaDTO> obtenerPorEstado(Cita.EstadoCita estado) {
		return citaRepository.findByEstado(estado).stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	// Obtener citas por trabajador y fecha
	public List<CitaDTO> obtenerPorTrabajadorYFecha(Long trabajadorId, LocalDate fecha) {
		return citaRepository.findByIdTrabajadorAndFecha(trabajadorId, fecha).stream().map(this::convertToDTO)
				.collect(Collectors.toList());
	}

	// Obtener citas por trabajador y estado
	public List<CitaDTO> obtenerPorTrabajadorYEstado(Long trabajadorId, Cita.EstadoCita estado) {
		return citaRepository.findByIdTrabajadorAndEstado(trabajadorId, estado).stream().map(this::convertToDTO)
				.collect(Collectors.toList());
	}

	// Obtener citas por fecha
	public List<CitaDTO> obtenerPorFecha(LocalDate fecha) {
		return citaRepository.findByFecha(fecha).stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	// Obtener citas por rango de fechas
	public List<CitaDTO> obtenerPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
		return citaRepository.findByFechaBetween(fechaInicio, fechaFin).stream().map(this::convertToDTO)
				.collect(Collectors.toList());
	}

	// Obtener citas por trabajador y rango de fechas
	public List<CitaDTO> obtenerPorTrabajadorYRangoFechas(Long trabajadorId, LocalDate fechaInicio,
			LocalDate fechaFin) {
		return citaRepository.findByIdTrabajadorAndFechaBetween(trabajadorId, fechaInicio, fechaFin).stream()
				.map(this::convertToDTO).collect(Collectors.toList());
	}

	// Actualizar cita
	@Transactional
	public CitaDTO actualizarCita(Long id, CitaDTO dto) {
		validarCita(dto);

		Cita existing = citaRepository.findById(id).orElseThrow(() -> new RuntimeException("Cita no encontrada"));

		// Verificar conflicto de horario excluyendo la cita actual
		if (citaRepository.existsConflictoHorarioExcluyendoCita(dto.getIdTrabajador(), dto.getFecha(),
				dto.getHoraInicio(), dto.getHoraFin(), id)) {
			throw new RuntimeException("Ya existe una cita programada en ese horario para el trabajador");
		}

		existing.setIdCliente(dto.getIdCliente());
		existing.setIdTrabajador(dto.getIdTrabajador());
		if (dto.getIdServicio() != null) {
			Servicio servicio = new Servicio();
			servicio.setId(dto.getIdServicio());
			existing.setServicio(servicio);
		}
		existing.setFecha(dto.getFecha());
		existing.setHoraInicio(dto.getHoraInicio());
		existing.setHoraFin(dto.getHoraFin());
		if (dto.getEstado() != null) {
			existing.setEstado(Cita.EstadoCita.valueOf(dto.getEstado().name()));
		}

		Cita updated = citaRepository.save(existing);
		return convertToDTO(updated);
	}

	// Cambiar estado de la cita
	@Transactional
	public CitaDTO cambiarEstado(Long id, Cita.EstadoCita estado) {
		Cita cita = citaRepository.findById(id).orElseThrow(() -> new RuntimeException("Cita no encontrada"));

		// Validar transiciones de estado
		validarTransicionEstado(cita.getEstado(), estado);

		cita.setEstado(estado);
		Cita updated = citaRepository.save(cita);
		return convertToDTO(updated);
	}

	// Validar transición de estado
	private void validarTransicionEstado(Cita.EstadoCita estadoActual, Cita.EstadoCita nuevoEstado) {
		if (estadoActual == Cita.EstadoCita.CANCELADA || estadoActual == Cita.EstadoCita.FINALIZADA) {
			throw new RuntimeException("No se puede modificar una cita " + estadoActual.toString().toLowerCase());
		}

		if (nuevoEstado == Cita.EstadoCita.PENDIENTE && estadoActual != Cita.EstadoCita.PENDIENTE) {
			throw new RuntimeException("No se puede revertir una cita a estado pendiente");
		}
	}

	// Confirmar cita
	@Transactional
	public CitaDTO confirmarCita(Long id) {
		return cambiarEstado(id, Cita.EstadoCita.CONFIRMADA);
	}

	// Cancelar cita
	@Transactional
	public CitaDTO cancelarCita(Long id) {
		return cambiarEstado(id, Cita.EstadoCita.CANCELADA);
	}

	// Finalizar cita
	@Transactional
	public CitaDTO finalizarCita(Long id) {
		return cambiarEstado(id, Cita.EstadoCita.FINALIZADA);
	}

	// Eliminar cita
	@Transactional
	public void eliminarCita(Long id) {
		if (!citaRepository.existsById(id)) {
			throw new RuntimeException("Cita no encontrada");
		}
		citaRepository.deleteById(id);
	}

	// Verificar conflicto de horario
	public boolean verificarConflictoHorario(Long trabajadorId, LocalDate fecha, LocalTime horaInicio,
			LocalTime horaFin) {
		return citaRepository.existsConflictoHorario(trabajadorId, fecha, horaInicio, horaFin);
	}

	// Verificar conflicto excluyendo una cita
	public boolean verificarConflictoExcluyendoCita(Long trabajadorId, LocalDate fecha, LocalTime horaInicio,
			LocalTime horaFin, Long excludeCitaId) {
		return citaRepository.existsConflictoHorarioExcluyendoCita(trabajadorId, fecha, horaInicio, horaFin,
				excludeCitaId);
	}

	// Contar citas por estado y trabajador
	public long contarCitasPorEstadoYTrabajador(Long trabajadorId, Cita.EstadoCita estado) {
		return citaRepository.countByIdTrabajadorAndEstado(trabajadorId, estado);
	}

	// Obtener citas pendientes de confirmación automática
	public List<CitaDTO> obtenerCitasPendientesConfirmacionAutomatica() {
		return citaRepository.findCitasPendientesConfirmacionAutomatica().stream().map(this::convertToDTO)
				.collect(Collectors.toList());
	}
}
