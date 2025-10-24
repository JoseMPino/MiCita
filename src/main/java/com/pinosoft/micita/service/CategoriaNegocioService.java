package com.pinosoft.micita.service;

import com.pinosoft.micita.dto.CategoriaNegocioDTO;
import com.pinosoft.micita.model.CategoriaNegocio;
import com.pinosoft.micita.repository.CategoriaNegocioRepository;

import org.springframework.stereotype.Service;

import java.util.List;

import com.pinosoft.micita.exception.CitaException;

import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoriaNegocioService {

	private final CategoriaNegocioRepository categoriaNegocioRepository;

	public CategoriaNegocioService(CategoriaNegocioRepository repository) {
		this.categoriaNegocioRepository = repository;
	}

	public List<CategoriaNegocio> listar() {
		return categoriaNegocioRepository.findAll();
	}

	@Transactional
	public CategoriaNegocio crear(CategoriaNegocioDTO dto) {
		if (categoriaNegocioRepository.existsByNombre(dto.getNombre())) {
			throw CitaException.businessRuleViolation("Ya existe una categoría con ese nombre");
		}

		CategoriaNegocio categoria = new CategoriaNegocio();
		categoria.setNombre(dto.getNombre());
		categoria.setDescripcion(dto.getDescripcion());
		return categoriaNegocioRepository.save(categoria);
	}

	public CategoriaNegocio buscarPorId(Long id) {
		return categoriaNegocioRepository.findById(id)
				.orElseThrow(() -> CitaException.resourceNotFound("Categoría no encontrada"));
	}

}
