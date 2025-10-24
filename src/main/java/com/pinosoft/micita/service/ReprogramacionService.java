package com.pinosoft.micita.service;

import com.pinosoft.micita.dto.ReprogramacionDTO;
import com.pinosoft.micita.model.Cita;
import com.pinosoft.micita.model.Reprogramacion;
import com.pinosoft.micita.repository.CitaRepository;
import com.pinosoft.micita.repository.ReprogramacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReprogramacionService {

	  private final ReprogramacionRepository reprogramacionRepository;
	    private final CitaRepository citaRepository;
	    private final CitaService citaService;

	    // Convertir Entity a DTO
	    private ReprogramacionDTO convertToDTO(Reprogramacion reprogramacion) {
	        ReprogramacionDTO dto = new ReprogramacionDTO();
	        dto.setIdReprogramacion(reprogramacion.getIdReprogramacion());
	        dto.setIdCita(reprogramacion.getIdCita());
	        dto.setNuevaFecha(reprogramacion.getNuevaFecha());
	        dto.setNuevaHoraInicio(reprogramacion.getNuevaHoraInicio());
	        dto.setNuevaHoraFin(reprogramacion.getNuevaHoraFin());
	        dto.setMotivo(reprogramacion.getMotivo());
	        dto.setEstado(ReprogramacionDTO.EstadoReprogramacion.valueOf(reprogramacion.getEstado().name()));
	        dto.setCreateAt(reprogramacion.getCreateAt());
	        return dto;
	    }

	    // Convertir DTO a Entity
	    private Reprogramacion convertToEntity(ReprogramacionDTO dto) {
	        Reprogramacion reprogramacion = new Reprogramacion();
	        reprogramacion.setIdReprogramacion(dto.getIdReprogramacion());
	        reprogramacion.setIdCita(dto.getIdCita());
	        reprogramacion.setNuevaFecha(dto.getNuevaFecha());
	        reprogramacion.setNuevaHoraInicio(dto.getNuevaHoraInicio());
	        reprogramacion.setNuevaHoraFin(dto.getNuevaHoraFin());
	        reprogramacion.setMotivo(dto.getMotivo());
	        if (dto.getEstado() != null) {
	            reprogramacion.setEstado(Reprogramacion.EstadoReprogramacion.valueOf(dto.getEstado().name()));
	        }
	        // createAt se genera automáticamente con @PrePersist
	        return reprogramacion;
	    }

	    // Validar reprogramación
	    private void validarReprogramacion(ReprogramacionDTO dto) {
	        // Validar que la cita exista
	        Cita cita = citaRepository.findById(dto.getIdCita())
	                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

	        // Validar que la nueva fecha sea presente o futura
	        if (dto.getNuevaFecha().isBefore(LocalDate.now())) {
	            throw new RuntimeException("La nueva fecha debe ser presente o futura");
	        }

	        // Validar que la hora de inicio sea menor que la hora de fin
	        if (dto.getNuevaHoraInicio().isAfter(dto.getNuevaHoraFin()) || 
	            dto.getNuevaHoraInicio().equals(dto.getNuevaHoraFin())) {
	            throw new RuntimeException("La nueva hora de inicio debe ser menor que la nueva hora de fin");
	        }

	        // Validar que no sea para el mismo día y hora
	        if (dto.getNuevaFecha().equals(cita.getFecha()) && 
	            dto.getNuevaHoraInicio().equals(cita.getHoraInicio())) {
	            throw new RuntimeException("La reprogramación debe ser para una fecha u hora diferente");
	        }

	        // Validar que no haya conflicto de horario para el trabajador
	        if (citaService.verificarConflictoHorario(cita.getIdTrabajador(), dto.getNuevaFecha(), 
	                dto.getNuevaHoraInicio(), dto.getNuevaHoraFin())) {
	            throw new RuntimeException("Ya existe una cita programada en ese horario para el trabajador");
	        }
	    }

	    // Crear nueva reprogramación
	    @Transactional
	    public ReprogramacionDTO crearReprogramacion(ReprogramacionDTO dto) {
	        validarReprogramacion(dto);
	        
	        // Verificar que la cita no tenga ya una reprogramación pendiente
	        if (reprogramacionRepository.existsPendienteByIdCita(dto.getIdCita())) {
	            throw new RuntimeException("Ya existe una reprogramación pendiente para esta cita");
	        }

	        Reprogramacion reprogramacion = convertToEntity(dto);
	        reprogramacion.setEstado(Reprogramacion.EstadoReprogramacion.PENDIENTE);
	        
	        Reprogramacion saved = reprogramacionRepository.save(reprogramacion);
	        return convertToDTO(saved);
	    }

	    // Obtener reprogramación por ID
	    public ReprogramacionDTO obtenerPorId(Long id) {
	        Reprogramacion reprogramacion = reprogramacionRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Reprogramación no encontrada"));
	        return convertToDTO(reprogramacion);
	    }

	    // Obtener todas las reprogramaciones de una cita
	    public List<ReprogramacionDTO> obtenerPorCita(Long idCita) {
	        return reprogramacionRepository.findByIdCita(idCita).stream()
	                .map(this::convertToDTO)
	                .collect(Collectors.toList());
	    }

	    // Obtener reprogramaciones por estado
	    public List<ReprogramacionDTO> obtenerPorEstado(Reprogramacion.EstadoReprogramacion estado) {
	        return reprogramacionRepository.findByEstado(estado).stream()
	                .map(this::convertToDTO)
	                .collect(Collectors.toList());
	    }

	    // Obtener reprogramaciones pendientes
	    public List<ReprogramacionDTO> obtenerPendientes() {
	        return reprogramacionRepository.findPendientes().stream()
	                .map(this::convertToDTO)
	                .collect(Collectors.toList());
	    }

	    // Obtener reprogramaciones aprobadas
	    public List<ReprogramacionDTO> obtenerAprobadas() {
	        return reprogramacionRepository.findAprobadas().stream()
	                .map(this::convertToDTO)
	                .collect(Collectors.toList());
	    }

	    // Obtener reprogramaciones rechazadas
	    public List<ReprogramacionDTO> obtenerRechazadas() {
	        return reprogramacionRepository.findRechazadas().stream()
	                .map(this::convertToDTO)
	                .collect(Collectors.toList());
	    }

	    // Obtener la última reprogramación de una cita
	    public ReprogramacionDTO obtenerUltimaPorCita(Long idCita) {
	        Reprogramacion reprogramacion = reprogramacionRepository.findFirstByIdCitaOrderByCreateAtDesc(idCita)
	                .orElseThrow(() -> new RuntimeException("No se encontró reprogramación para la cita"));
	        return convertToDTO(reprogramacion);
	    }

	    // Obtener reprogramaciones pendientes por cita
	    public List<ReprogramacionDTO> obtenerPendientesPorCita(Long idCita) {
	        return reprogramacionRepository.findPendientesByIdCita(idCita).stream()
	                .map(this::convertToDTO)
	                .collect(Collectors.toList());
	    }

	    // Aprobar reprogramación
	    @Transactional
	    public ReprogramacionDTO aprobarReprogramacion(Long id) {
	        Reprogramacion reprogramacion = reprogramacionRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Reprogramación no encontrada"));

	        // Validar que esté pendiente
	        if (reprogramacion.getEstado() != Reprogramacion.EstadoReprogramacion.PENDIENTE) {
	            throw new RuntimeException("Solo se pueden aprobar reprogramaciones pendientes");
	        }

	        // Obtener la cita original
	        Cita cita = citaRepository.findById(reprogramacion.getIdCita())
	                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

	        // Verificar que no haya conflicto de horario (por si acaso)
	        if (citaService.verificarConflictoExcluyendoCita(
	                cita.getIdTrabajador(), 
	                reprogramacion.getNuevaFecha(), 
	                reprogramacion.getNuevaHoraInicio(), 
	                reprogramacion.getNuevaHoraFin(), 
	                cita.getIdCita())) {
	            throw new RuntimeException("Ya existe una cita programada en ese horario para el trabajador");
	        }

	        // Actualizar la cita con los nuevos datos
	        cita.setFecha(reprogramacion.getNuevaFecha());
	        cita.setHoraInicio(reprogramacion.getNuevaHoraInicio());
	        cita.setHoraFin(reprogramacion.getNuevaHoraFin());
	        citaRepository.save(cita);

	        // Actualizar el estado de la reprogramación
	        reprogramacion.setEstado(Reprogramacion.EstadoReprogramacion.APROBADA);
	        Reprogramacion updated = reprogramacionRepository.save(reprogramacion);

	        return convertToDTO(updated);
	    }

	    // Rechazar reprogramación
	    @Transactional
	    public ReprogramacionDTO rechazarReprogramacion(Long id, String motivoRechazo) {
	        Reprogramacion reprogramacion = reprogramacionRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Reprogramación no encontrada"));

	        // Validar que esté pendiente
	        if (reprogramacion.getEstado() != Reprogramacion.EstadoReprogramacion.PENDIENTE) {
	            throw new RuntimeException("Solo se pueden rechazar reprogramaciones pendientes");
	        }

	        // Actualizar motivo si se proporciona uno adicional
	        if (motivoRechazo != null && !motivoRechazo.trim().isEmpty()) {
	            reprogramacion.setMotivo(reprogramacion.getMotivo() + " - Rechazado: " + motivoRechazo);
	        }

	        reprogramacion.setEstado(Reprogramacion.EstadoReprogramacion.RECHAZADA);
	        Reprogramacion updated = reprogramacionRepository.save(reprogramacion);

	        return convertToDTO(updated);
	    }

	    // Cambiar estado de reprogramación
	    @Transactional
	    public ReprogramacionDTO cambiarEstado(Long id, Reprogramacion.EstadoReprogramacion estado) {
	        Reprogramacion reprogramacion = reprogramacionRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Reprogramación no encontrada"));

	        validarTransicionEstado(reprogramacion.getEstado(), estado);
	        reprogramacion.setEstado(estado);
	        
	        // Si se aprueba, actualizar la cita
	        if (estado == Reprogramacion.EstadoReprogramacion.APROBADA) {
	            Cita cita = citaRepository.findById(reprogramacion.getIdCita())
	                    .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
	            
	            cita.setFecha(reprogramacion.getNuevaFecha());
	            cita.setHoraInicio(reprogramacion.getNuevaHoraInicio());
	            cita.setHoraFin(reprogramacion.getNuevaHoraFin());
	            citaRepository.save(cita);
	        }

	        Reprogramacion updated = reprogramacionRepository.save(reprogramacion);
	        return convertToDTO(updated);
	    }

	    // Validar transición de estado
	    private void validarTransicionEstado(Reprogramacion.EstadoReprogramacion estadoActual, 
	                                       Reprogramacion.EstadoReprogramacion nuevoEstado) {
	        if (estadoActual != Reprogramacion.EstadoReprogramacion.PENDIENTE) {
	            throw new RuntimeException("Solo se pueden modificar reprogramaciones pendientes");
	        }
	    }

	    // Eliminar reprogramación
	    @Transactional
	    public void eliminarReprogramacion(Long id) {
	        if (!reprogramacionRepository.existsById(id)) {
	            throw new RuntimeException("Reprogramación no encontrada");
	        }
	        reprogramacionRepository.deleteById(id);
	    }

	    // Verificar si existe reprogramación pendiente para una cita
	    public boolean existeReprogramacionPendiente(Long idCita) {
	        return reprogramacionRepository.existsPendienteByIdCita(idCita);
	    }

	    // Contar reprogramaciones pendientes por trabajador
	    public long contarReprogramacionesPendientesPorTrabajador(Long trabajadorId) {
	        return reprogramacionRepository.countReprogramacionesPendientesPorTrabajador(trabajadorId);
	    }

	    // Obtener reprogramaciones por cita y estado
	    public List<ReprogramacionDTO> obtenerPorCitaYEstado(Long idCita, Reprogramacion.EstadoReprogramacion estado) {
	        return reprogramacionRepository.findByIdCitaAndEstado(idCita, estado).stream()
	                .map(this::convertToDTO)
	                .collect(Collectors.toList());
	    }
}
