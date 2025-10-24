package com.pinosoft.micita.controller;

import com.pinosoft.micita.dto.DisponibilidadBaseDTO;
import com.pinosoft.micita.service.DisponibilidadBaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disponibilidad-base")
@RequiredArgsConstructor
public class DisponibilidadBaseController {
	 private final DisponibilidadBaseService disponibilidadBaseService;

	    // Crear nueva disponibilidad base
	    @PostMapping
	    public ResponseEntity<DisponibilidadBaseDTO> crearDisponibilidadBase(
	            @Valid @RequestBody DisponibilidadBaseDTO disponibilidadBaseDTO) {
	        try {
	            DisponibilidadBaseDTO creado = disponibilidadBaseService.crearDisponibilidadBase(disponibilidadBaseDTO);
	            return new ResponseEntity<>(creado, HttpStatus.CREATED);
	        } catch (RuntimeException e) {
	            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	        }
	    }

	    // Obtener disponibilidad base por ID
	    @GetMapping("/{id}")
	    public ResponseEntity<DisponibilidadBaseDTO> obtenerPorId(@PathVariable Long id) {
	        try {
	            DisponibilidadBaseDTO dto = disponibilidadBaseService.obtenerPorId(id);
	            return ResponseEntity.ok(dto);
	        } catch (RuntimeException e) {
	            return ResponseEntity.notFound().build();
	        }
	    }

	    // Obtener todas las disponibilidades base de un trabajador
	    @GetMapping("/trabajador/{trabajadorId}")
	    public ResponseEntity<List<DisponibilidadBaseDTO>> obtenerPorTrabajador(
	            @PathVariable Long trabajadorId) {
	        List<DisponibilidadBaseDTO> disponibilidades = disponibilidadBaseService.obtenerPorTrabajador(trabajadorId);
	        return ResponseEntity.ok(disponibilidades);
	    }

	    // Obtener disponibilidades base activas de un trabajador
	    @GetMapping("/trabajador/{trabajadorId}/activas")
	    public ResponseEntity<List<DisponibilidadBaseDTO>> obtenerActivasPorTrabajador(
	            @PathVariable Long trabajadorId) {
	        List<DisponibilidadBaseDTO> disponibilidades = disponibilidadBaseService.obtenerActivasPorTrabajador(trabajadorId);
	        return ResponseEntity.ok(disponibilidades);
	    }

	    // Obtener disponibilidades base por trabajador y día de la semana
	    @GetMapping("/trabajador/{trabajadorId}/dia/{diaSemana}")
	    public ResponseEntity<List<DisponibilidadBaseDTO>> obtenerPorTrabajadorYDia(
	            @PathVariable Long trabajadorId, 
	            @PathVariable Integer diaSemana) {
	        List<DisponibilidadBaseDTO> disponibilidades = disponibilidadBaseService
	                .obtenerPorTrabajadorYDia(trabajadorId, diaSemana);
	        return ResponseEntity.ok(disponibilidades);
	    }

	    // Obtener disponibilidades base activas por trabajador y día
	    @GetMapping("/trabajador/{trabajadorId}/dia/{diaSemana}/activas")
	    public ResponseEntity<List<DisponibilidadBaseDTO>> obtenerActivasPorTrabajadorYDia(
	            @PathVariable Long trabajadorId, 
	            @PathVariable Integer diaSemana) {
	        List<DisponibilidadBaseDTO> disponibilidades = disponibilidadBaseService
	                .obtenerActivasPorTrabajadorYDia(trabajadorId, diaSemana);
	        return ResponseEntity.ok(disponibilidades);
	    }

	    // Actualizar disponibilidad base
	    @PutMapping("/{id}")
	    public ResponseEntity<DisponibilidadBaseDTO> actualizarDisponibilidadBase(
	            @PathVariable Long id, 
	            @Valid @RequestBody DisponibilidadBaseDTO disponibilidadBaseDTO) {
	        try {
	            DisponibilidadBaseDTO actualizado = disponibilidadBaseService
	                    .actualizarDisponibilidadBase(id, disponibilidadBaseDTO);
	            return ResponseEntity.ok(actualizado);
	        } catch (RuntimeException e) {
	            return ResponseEntity.notFound().build();
	        }
	    }

	    // Eliminar disponibilidad base
	    @DeleteMapping("/{id}")
	    public ResponseEntity<Void> eliminarDisponibilidadBase(@PathVariable Long id) {
	        try {
	            disponibilidadBaseService.eliminarDisponibilidadBase(id);
	            return ResponseEntity.noContent().build();
	        } catch (RuntimeException e) {
	            return ResponseEntity.notFound().build();
	        }
	    }

	    // Cambiar estado de la disponibilidad base
	    @PatchMapping("/{id}/estado")
	    public ResponseEntity<DisponibilidadBaseDTO> cambiarEstado(
	            @PathVariable Long id, 
	            @RequestParam Boolean estado) {
	        try {
	            DisponibilidadBaseDTO actualizado = disponibilidadBaseService.cambiarEstado(id, estado);
	            return ResponseEntity.ok(actualizado);
	        } catch (RuntimeException e) {
	            return ResponseEntity.notFound().build();
	        }
	    }

	    // Verificar si existe disponibilidad activa
	    @GetMapping("/existe-activa")
	    public ResponseEntity<Boolean> existeDisponibilidadActiva(
	            @RequestParam Long trabajadorId, 
	            @RequestParam Integer diaSemana) {
	        boolean existe = disponibilidadBaseService.existeDisponibilidadActiva(trabajadorId, diaSemana);
	        return ResponseEntity.ok(existe);
	    }

	    // Contar disponibilidades activas por trabajador
	    @GetMapping("/trabajador/{trabajadorId}/contar-activas")
	    public ResponseEntity<Long> contarDisponibilidadesActivas(@PathVariable Long trabajadorId) {
	        long count = disponibilidadBaseService.contarDisponibilidadesActivas(trabajadorId);
	        return ResponseEntity.ok(count);
	    }

	
}
