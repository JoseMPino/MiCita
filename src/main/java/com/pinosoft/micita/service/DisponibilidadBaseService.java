package com.pinosoft.micita.service;

import com.pinosoft.micita.dto.DisponibilidadBaseDTO;
import com.pinosoft.micita.model.DisponibilidadBase;
import com.pinosoft.micita.repository.DisponibilidadBaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DisponibilidadBaseService {
	private final DisponibilidadBaseRepository disponibilidadBaseRepository;

    // Convertir Entity a DTO
    private DisponibilidadBaseDTO convertToDTO(DisponibilidadBase disponibilidadBase) {
        DisponibilidadBaseDTO dto = new DisponibilidadBaseDTO();
        dto.setIdDisponibilidadBase(disponibilidadBase.getIdDisponibilidadBase());
        dto.setIdTrabajador(disponibilidadBase.getIdTrabajador());
        dto.setDiaSemana(disponibilidadBase.getDiaSemana());
        dto.setHoraInicio(disponibilidadBase.getHoraInicio());
        dto.setHoraFin(disponibilidadBase.getHoraFin());
        dto.setEstado(disponibilidadBase.getEstado());
        return dto;
    }

    // Convertir DTO a Entity
    private DisponibilidadBase convertToEntity(DisponibilidadBaseDTO dto) {
        DisponibilidadBase disponibilidadBase = new DisponibilidadBase();
        disponibilidadBase.setIdDisponibilidadBase(dto.getIdDisponibilidadBase());
        disponibilidadBase.setIdTrabajador(dto.getIdTrabajador());
        disponibilidadBase.setDiaSemana(dto.getDiaSemana());
        disponibilidadBase.setHoraInicio(dto.getHoraInicio());
        disponibilidadBase.setHoraFin(dto.getHoraFin());
        disponibilidadBase.setEstado(dto.getEstado());
        return disponibilidadBase;
    }

    // Validar horario
    private void validarHorario(DisponibilidadBaseDTO dto) {
        if (dto.getDiaSemana() < 1 || dto.getDiaSemana() > 7) {
            throw new RuntimeException("El día de la semana debe estar entre 1 y 7");
        }
        
        // Validar formato de horas y que horaInicio < horaFin
        if (dto.getHoraInicio().compareTo(dto.getHoraFin()) >= 0) {
            throw new RuntimeException("La hora de inicio debe ser menor que la hora de fin");
        }
    }

    // Crear nueva disponibilidad base
    @Transactional
    public DisponibilidadBaseDTO crearDisponibilidadBase(DisponibilidadBaseDTO dto) {
        validarHorario(dto);
        
        // Verificar si ya existe una disponibilidad para el mismo día y trabajador que se solape
        List<DisponibilidadBase> existentes = disponibilidadBaseRepository
                .findByIdTrabajadorAndDiaSemana(dto.getIdTrabajador(), dto.getDiaSemana());
        
        for (DisponibilidadBase existente : existentes) {
            if (existeSolapamiento(existente, dto.getHoraInicio(), dto.getHoraFin())) {
                throw new RuntimeException("Ya existe una disponibilidad que se solapa con este horario");
            }
        }

        DisponibilidadBase disponibilidadBase = convertToEntity(dto);
        DisponibilidadBase saved = disponibilidadBaseRepository.save(disponibilidadBase);
        return convertToDTO(saved);
    }

    // Verificar solapamiento de horarios
    private boolean existeSolapamiento(DisponibilidadBase existente, String nuevaHoraInicio, String nuevaHoraFin) {
        return (nuevaHoraInicio.compareTo(existente.getHoraFin()) < 0 && 
                nuevaHoraFin.compareTo(existente.getHoraInicio()) > 0);
    }

    // Obtener disponibilidad base por ID
    public DisponibilidadBaseDTO obtenerPorId(Long id) {
        DisponibilidadBase disponibilidadBase = disponibilidadBaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disponibilidad base no encontrada"));
        return convertToDTO(disponibilidadBase);
    }

    // Obtener todas las disponibilidades base de un trabajador
    public List<DisponibilidadBaseDTO> obtenerPorTrabajador(Long trabajadorId) {
        return disponibilidadBaseRepository.findByIdTrabajador(trabajadorId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Obtener disponibilidades base activas de un trabajador
    public List<DisponibilidadBaseDTO> obtenerActivasPorTrabajador(Long trabajadorId) {
        return disponibilidadBaseRepository.findByIdTrabajadorAndEstadoTrue(trabajadorId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Obtener disponibilidades base por trabajador y día de la semana
    public List<DisponibilidadBaseDTO> obtenerPorTrabajadorYDia(Long trabajadorId, Integer diaSemana) {
        return disponibilidadBaseRepository.findByIdTrabajadorAndDiaSemana(trabajadorId, diaSemana)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Obtener disponibilidades base activas por trabajador y día
    public List<DisponibilidadBaseDTO> obtenerActivasPorTrabajadorYDia(Long trabajadorId, Integer diaSemana) {
        return disponibilidadBaseRepository.findByIdTrabajadorAndDiaSemanaAndEstadoTrue(trabajadorId, diaSemana)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Actualizar disponibilidad base
    @Transactional
    public DisponibilidadBaseDTO actualizarDisponibilidadBase(Long id, DisponibilidadBaseDTO dto) {
        validarHorario(dto);
        
        DisponibilidadBase existing = disponibilidadBaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disponibilidad base no encontrada"));

        // Verificar solapamiento excluyendo la actual
        List<DisponibilidadBase> existentes = disponibilidadBaseRepository
                .findByIdTrabajadorAndDiaSemana(dto.getIdTrabajador(), dto.getDiaSemana());
        
        for (DisponibilidadBase existente : existentes) {
            if (!existente.getIdDisponibilidadBase().equals(id) && 
                existeSolapamiento(existente, dto.getHoraInicio(), dto.getHoraFin())) {
                throw new RuntimeException("Ya existe una disponibilidad que se solapa con este horario");
            }
        }

        existing.setIdTrabajador(dto.getIdTrabajador());
        existing.setDiaSemana(dto.getDiaSemana());
        existing.setHoraInicio(dto.getHoraInicio());
        existing.setHoraFin(dto.getHoraFin());
        existing.setEstado(dto.getEstado());

        DisponibilidadBase updated = disponibilidadBaseRepository.save(existing);
        return convertToDTO(updated);
    }

    // Eliminar disponibilidad base
    @Transactional
    public void eliminarDisponibilidadBase(Long id) {
        if (!disponibilidadBaseRepository.existsById(id)) {
            throw new RuntimeException("Disponibilidad base no encontrada");
        }
        disponibilidadBaseRepository.deleteById(id);
    }

    // Activar/desactivar disponibilidad base
    @Transactional
    public DisponibilidadBaseDTO cambiarEstado(Long id, Boolean estado) {
        DisponibilidadBase disponibilidadBase = disponibilidadBaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disponibilidad base no encontrada"));
        
        disponibilidadBase.setEstado(estado);
        DisponibilidadBase updated = disponibilidadBaseRepository.save(disponibilidadBase);
        return convertToDTO(updated);
    }

    // Verificar si existe disponibilidad base activa para un trabajador en un día específico
    public boolean existeDisponibilidadActiva(Long trabajadorId, Integer diaSemana) {
        return disponibilidadBaseRepository.existsByIdTrabajadorAndDiaSemanaAndEstadoTrue(trabajadorId, diaSemana);
    }

    // Contar disponibilidades base activas por trabajador
    public long contarDisponibilidadesActivas(Long trabajadorId) {
        return disponibilidadBaseRepository.countByIdTrabajadorAndEstadoTrue(trabajadorId);
    }
}
