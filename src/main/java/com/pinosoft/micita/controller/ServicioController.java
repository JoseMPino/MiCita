package com.pinosoft.micita.controller;

import com.pinosoft.micita.dto.ServicioClienteDTO;
import com.pinosoft.micita.dto.ServicioDTO;
import com.pinosoft.micita.service.ServicioService;

import jakarta.validation.Valid;

import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/servicios")
public class ServicioController {
	private final ServicioService servicioService;
	
	public ServicioController(ServicioService servicioService) {
		this.servicioService = servicioService;
	}

	@PostMapping
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	public ResponseEntity<ServicioDTO> crearServicio(@Valid @RequestBody ServicioDTO servicioDTO) {
		ServicioDTO servicioCreado = servicioService.crearServicio(servicioDTO);
		return new ResponseEntity<>(servicioCreado, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	public ResponseEntity<ServicioDTO> actualizarServicio(@PathVariable Long id,
			@Valid @RequestBody ServicioDTO servicioDTO) {
		ServicioDTO servicioActualizado = servicioService.actualizarServicio(id, servicioDTO);
		return ResponseEntity.ok(servicioActualizado);
	}

	@PatchMapping("/{id}/estado")
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	public ResponseEntity<ServicioDTO> actualizarEstadoServicio(@PathVariable Long id, @RequestBody Boolean estado) {
		ServicioDTO servicioActualizado = servicioService.actualizarEstadoServicio(id, estado);
		return ResponseEntity.ok(servicioActualizado);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	public ResponseEntity<Void> eliminarServicio(@PathVariable Long id) {
		servicioService.eliminarServicio(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/negocio/{negocioId}")
	@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'TRABAJADOR', 'CLIENTE')")
	public ResponseEntity<?> obtenerServiciosPorNegocio(@PathVariable Long negocioId,
			@RequestParam(required = false) Boolean estado) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		boolean esCliente = auth.getAuthorities().stream().anyMatch(rol -> rol.getAuthority().equals("ROLE_CLIENTE"));

		if (esCliente) {
			List<ServicioClienteDTO> servicios;
			if (estado != null) {
				servicios = servicioService.obtenerPorNegocioYEstadoParaCliente(negocioId, estado);
			} else {
				servicios = servicioService.obtenerPorNegocioParaCliente(negocioId);
			}
			return ResponseEntity.ok(servicios);
		} else {
			List<ServicioDTO> servicios;
			if (estado != null) {
				servicios = servicioService.obtenerPorNegocioYEstado(negocioId, estado);
			} else {
				servicios = servicioService.obtenerPorNegocio(negocioId);
			}
			return ResponseEntity.ok(servicios);
		}
	}

	@GetMapping("/negocio/{negocioId}/activos")
	@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'TRABAJADOR', 'CLIENTE')")
	public ResponseEntity<?> obtenerServiciosActivosPorNegocio(@PathVariable Long negocioId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		boolean esCliente = auth.getAuthorities().stream().anyMatch(rol -> rol.getAuthority().equals("ROLE_CLIENTE"));

		if (esCliente) {
			List<ServicioClienteDTO> servicios = servicioService.obtenerPorNegocioYEstadoParaCliente(negocioId, true);
			return ResponseEntity.ok(servicios);
		} else {
			List<ServicioDTO> servicios = servicioService.obtenerPorNegocioYEstado(negocioId, true);
			return ResponseEntity.ok(servicios);
		}
	}

	@GetMapping("/negocio/{negocioId}/inactivos")
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	public ResponseEntity<List<ServicioDTO>> obtenerServiciosInactivosPorNegocio(@PathVariable Long negocioId) {
		List<ServicioDTO> servicios = servicioService.obtenerPorNegocioYEstado(negocioId, false);
		return ResponseEntity.ok(servicios);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'TRABAJADOR', 'CLIENTE')")
	public ResponseEntity<?> obtenerServicioPorId(@PathVariable Long id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		boolean esCliente = auth.getAuthorities().stream().anyMatch(rol -> rol.getAuthority().equals("ROLE_CLIENTE"));

		if (esCliente) {
			ServicioClienteDTO servicio = servicioService.obtenerPorIdParaCliente(id);
			return ResponseEntity.ok(servicio);
		} else {
			ServicioDTO servicio = servicioService.obtenerPorId(id);
			return ResponseEntity.ok(servicio);
		}
	}

	@GetMapping("/negocio/{negocioId}/cliente")
	@PreAuthorize("hasRole('CLIENTE')")
	public ResponseEntity<List<ServicioClienteDTO>> obtenerServiciosPorNegocioParaCliente(
			@PathVariable Long negocioId) {
		List<ServicioClienteDTO> servicios = servicioService.obtenerPorNegocioParaCliente(negocioId);
		return ResponseEntity.ok(servicios);
	}

	@GetMapping("/negocio/{negocioId}/cliente/activos")
	@PreAuthorize("hasRole('CLIENTE')")
	public ResponseEntity<List<ServicioClienteDTO>> obtenerServiciosActivosPorNegocioParaCliente(
			@PathVariable Long negocioId) {
		List<ServicioClienteDTO> servicios = servicioService.obtenerPorNegocioYEstadoParaCliente(negocioId, true);
		return ResponseEntity.ok(servicios);
	}

}
