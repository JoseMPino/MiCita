package com.pinosoft.micita.controller;
import com.pinosoft.micita.dto.CategoriaServicioDTO;
import com.pinosoft.micita.model.CategoriaServicio;
import com.pinosoft.micita.service.CategoriaServicioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias-servicio")
public class CategoriaServicioController {
	 private final CategoriaServicioService categoriaServicioService;

	    public CategoriaServicioController(CategoriaServicioService categoriaServicioService) {
	        this.categoriaServicioService = categoriaServicioService;
	    }

	    @PostMapping
	    @PreAuthorize("hasRole('ADMINISTRADOR')")
	    public ResponseEntity<CategoriaServicio> crear(@Valid @RequestBody CategoriaServicioDTO dto) {
	        return new ResponseEntity<>(categoriaServicioService.crear(dto), HttpStatus.CREATED);
	    }

	    @GetMapping("/negocio/{idCategoriaNegocio}")
	    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'TRABAJADOR', 'CLIENTE')")
	    public ResponseEntity<List<CategoriaServicio>> listarPorCategoriaNegocio(@PathVariable Long idCategoriaNegocio) {
	        return ResponseEntity.ok(categoriaServicioService.obtenerPorCategoriaNegocio(idCategoriaNegocio));
	    }

	    @GetMapping("/{id}")
	    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'TRABAJADOR', 'CLIENTE')")
	    public ResponseEntity<CategoriaServicio> buscarPorId(@PathVariable Long id) {
	        return ResponseEntity.ok(categoriaServicioService.buscarPorId(id));
	    }

}
