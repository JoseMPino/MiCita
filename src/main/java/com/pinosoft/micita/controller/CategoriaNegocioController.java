package com.pinosoft.micita.controller;
import com.pinosoft.micita.dto.CategoriaNegocioDTO;
import com.pinosoft.micita.model.CategoriaNegocio;
import com.pinosoft.micita.service.CategoriaNegocioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias-negocio")
public class CategoriaNegocioController {
	 private final CategoriaNegocioService categoriaNegocioService;

	    public CategoriaNegocioController(CategoriaNegocioService categoriaNegocioService) {
	        this.categoriaNegocioService = categoriaNegocioService;
	    }

	    @PostMapping
	    @PreAuthorize("hasRole('ADMINISTRADOR')")
	    public ResponseEntity<CategoriaNegocio> crear(@Valid @RequestBody CategoriaNegocioDTO dto) {
	        return new ResponseEntity<>(categoriaNegocioService.crear(dto), HttpStatus.CREATED);
	    }

	    @GetMapping
	    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'TRABAJADOR', 'CLIENTE')")
	    public ResponseEntity<List<CategoriaNegocio>> listar() {
	        return ResponseEntity.ok(categoriaNegocioService.listar());
	    }

	    @GetMapping("/{id}")
	    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'TRABAJADOR', 'CLIENTE')")
	    public ResponseEntity<CategoriaNegocio> buscarPorId(@PathVariable Long id) {
	        return ResponseEntity.ok(categoriaNegocioService.buscarPorId(id));
	    }

}
