package com.pinosoft.micita.controller;

import com.pinosoft.micita.dto.NegocioDTO;
import com.pinosoft.micita.model.Negocio;
import com.pinosoft.micita.service.NegocioService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/negocios")
public class NegocioController {
	private final NegocioService negocioService;

	public NegocioController(NegocioService negocioService) {
		this.negocioService = negocioService;
	}

	@PostMapping
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	public ResponseEntity<Negocio> crearNegocio(@Valid @RequestBody NegocioDTO negocioDTO, Authentication authentication) {
		Negocio nuevoNegocio = negocioService.crearNegocio(negocioDTO, authentication);
		return new ResponseEntity<>(nuevoNegocio, HttpStatus.CREATED);
	}

	@GetMapping("/all")
	@PreAuthorize("hasRole('ADMINISTRADOR','CLIENTE','TRABAJADOR')")
	public ResponseEntity<List<Negocio>> obtenerTodosLosNegocios() {
		List<Negocio> negocios = negocioService.obtenerTodosNegocios();
		return ResponseEntity.ok(negocios);
	}

	@GetMapping
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	public ResponseEntity<List<Negocio>> obtenerTodosLosNegociosAdmin(Authentication authentication) {
		List<Negocio> negocios = negocioService.obtenerNegociosDelAdministrador(authentication);
		return ResponseEntity.ok(negocios);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMINISTRADOR','CLIENTE')")
	public ResponseEntity<Negocio> obtenerNegocioPorId(@PathVariable Long id, Authentication authentication) {
		Negocio negocio = negocioService.obtenerNegocioPorId(id, authentication);
		return ResponseEntity.ok(negocio);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	public ResponseEntity<Negocio> actualizarNegocio(@PathVariable Long id, @Valid @RequestBody NegocioDTO negocioDTO,
			Authentication authentication) {
		Negocio negocioActualizado = negocioService.actualizarNegocio(id, negocioDTO, authentication);
		return ResponseEntity.ok(negocioActualizado);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	public ResponseEntity<Void> eliminarNegocio(@PathVariable Long id, Authentication authentication) {
		negocioService.eliminarNegocio(id, authentication);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/existe/{id}")
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	public ResponseEntity<Boolean> existeNegocio(@PathVariable Long id, Authentication authentication) {
		boolean existe = negocioService.existeNegocioParaAdministrador(id, authentication);
		return ResponseEntity.ok(existe);
	}

}
