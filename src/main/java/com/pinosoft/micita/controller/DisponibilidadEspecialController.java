package com.pinosoft.micita.controller;

import com.pinosoft.micita.dto.DisponibilidadEspecialDTO;
import com.pinosoft.micita.model.DisponibilidadEspecial;
import com.pinosoft.micita.service.DisponibilidadEspecialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/disponibilidad-especial")
@RequiredArgsConstructor
public class DisponibilidadEspecialController {

	private final DisponibilidadEspecialService disponibilidadEspecialService;

    // Crear nueva disponibilidad especial
    @PostMapping
    public ResponseEntity<DisponibilidadEspecialDTO> crearDisponibilidadEspecial(
            @Valid @RequestBody DisponibilidadEspecialDTO disponibilidadEspecialDTO) {
        try {
            DisponibilidadEspecialDTO creado = disponibilidadEspecialService.crearDisponibilidadEspecial(disponibilidadEspecialDTO);
            return new ResponseEntity<>(creado, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Obtener disponibilidad especial por ID
    @GetMapping("/{id}")
    public ResponseEntity<DisponibilidadEspecialDTO> obtenerPorId(@PathVariable Long id) {
        try {
            DisponibilidadEspecialDTO dto = disponibilidadEspecialService.obtenerPorId(id);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Obtener todas las disponibilidades especiales de un trabajador
    @GetMapping("/trabajador/{trabajadorId}")
    public ResponseEntity<List<DisponibilidadEspecialDTO>> obtenerPorTrabajador(
            @PathVariable Long trabajadorId) {
        List<DisponibilidadEspecialDTO> disponibilidades = disponibilidadEspecialService.obtenerPorTrabajador(trabajadorId);
        return ResponseEntity.ok(disponibilidades);
    }

    // Obtener disponibilidades especiales por trabajador y fecha
    @GetMapping("/trabajador/{trabajadorId}/fecha/{fecha}")
    public ResponseEntity<List<DisponibilidadEspecialDTO>> obtenerPorTrabajadorYFecha(
            @PathVariable Long trabajadorId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        List<DisponibilidadEspecialDTO> disponibilidades = disponibilidadEspecialService
                .obtenerPorTrabajadorYFecha(trabajadorId, fecha);
        return ResponseEntity.ok(disponibilidades);
    }

    // Obtener disponibilidades especiales por trabajador y rango de fechas
    @GetMapping("/trabajador/{trabajadorId}/rango-fechas")
    public ResponseEntity<List<DisponibilidadEspecialDTO>> obtenerPorTrabajadorYRangoFechas(
            @PathVariable Long trabajadorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        List<DisponibilidadEspecialDTO> disponibilidades = disponibilidadEspecialService
                .obtenerPorTrabajadorYRangoFechas(trabajadorId, fechaInicio, fechaFin);
        return ResponseEntity.ok(disponibilidades);
    }

    // Obtener bloques por trabajador y fecha
    @GetMapping("/trabajador/{trabajadorId}/fecha/{fecha}/bloqueos")
    public ResponseEntity<List<DisponibilidadEspecialDTO>> obtenerBloquesPorTrabajadorYFecha(
            @PathVariable Long trabajadorId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        List<DisponibilidadEspecialDTO> bloqueos = disponibilidadEspecialService
                .obtenerBloquesPorTrabajadorYFecha(trabajadorId, fecha);
        return ResponseEntity.ok(bloqueos);
    }

    // Obtener disponibilidades extra por trabajador y fecha
    @GetMapping("/trabajador/{trabajadorId}/fecha/{fecha}/extras")
    public ResponseEntity<List<DisponibilidadEspecialDTO>> obtenerExtrasPorTrabajadorYFecha(
            @PathVariable Long trabajadorId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        List<DisponibilidadEspecialDTO> extras = disponibilidadEspecialService
                .obtenerExtrasPorTrabajadorYFecha(trabajadorId, fecha);
        return ResponseEntity.ok(extras);
    }

    // Obtener disponibilidades especiales por tipo
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<DisponibilidadEspecialDTO>> obtenerPorTipo(@PathVariable String tipo) {
    	try {
            // Usar el enum del DTO para la conversión
            DisponibilidadEspecialDTO.TipoDisponibilidad tipoDTO = 
                DisponibilidadEspecialDTO.TipoDisponibilidad.valueOf(tipo.toUpperCase());
            
            // Convertir al enum de la entidad para el servicio
            DisponibilidadEspecial.TipoDisponibilidad tipoEntidad = 
                DisponibilidadEspecial.TipoDisponibilidad.valueOf(tipoDTO.name());
            
            List<DisponibilidadEspecialDTO> disponibilidades = disponibilidadEspecialService.obtenerPorTipo(tipoEntidad);
            return ResponseEntity.ok(disponibilidades);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Obtener disponibilidades especiales futuras por trabajador
    @GetMapping("/trabajador/{trabajadorId}/futuras")
    public ResponseEntity<List<DisponibilidadEspecialDTO>> obtenerFuturasPorTrabajador(@PathVariable Long trabajadorId) {
        List<DisponibilidadEspecialDTO> disponibilidades = disponibilidadEspecialService
                .obtenerFuturasPorTrabajador(trabajadorId);
        return ResponseEntity.ok(disponibilidades);
    }

    // Verificar si existe bloqueo en horario
    @GetMapping("/verificar-bloqueo")
    public ResponseEntity<Boolean> verificarBloqueo(
            @RequestParam Long trabajadorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam String horaInicio,
            @RequestParam String horaFin) {
        boolean existeBloqueo = disponibilidadEspecialService.existeBloqueoEnHorario(trabajadorId, fecha, horaInicio, horaFin);
        return ResponseEntity.ok(existeBloqueo);
    }

    // Actualizar disponibilidad especial
    @PutMapping("/{id}")
    public ResponseEntity<DisponibilidadEspecialDTO> actualizarDisponibilidadEspecial(
            @PathVariable Long id,
            @Valid @RequestBody DisponibilidadEspecialDTO disponibilidadEspecialDTO) {
        try {
            DisponibilidadEspecialDTO actualizado = disponibilidadEspecialService
                    .actualizarDisponibilidadEspecial(id, disponibilidadEspecialDTO);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar disponibilidad especial
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDisponibilidadEspecial(@PathVariable Long id) {
        try {
            disponibilidadEspecialService.eliminarDisponibilidadEspecial(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar disponibilidades especiales por trabajador y fecha
    @DeleteMapping("/trabajador/{trabajadorId}/fecha/{fecha}")
    public ResponseEntity<Void> eliminarPorTrabajadorYFecha(
            @PathVariable Long trabajadorId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        try {
            disponibilidadEspecialService.eliminarPorTrabajadorYFecha(trabajadorId, fecha);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
