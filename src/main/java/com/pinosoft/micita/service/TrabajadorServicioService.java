package com.pinosoft.micita.service;

import com.pinosoft.micita.dto.TrabajadorServicioDTO;
import com.pinosoft.micita.model.TrabajadorServicio;
import com.pinosoft.micita.repository.TrabajadorServicioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrabajadorServicioService {

	private final TrabajadorServicioRepository trabajadorServicioRepository;

    // Convertir Entity a DTO
    private TrabajadorServicioDTO convertToDTO(TrabajadorServicio trabajadorServicio) {
        TrabajadorServicioDTO dto = new TrabajadorServicioDTO();
        dto.setId(trabajadorServicio.getId());
        dto.setTrabajadorId(trabajadorServicio.getTrabajadorId());
        dto.setServicioId(trabajadorServicio.getServicioId());
        dto.setCreadoEn(trabajadorServicio.getCreadoEn());
        dto.setConfirmacionAutomatica(trabajadorServicio.getConfirmacionAutomatica());
        return dto;
    }

    // Convertir DTO a Entity
    private TrabajadorServicio convertToEntity(TrabajadorServicioDTO dto) {
        TrabajadorServicio trabajadorServicio = new TrabajadorServicio();
        trabajadorServicio.setId(dto.getId());
        trabajadorServicio.setTrabajadorId(dto.getTrabajadorId());
        trabajadorServicio.setServicioId(dto.getServicioId());
        trabajadorServicio.setConfirmacionAutomatica(dto.getConfirmacionAutomatica());
        // creadoEn se genera automáticamente con @PrePersist
        return trabajadorServicio;
    }

    // Crear nueva relación trabajador-servicio
    @Transactional
    public TrabajadorServicioDTO crearTrabajadorServicio(TrabajadorServicioDTO dto) {
        // Verificar si ya existe la relación
        if (trabajadorServicioRepository.existsByTrabajadorIdAndServicioId(
                dto.getTrabajadorId(), dto.getServicioId())) {
            throw new RuntimeException("Ya existe esta relación trabajador-servicio");
        }

        TrabajadorServicio trabajadorServicio = convertToEntity(dto);
        TrabajadorServicio saved = trabajadorServicioRepository.save(trabajadorServicio);
        return convertToDTO(saved);
    }

    // Obtener relación por ID
    public TrabajadorServicioDTO obtenerPorId(Long id) {
        TrabajadorServicio trabajadorServicio = trabajadorServicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Relación trabajador-servicio no encontrada"));
        return convertToDTO(trabajadorServicio);
    }

    // Obtener todos los servicios de un trabajador
    public List<TrabajadorServicioDTO> obtenerServiciosPorTrabajador(Long trabajadorId) {
        return trabajadorServicioRepository.findByTrabajadorId(trabajadorId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Obtener todos los trabajadores de un servicio
    public List<TrabajadorServicioDTO> obtenerTrabajadoresPorServicio(Long servicioId) {
        return trabajadorServicioRepository.findByServicioId(servicioId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Obtener servicios con confirmación automática por trabajador
    public List<TrabajadorServicioDTO> obtenerServiciosConConfirmacionAutomatica(Long trabajadorId) {
        return trabajadorServicioRepository.findByTrabajadorIdAndConfirmacionAutomaticaTrue(trabajadorId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Actualizar relación trabajador-servicio
    @Transactional
    public TrabajadorServicioDTO actualizarTrabajadorServicio(Long id, TrabajadorServicioDTO dto) {
        TrabajadorServicio existing = trabajadorServicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Relación trabajador-servicio no encontrada"));

        // Verificar si la nueva relación ya existe (excluyendo el actual)
        if (!existing.getTrabajadorId().equals(dto.getTrabajadorId()) || 
            !existing.getServicioId().equals(dto.getServicioId())) {
            
            if (trabajadorServicioRepository.existsByTrabajadorIdAndServicioId(
                    dto.getTrabajadorId(), dto.getServicioId())) {
                throw new RuntimeException("Ya existe esta relación trabajador-servicio");
            }
        }

        existing.setTrabajadorId(dto.getTrabajadorId());
        existing.setServicioId(dto.getServicioId());
        existing.setConfirmacionAutomatica(dto.getConfirmacionAutomatica());

        TrabajadorServicio updated = trabajadorServicioRepository.save(existing);
        return convertToDTO(updated);
    }

    // Eliminar relación trabajador-servicio
    @Transactional
    public void eliminarTrabajadorServicio(Long id) {
        if (!trabajadorServicioRepository.existsById(id)) {
            throw new RuntimeException("Relación trabajador-servicio no encontrada");
        }
        trabajadorServicioRepository.deleteById(id);
    }

    // Eliminar relación específica por trabajador y servicio
    @Transactional
    public void eliminarPorTrabajadorYServicio(Long trabajadorId, Long servicioId) {
        TrabajadorServicio relación = trabajadorServicioRepository
                .findByTrabajadorIdAndServicioId(trabajadorId, servicioId)
                .orElseThrow(() -> new RuntimeException("Relación trabajador-servicio no encontrada"));
        
        trabajadorServicioRepository.delete(relación);
    }

    // Verificar si existe relación
    public boolean existeRelacion(Long trabajadorId, Long servicioId) {
        return trabajadorServicioRepository.existsByTrabajadorIdAndServicioId(trabajadorId, servicioId);
    }

}
