package com.pinosoft.micita.controller;

import com.pinosoft.micita.dto.TrabajadorServicioDTO;
import com.pinosoft.micita.service.TrabajadorServicioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trabajador-servicios")
@RequiredArgsConstructor
public class TrabajadorServicioController {

	 private final TrabajadorServicioService trabajadorServicioService;

	    // Crear nueva relación trabajador-servicio
	    @PostMapping
	    public ResponseEntity<TrabajadorServicioDTO> crearTrabajadorServicio(
	            @Valid @RequestBody TrabajadorServicioDTO trabajadorServicioDTO) {
	        try {
	            TrabajadorServicioDTO creado = trabajadorServicioService.crearTrabajadorServicio(trabajadorServicioDTO);
	            return new ResponseEntity<>(creado, HttpStatus.CREATED);
	        } catch (RuntimeException e) {
	            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	        }
	    }

	    // Obtener relación por ID
	    @GetMapping("/{id}")
	    public ResponseEntity<TrabajadorServicioDTO> obtenerPorId(@PathVariable Long id) {
	        try {
	            TrabajadorServicioDTO dto = trabajadorServicioService.obtenerPorId(id);
	            return ResponseEntity.ok(dto);
	        } catch (RuntimeException e) {
	            return ResponseEntity.notFound().build();
	        }
	    }

	    // Obtener todos los servicios de un trabajador
	    @GetMapping("/trabajador/{trabajadorId}")
	    public ResponseEntity<List<TrabajadorServicioDTO>> obtenerServiciosPorTrabajador(
	            @PathVariable Long trabajadorId) {
	        List<TrabajadorServicioDTO> servicios = trabajadorServicioService.obtenerServiciosPorTrabajador(trabajadorId);
	        return ResponseEntity.ok(servicios);
	    }

	    // Obtener todos los trabajadores de un servicio
	    @GetMapping("/servicio/{servicioId}")
	    public ResponseEntity<List<TrabajadorServicioDTO>> obtenerTrabajadoresPorServicio(
	            @PathVariable Long servicioId) {
	        List<TrabajadorServicioDTO> trabajadores = trabajadorServicioService.obtenerTrabajadoresPorServicio(servicioId);
	        return ResponseEntity.ok(trabajadores);
	    }

	    // Obtener servicios con confirmación automática por trabajador
	    @GetMapping("/trabajador/{trabajadorId}/confirmacion-automatica")
	    public ResponseEntity<List<TrabajadorServicioDTO>> obtenerServiciosConConfirmacionAutomatica(
	            @PathVariable Long trabajadorId) {
	        List<TrabajadorServicioDTO> servicios = trabajadorServicioService
	                .obtenerServiciosConConfirmacionAutomatica(trabajadorId);
	        return ResponseEntity.ok(servicios);
	    }

	    // Actualizar relación trabajador-servicio
	    @PutMapping("/{id}")
	    public ResponseEntity<TrabajadorServicioDTO> actualizarTrabajadorServicio(
	            @PathVariable Long id, 
	            @Valid @RequestBody TrabajadorServicioDTO trabajadorServicioDTO) {
	        try {
	            TrabajadorServicioDTO actualizado = trabajadorServicioService.actualizarTrabajadorServicio(id, trabajadorServicioDTO);
	            return ResponseEntity.ok(actualizado);
	        } catch (RuntimeException e) {
	            return ResponseEntity.notFound().build();
	        }
	    }

	    // Eliminar relación por ID
	    @DeleteMapping("/{id}")
	    public ResponseEntity<Void> eliminarTrabajadorServicio(@PathVariable Long id) {
	        try {
	            trabajadorServicioService.eliminarTrabajadorServicio(id);
	            return ResponseEntity.noContent().build();
	        } catch (RuntimeException e) {
	            return ResponseEntity.notFound().build();
	        }
	    }

	    // Eliminar relación específica por trabajador y servicio
	    @DeleteMapping("/trabajador/{trabajadorId}/servicio/{servicioId}")
	    public ResponseEntity<Void> eliminarPorTrabajadorYServicio(
	            @PathVariable Long trabajadorId, 
	            @PathVariable Long servicioId) {
	        try {
	            trabajadorServicioService.eliminarPorTrabajadorYServicio(trabajadorId, servicioId);
	            return ResponseEntity.noContent().build();
	        } catch (RuntimeException e) {
	            return ResponseEntity.notFound().build();
	        }
	    }

	    // Verificar si existe relación
	    @GetMapping("/existe")
	    public ResponseEntity<Boolean> existeRelacion(
	            @RequestParam Long trabajadorId, 
	            @RequestParam Long servicioId) {
	        boolean existe = trabajadorServicioService.existeRelacion(trabajadorId, servicioId);
	        return ResponseEntity.ok(existe);
	    }

}
