package com.pinosoft.micita.service;

import com.pinosoft.micita.dto.DisponibilidadEspecialDTO;
import com.pinosoft.micita.model.DisponibilidadEspecial;
import com.pinosoft.micita.repository.DisponibilidadEspecialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DisponibilidadEspecialService {


	private final DisponibilidadEspecialRepository disponibilidadEspecialRepository;

	// Convertir Entity a DTO
	private DisponibilidadEspecialDTO convertToDTO(DisponibilidadEspecial disponibilidadEspecial) {
		DisponibilidadEspecialDTO dto = new DisponibilidadEspecialDTO();
		dto.setIdDisponibilidadEspecial(disponibilidadEspecial.getIdDisponibilidadEspecial());
		dto.setIdTrabajador(disponibilidadEspecial.getIdTrabajador());
		dto.setFecha(disponibilidadEspecial.getFecha());
		dto.setHoraInicio(disponibilidadEspecial.getHoraInicio());
		dto.setHoraFin(disponibilidadEspecial.getHoraFin());
		dto.setTipo(DisponibilidadEspecialDTO.TipoDisponibilidad.valueOf(disponibilidadEspecial.getTipo().name()));
		dto.setDescripcion(disponibilidadEspecial.getDescripcion());
		return dto;
	}

	// Convertir DTO a Entity
	private DisponibilidadEspecial convertToEntity(DisponibilidadEspecialDTO dto) {
		DisponibilidadEspecial disponibilidadEspecial = new DisponibilidadEspecial();
		disponibilidadEspecial.setIdDisponibilidadEspecial(dto.getIdDisponibilidadEspecial());
		disponibilidadEspecial.setIdTrabajador(dto.getIdTrabajador());
		disponibilidadEspecial.setFecha(dto.getFecha());
		disponibilidadEspecial.setHoraInicio(dto.getHoraInicio());
		disponibilidadEspecial.setHoraFin(dto.getHoraFin());
		disponibilidadEspecial.setTipo(DisponibilidadEspecial.TipoDisponibilidad.valueOf(dto.getTipo().name()));
		disponibilidadEspecial.setDescripcion(dto.getDescripcion());
		return disponibilidadEspecial;
	}

	// Validar disponibilidad especial
	private void validarDisponibilidadEspecial(DisponibilidadEspecialDTO dto) {
		if (dto.getFecha().isBefore(LocalDate.now())) {
			throw new RuntimeException("La fecha debe ser presente o futura");
		}

		if (dto.getHoraInicio().compareTo(dto.getHoraFin()) >= 0) {
			throw new RuntimeException("La hora de inicio debe ser menor que la hora de fin");
		}
	}

	// Crear nueva disponibilidad especial
	@Transactional
	public DisponibilidadEspecialDTO crearDisponibilidadEspecial(DisponibilidadEspecialDTO dto) {
		validarDisponibilidadEspecial(dto);

		// Verificar si ya existe una disponibilidad especial para la misma fecha y
		// trabajador que se solape
		List<DisponibilidadEspecial> existentes = disponibilidadEspecialRepository
				.findByIdTrabajadorAndFecha(dto.getIdTrabajador(), dto.getFecha());

		for (DisponibilidadEspecial existente : existentes) {
			if (existeSolapamiento(existente, dto.getHoraInicio(), dto.getHoraFin())) {
				throw new RuntimeException("Ya existe una disponibilidad especial que se solapa con este horario");
			}
		}

		DisponibilidadEspecial disponibilidadEspecial = convertToEntity(dto);
		DisponibilidadEspecial saved = disponibilidadEspecialRepository.save(disponibilidadEspecial);
		return convertToDTO(saved);
	}

	// Verificar solapamiento de horarios
	private boolean existeSolapamiento(DisponibilidadEspecial existente, String nuevaHoraInicio, String nuevaHoraFin) {
		return (nuevaHoraInicio.compareTo(existente.getHoraFin()) < 0
				&& nuevaHoraFin.compareTo(existente.getHoraInicio()) > 0);
	}

	// Obtener disponibilidad especial por ID
	public DisponibilidadEspecialDTO obtenerPorId(Long id) {
		DisponibilidadEspecial disponibilidadEspecial = disponibilidadEspecialRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Disponibilidad especial no encontrada"));
		return convertToDTO(disponibilidadEspecial);
	}

	// Obtener todas las disponibilidades especiales de un trabajador
	public List<DisponibilidadEspecialDTO> obtenerPorTrabajador(Long trabajadorId) {
		return disponibilidadEspecialRepository.findByIdTrabajador(trabajadorId).stream().map(this::convertToDTO)
				.collect(Collectors.toList());
	}

	// Obtener disponibilidades especiales por trabajador y fecha
	public List<DisponibilidadEspecialDTO> obtenerPorTrabajadorYFecha(Long trabajadorId, LocalDate fecha) {
		return disponibilidadEspecialRepository.findByIdTrabajadorAndFecha(trabajadorId, fecha).stream()
				.map(this::convertToDTO).collect(Collectors.toList());
	}

	// Obtener disponibilidades especiales por trabajador y rango de fechas
	public List<DisponibilidadEspecialDTO> obtenerPorTrabajadorYRangoFechas(Long trabajadorId, LocalDate fechaInicio,
			LocalDate fechaFin) {
		return disponibilidadEspecialRepository.findByIdTrabajadorAndFechaBetween(trabajadorId, fechaInicio, fechaFin)
				.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	// Obtener bloques por trabajador y fecha
	public List<DisponibilidadEspecialDTO> obtenerBloquesPorTrabajadorYFecha(Long trabajadorId, LocalDate fecha) {
		return disponibilidadEspecialRepository
				.findByIdTrabajadorAndFechaAndTipo(trabajadorId, fecha,
						DisponibilidadEspecial.TipoDisponibilidad.BLOQUEO)
				.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	// Obtener disponibilidades extra por trabajador y fecha
	public List<DisponibilidadEspecialDTO> obtenerExtrasPorTrabajadorYFecha(Long trabajadorId, LocalDate fecha) {
		return disponibilidadEspecialRepository
				.findByIdTrabajadorAndFechaAndTipo(trabajadorId, fecha, DisponibilidadEspecial.TipoDisponibilidad.EXTRA)
				.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	// Obtener disponibilidades especiales por tipo
	public List<DisponibilidadEspecialDTO> obtenerPorTipo(DisponibilidadEspecial.TipoDisponibilidad tipo) {
		return disponibilidadEspecialRepository.findByTipo(tipo).stream().map(this::convertToDTO)
				.collect(Collectors.toList());
	}

	// Obtener disponibilidades especiales futuras por trabajador
	public List<DisponibilidadEspecialDTO> obtenerFuturasPorTrabajador(Long trabajadorId) {
		return disponibilidadEspecialRepository
				.findByIdTrabajadorAndFechaGreaterThanEqual(trabajadorId, LocalDate.now()).stream()
				.map(this::convertToDTO).collect(Collectors.toList());
	}

	// Verificar si existe bloqueo para un trabajador en una fecha y hora específica
	public boolean existeBloqueoEnHorario(Long trabajadorId, LocalDate fecha, String horaInicio, String horaFin) {
		return disponibilidadEspecialRepository.existsBloqueoEnHorario(trabajadorId, fecha, horaInicio, horaInicio,
				horaFin);
	}

	// Actualizar disponibilidad especial
	@Transactional
	public DisponibilidadEspecialDTO actualizarDisponibilidadEspecial(Long id, DisponibilidadEspecialDTO dto) {
		validarDisponibilidadEspecial(dto);

		DisponibilidadEspecial existing = disponibilidadEspecialRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Disponibilidad especial no encontrada"));

		// Verificar solapamiento excluyendo la actual
		List<DisponibilidadEspecial> existentes = disponibilidadEspecialRepository
				.findByIdTrabajadorAndFecha(dto.getIdTrabajador(), dto.getFecha());

		for (DisponibilidadEspecial existente : existentes) {
			if (!existente.getIdDisponibilidadEspecial().equals(id)
					&& existeSolapamiento(existente, dto.getHoraInicio(), dto.getHoraFin())) {
				throw new RuntimeException("Ya existe una disponibilidad especial que se solapa con este horario");
			}
		}

		existing.setIdTrabajador(dto.getIdTrabajador());
		existing.setFecha(dto.getFecha());
		existing.setHoraInicio(dto.getHoraInicio());
		existing.setHoraFin(dto.getHoraFin());
		existing.setTipo(DisponibilidadEspecial.TipoDisponibilidad.valueOf(dto.getTipo().name()));
		existing.setDescripcion(dto.getDescripcion());

		DisponibilidadEspecial updated = disponibilidadEspecialRepository.save(existing);
		return convertToDTO(updated);
	}

	// Eliminar disponibilidad especial
	@Transactional
	public void eliminarDisponibilidadEspecial(Long id) {
		if (!disponibilidadEspecialRepository.existsById(id)) {
			throw new RuntimeException("Disponibilidad especial no encontrada");
		}
		disponibilidadEspecialRepository.deleteById(id);
	}

	// Eliminar disponibilidades especiales por trabajador y fecha
	@Transactional
	public void eliminarPorTrabajadorYFecha(Long trabajadorId, LocalDate fecha) {
		List<DisponibilidadEspecial> disponibilidades = disponibilidadEspecialRepository
				.findByIdTrabajadorAndFecha(trabajadorId, fecha);
		disponibilidadEspecialRepository.deleteAll(disponibilidades);
	}

}
